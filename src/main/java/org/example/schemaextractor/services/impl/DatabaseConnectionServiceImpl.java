package org.example.schemaextractor.services.impl;

import org.example.schemaextractor.domain.entities.*;
import org.example.schemaextractor.repository.*;
import org.example.schemaextractor.services.DatabaseConnectionService;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

@Service
public class DatabaseConnectionServiceImpl implements DatabaseConnectionService {

    private final ReportRepository reportRepository;
    private final CatalogRepository catalogRepository;
    private final TableRepository tableRepository;
    private final ColumnRepository columnRepository;
    private final ConstraintRepository constraintRepository;
    private final IndexRepository indexRepository;

    public DatabaseConnectionServiceImpl(
            ReportRepository reportRepository,
            CatalogRepository catalogRepository,
            TableRepository tableRepository,
            ColumnRepository columnRepository,
            ConstraintRepository constraintRepository,
            IndexRepository indexRepository
    ) {
        this.reportRepository = reportRepository;
        this.catalogRepository = catalogRepository;
        this.tableRepository = tableRepository;
        this.columnRepository = columnRepository;
        this.constraintRepository = constraintRepository;
        this.indexRepository = indexRepository;
    }

    @Override
    public Long connect(String url, String username, String password) throws SQLException {
        try (
                Connection conn = DriverManager.getConnection(url, username, password)
        ) {

            System.out.println("Connection established successfully");

            // getting metadata
            DatabaseMetaData metaData = conn.getMetaData();

            ReportEntity savedReportEntity = saveDatabaseEntity(metaData, url, username);
//            System.out.println(databaseEntity.getId());

            ResultSet catalogs = metaData.getCatalogs();
            while (catalogs.next()) {
                // save catalog
                String catalog = catalogs.getString("TABLE_CAT");
                CatalogEntity catalogEntity = new CatalogEntity(catalog, savedReportEntity);
                CatalogEntity savedCatalogEntity = catalogRepository.save(catalogEntity);


                System.out.println("-------------------------------------------");
                System.out.println("    DATABASE: " + catalog);
                System.out.println("-------------------------------------------");
                ResultSet tables = metaData.getTables(catalog, null, null, new String[]{"TABLE"});
                while (tables.next()) {
                    String tableType = tables.getString("TABLE_TYPE");
                    if (!Objects.equals(tableType, "TABLE")) {
                        continue;
                    }

                    String tableCat = tables.getString("TABLE_CAT");
                    String tableSchema = tables.getString("TABLE_SCHEM");
                    System.out.println("TABLE TYPE: " + tableType);
                    String tableName = tables.getString("TABLE_NAME");
                    TableEntity tableEntity = new TableEntity(tableName, savedCatalogEntity);
                    TableEntity savedTableEntity = tableRepository.save(tableEntity);

                    System.out.println("TABLE NAME IS: " + tableName);
                    System.out.println("---------------INDEXES------------------");
                    ResultSet indexes = metaData.getIndexInfo(catalog, tableSchema, tableName, false, false);
                    while (indexes.next()) {
                        String indexName = indexes.getString("INDEX_NAME");
                        String indexType = indexes.getString("TYPE");
                        String indexColumn = indexes.getString("COLUMN_NAME");

                        IndexEntity indexEntity = new IndexEntity(indexName, indexColumn, savedTableEntity);
                        indexRepository.save(indexEntity);

                        System.out.println("INDEX_NAME: " + indexName);
                        System.out.println("INDEX_TYPE: " + indexType);
                        System.out.println("INDEX_COLUMN_NAME: " + indexColumn);
                    }

                    System.out.println("################## Maybe PRIMARY KEYS ARE: ##################");
                    ArrayList<String> primaryKeyColumns = new ArrayList<>();
                    ResultSet primaryKeys = metaData.getPrimaryKeys(tableCat, tableSchema, tableName);
                    while (primaryKeys.next()) {
                        String primaryKeyColumnName = primaryKeys.getString("COLUMN_NAME");
                        String primaryKeyName = primaryKeys.getString("PK_NAME");

                        primaryKeyColumns.add(primaryKeyColumnName);
                        ConstraintEntity constraintEntity = new ConstraintEntity(
                                primaryKeyName,
                                primaryKeyColumnName,
                                "PK",
                                null,
                                null,
                                savedTableEntity
                        );
                        constraintRepository.save(constraintEntity);
                    }

                    System.out.println("$$$$$$$$$$$$$$$ FOREIGN KEYS are: ... $$$$$$$$$$$$$$$");
                    ArrayList<String> foreignKeyColumns = new ArrayList<>();
                    ResultSet foreignKeys = metaData.getImportedKeys(tableCat, tableSchema, tableName);
                    while (foreignKeys.next()) {
                        String pkTableName = foreignKeys.getString("PKTABLE_NAME");
                        String fkTableName = foreignKeys.getString("FKTABLE_NAME");
                        String pkColumnName = foreignKeys.getString("PKCOLUMN_NAME");
                        String fkColumnName = foreignKeys.getString("FKCOLUMN_NAME");
                        String pkName = foreignKeys.getString("PK_NAME");
                        String fkName = foreignKeys.getString("FK_NAME");

                        foreignKeyColumns.add(fkColumnName);
                        ConstraintEntity constraintEntity = new ConstraintEntity(
                                fkName,
                                fkColumnName,
                                "FK",
                                pkTableName,
                                pkColumnName,
                                savedTableEntity
                        );
                        constraintRepository.save(constraintEntity);

//                        System.out.println("PK TABLE NAME: " + pkTableName);
//                        System.out.println("FK TABLE NAME: " + fkTableName);
//                        System.out.println("PK COLUMN NAME: " + pkColumnName);
//                        System.out.println("FK COLUMN NAME: " + fkColumnName);
//                        System.out.println("PK NAME: " + pkName);
//                        System.out.println("FK NAME: " + fkName);
                    }

                    ResultSet columns = metaData.getColumns(tableCat, null, tableName, null);
                    while (columns.next()) {
                        String columnName = columns.getString("COLUMN_NAME");
                        String columnDataType = columns.getString("DATA_TYPE");
                        String columnTypeName = columns.getString("TYPE_NAME");
                        String columnSize = columns.getString("COLUMN_SIZE");
                        String nullable = columns.getString("NULLABLE");
                        String autoIncrement = columns.getString("IS_AUTOINCREMENT");
                        String columnDefault = columns.getString("COLUMN_DEF");
                        if (Objects.equals(columnDefault, "NULL")) columnDefault = null;

                        String keyType = null;
                        if (primaryKeyColumns.contains(columnName)) keyType = "PK";
                        if (foreignKeyColumns.contains(columnName)) keyType = "FK";

                        ColumnEntity columnEntity = new ColumnEntity(
                                columnName,
                                columnTypeName,
                                Integer.parseInt(columnSize),
                                Boolean.parseBoolean(nullable),
                                Boolean.parseBoolean(autoIncrement),
                                columnDefault,
                                keyType,
                                savedTableEntity
                        );
                        columnRepository.save(columnEntity);


//                        System.out.println("+---------------------------------+");
//                        System.out.println("COLUMN_NAME: " + columnName);
//                        System.out.println("DATA_TYPE: " + columnDataType);
//                        System.out.println("TYPE_NAME: " + columnTypeName);
//                        System.out.println("COLUMN_SIZE: " + columnSize);
//                        System.out.println("NULLABLE: " + nullable);
//                        System.out.println("IS_NULLABLE: " + columns.getString("IS_NULLABLE"));
//                        System.out.println("COLUMN_DEF: " + columnDefault);
//                        System.out.println("SOURCE_DATA_TYPE: " + columns.getString("SOURCE_DATA_TYPE"));
//                        System.out.println("IS_AUTOINCREMENT: " + autoIncrement);
//                        System.out.println("+---------------------------------+");
                    }


                    System.out.println("+++++++++++++++++++++++++++++++");
                }
            }

            //Retrieving the data
//            if (Objects.equals(database, "PostgreSQL")) {
//                rs = stmt.executeQuery("\\l");
//            } else if (Objects.equals(database, "MySQL")) {
//                rs = stmt.executeQuery("Show Databases");
//            }
//            System.out.println("List of databases: ");
//            while(rs.next()) {
//                System.out.print(rs.getString(1));
//                System.out.println();
//            }

            System.out.println("Connection Closed ....");

            return savedReportEntity.getId();
        }
    }

    private ReportEntity saveDatabaseEntity(DatabaseMetaData metaData, String url, String username) throws SQLException {
        // saving database scraping calls
        String urlString = metaData.getURL();
        ReportEntity reportEntity = new ReportEntity(urlString);
        return reportRepository.save(reportEntity);
    }
}
