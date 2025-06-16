package org.example.schemaextractor.services.impl;

import jakarta.transaction.Transactional;
import org.apache.commons.collections4.IteratorUtils;
import org.example.schemaextractor.domain.entities.*;
import org.example.schemaextractor.exceptions.*;
import org.example.schemaextractor.repository.*;
import org.example.schemaextractor.services.ReportEditorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReportEditorServiceImpl implements ReportEditorService {

    private final ReportRepository reportRepository;
    private final CatalogRepository catalogRepository;
    private final TableRepository tableRepository;
    private final ColumnRepository columnRepository;
    private final ConstraintRepository constraintRepository;
    private final IndexRepository indexRepository;

    public ReportEditorServiceImpl(
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
    public Page<ReportEntity> getReportPage(String url, Pageable page) {
        if (url != null)
            return reportRepository.findByUrlContainingIgnoreCase(url, page);
        else
            return reportRepository.findAll(page);
    }

    @Override
    public Page<ColumnEntity> getColumnPage(Pageable page) {
        return columnRepository.findAll(page);
    }

    @Override
    public Page<CatalogEntity> getCatalogPage(String name, Pageable page) {
        if (name != null)
            return catalogRepository.findByNameContainingIgnoreCase(name, page);
        else
            return catalogRepository.findAll(page);
    }

    @Override
    public List<TableEntity> getCatalogTables(Long catalogId) {
        Optional<CatalogEntity> catalog = catalogRepository.findById(catalogId);
        if (catalog.isEmpty()) throw new CatalogNotFoundException("Can't find catalog with id " + catalogId);
        return tableRepository.findByCatalog_Id(catalogId);
    }

    @Override
    public void deleteCatalog(Long catalogId) {
        catalogRepository.deleteById(catalogId);
    }

    @Override
    public Page<TableEntity> getTablePage(String name, String description, Pageable page) {
        if (name != null || description != null)
            return tableRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(name, description, page);
        else
            return tableRepository.findAll(page);
    }

    @Override
    public void deleteTable(Long tableId) {
        tableRepository.deleteById(tableId);
    }

    @Override
    public TableEntity getTable(Long tableId) {
        Optional<TableEntity> table = tableRepository.findById(tableId);
        if (table.isEmpty()) throw new TableNotFoundException("Can't find table with id " + tableId);
        return table.get();
    }

    @Override
    public CatalogEntity createCatalog(Long reportId, CatalogEntity catalogEntity) {
        Optional<ReportEntity> report = reportRepository.findById(reportId);
        if (report.isEmpty())
            throw new ReportNotFoundException("Can't find report with id " + reportId + " to create catalog");
        catalogEntity.setReport(report.get());
        catalogEntity.setId(null);
        catalogEntity.setTables(null);
        return catalogRepository.save(catalogEntity);
    }

    @Override
    public TableEntity createTable(Long catalogId, TableEntity tableEntity) {
        Optional<CatalogEntity> catalog = catalogRepository.findById(catalogId);
        if (catalog.isEmpty()) throw new CatalogNotFoundException("Can't find catalog with id " + catalogId + " to create table");
        tableEntity.setCatalog(catalog.get());
        tableEntity.setId(null);
        tableEntity.setConstraints(List.of());
        tableEntity.setColumns(null);
        tableEntity.setIndexes(null);
        return tableRepository.save(tableEntity);
    }

    @Override
    public CatalogEntity getCatalog(Long catalogId) {
        Optional<CatalogEntity> catalog = catalogRepository.findById(catalogId);
        if (catalog.isEmpty()) throw new CatalogNotFoundException("Can't find catalog with id " + catalogId);
        return catalog.get();
    }

    @Override
    public ReportEntity getReport(Long reportId) {
        Optional<ReportEntity> report = reportRepository.findById(reportId);
        if (report.isEmpty()) throw new ReportNotFoundException("Can't find report with id " + reportId);
        return report.get();
    }

    @Override
    public ReportEntity updateReport(Long reportId, ReportEntity reportEntity) {
        Optional<ReportEntity> report = reportRepository.findById(reportId);
        if (report.isEmpty()) throw new ReportNotFoundException("Can't find report with id " + reportId);
        reportEntity.setId(reportId);
        reportEntity.setCatalogs(report.get().getCatalogs());
        return reportRepository.save(reportEntity);
    }

    @Override
    public TableEntity updateTable(Long catalogId, Long tableId, TableEntity tableEntity) {
        Optional<CatalogEntity> catalog = catalogRepository.findById(catalogId);
        Optional<TableEntity> table = tableRepository.findById(tableId);
        if (catalog.isEmpty()) throw new CatalogNotFoundException("Can't find catalog with id " + catalogId + " for updating");
        if (table.isEmpty()) throw new TableNotFoundException("Can't find old table with id " + catalogId + " to update");
        tableEntity.setId(tableId);
        tableEntity.setCatalog(catalog.get());
        tableEntity.setColumns(table.get().getColumns());
        tableEntity.setConstraints(table.get().getConstraints());
        tableEntity.setIndexes(table.get().getIndexes());
        return tableRepository.save(tableEntity);
    }

    @Override
    public CatalogEntity updateCatalog(Long reportId, Long catalogId, CatalogEntity catalogEntity) {
        Optional<ReportEntity> report = reportRepository.findById(reportId);
        Optional<CatalogEntity> catalog = catalogRepository.findById(catalogId);
        if (report.isEmpty()) throw new ReportNotFoundException("Can't find report with id " + reportId + " for updating");
        if (catalog.isEmpty()) throw new CatalogNotFoundException("Can't find catalog with id " + catalogId + " for updating");
        catalogEntity.setId(catalogId);
        catalogEntity.setReport(report.get());
        catalogEntity.setTables(catalog.get().getTables());
        return catalogRepository.save(catalogEntity);
    }

    @Override
    public ConstraintEntity updateConstraint(ConstraintEntity constraintEntity, Long tableId, Long constraintId) {
        Optional<TableEntity> table = tableRepository.findById(tableId);
        if (table.isEmpty()) throw new TableNotFoundException("Can't find table with id " + tableId + " for updating");
        constraintEntity.setId(constraintId);
        constraintEntity.setTable(table.get());
        if (constraintEntity.getKeyName() == null) constraintEntity.setKeyName("");
        if (constraintEntity.getColumnName() == null) constraintEntity.setColumnName("");
        if (constraintEntity.getKeyType() == null) constraintEntity.setKeyType("");
        return constraintRepository.save(constraintEntity);
    }

    @Override
    public IndexEntity updateIndex(IndexEntity indexEntity, Long tableId, Long indexId) {
        Optional<TableEntity> table = tableRepository.findById(tableId);
        if (table.isEmpty()) throw new TableNotFoundException("Can't find table with id " + tableId + " for updating");
        indexEntity.setId(indexId);
        indexEntity.setTable(table.get());
        if (indexEntity.getName() == null) indexEntity.setName("");
        if (indexEntity.getReferencedColumnName() == null) indexEntity.setReferencedColumnName("");
        return indexRepository.save(indexEntity);
    }

    @Override
    public List<TableEntity> getReportTables(Long reportId) {
        Optional<ReportEntity> database = reportRepository.findById(reportId);
        if (database.isEmpty()) throw new ReportNotFoundException("Can't find report with id " + reportId);
        List<CatalogEntity> catalogEntities = catalogRepository.findByReport_Id(reportId);
        ArrayList<TableEntity> tableEntities = new ArrayList<>();
        for (CatalogEntity catalog : catalogEntities) {
            List<TableEntity> tables = tableRepository.findByCatalog_Id(catalog.getId());
            tableEntities.addAll(tables);
        }
        return tableEntities;
    }

    @Override
    public List<ColumnEntity> getReportColumns(Long reportId) {
        List<CatalogEntity> catalogEntities = catalogRepository.findByReport_Id(reportId);
        ArrayList<ColumnEntity> columnEntities = new ArrayList<>();
        for (CatalogEntity catalog : catalogEntities) {
            List<TableEntity> tables = tableRepository.findByCatalog_Id(catalog.getId());
            for (TableEntity table : tables) {
                List<ColumnEntity> columns = columnRepository.findByTable_Id(table.getId());
                columnEntities.addAll(columns);
            }
        }
        return columnEntities;
    }

    @Override
    public List<ConstraintEntity> getReportConstraints(Long reportId) {
        List<CatalogEntity> catalogEntities = catalogRepository.findByReport_Id(reportId);
        ArrayList<ConstraintEntity> constraintEntities = new ArrayList<>();
        for (CatalogEntity catalog : catalogEntities) {
            tableRepository.findByCatalog_Id(catalog.getId())
                    .forEach(tableEntity ->
                            constraintEntities.addAll(constraintRepository.findByTable_Id(tableEntity.getId())));
        }
        return constraintEntities;
    }

    @Override
    public List<IndexEntity> getReportIndexes(Long reportId) {
        List<CatalogEntity> catalogEntities = catalogRepository.findByReport_Id(reportId);
        ArrayList<IndexEntity> indexEntities = new ArrayList<>();
        for (CatalogEntity catalog : catalogEntities) {
            tableRepository.findByCatalog_Id(catalog.getId())
                    .forEach(tableEntity ->
                            indexEntities.addAll(indexRepository.findByTable_Id(tableEntity.getId())));
        }
        return indexEntities;
    }

    @Override
    public List<TableEntity> partialUpdate(Long reportId, List<TableEntity> tableEntities) {
        List<TableEntity> currentTables = getReportTables(reportId);
        List<ColumnEntity> currentColumns = getReportColumns(reportId);
        List<ConstraintEntity> currentConstraints = getReportConstraints(reportId);
        List<IndexEntity> currentIndexes = getReportIndexes(reportId);

        // double for loop to loop through and match TableEntity in currentTable and tableEntities by id and update them
        for (TableEntity currentTableEntity : currentTables) {
            for (TableEntity tableEntity : tableEntities) {
                if (!Objects.equals(tableEntity.getId(), currentTableEntity.getId())) continue;

                // update if id are equal
                Optional.ofNullable(tableEntity.getName()).ifPresent(currentTableEntity::setName);
                Optional.ofNullable(tableEntity.getDescription()).ifPresent(currentTableEntity::setDescription);
                tableRepository.save(currentTableEntity);

                // loop for updating columns
                List<ColumnEntity> columnEntities = tableEntity.getColumns();
                for (ColumnEntity currentColumn : currentColumns) {
                    for (ColumnEntity columnEntity : columnEntities) {
                        if (!Objects.equals(currentColumn.getId(), columnEntity.getId())) continue;

                        Optional.ofNullable(columnEntity.getFieldName()).ifPresent(currentColumn::setFieldName);
                        Optional.ofNullable(columnEntity.getDataType()).ifPresent(currentColumn::setDataType);
                        Optional.ofNullable(columnEntity.getDataLength()).ifPresent(currentColumn::setDataLength);
                        Optional.ofNullable(columnEntity.getNullable()).ifPresent(currentColumn::setNullable);
                        Optional.ofNullable(columnEntity.getAutoIncrement()).ifPresent(currentColumn::setAutoIncrement);
                        Optional.ofNullable(columnEntity.getKeyType()).ifPresent(currentColumn::setKeyType);
                        Optional.ofNullable(columnEntity.getDefaultValue()).ifPresent(currentColumn::setDefaultValue);
                        Optional.ofNullable(columnEntity.getDescription()).ifPresent(currentColumn::setDescription);

                        columnRepository.save(currentColumn);
                        break;
                    }
                }

                //  loop for updating constraints
                List<ConstraintEntity> constraintEntities = tableEntity.getConstraints();
                for (ConstraintEntity currentConstraint : currentConstraints) {
                    for (ConstraintEntity constraintEntity : constraintEntities) {
                        if (!Objects.equals(currentConstraint.getId(), constraintEntity.getId())) continue;

                        Optional.ofNullable(constraintEntity.getKeyName()).ifPresent(currentConstraint::setKeyName);
                        Optional.ofNullable(constraintEntity.getColumnName()).ifPresent(currentConstraint::setColumnName);
                        Optional.ofNullable(constraintEntity.getKeyType()).ifPresent(currentConstraint::setKeyType);
                        Optional.ofNullable(constraintEntity.getReferencedTableName()).ifPresent(currentConstraint::setReferencedTableName);
                        Optional.ofNullable(constraintEntity.getReferencedColumnName()).ifPresent(currentConstraint::setReferencedColumnName);

                        constraintRepository.save(currentConstraint);
                        break;
                    }
                }

                // loop to update indexes
                List<IndexEntity> indexEntities = tableEntity.getIndexes();
                for (IndexEntity currentIndex : currentIndexes) {
                    for (IndexEntity indexEntity : indexEntities) {
                        if (!Objects.equals(currentIndex.getId(), indexEntity.getId())) continue;

                        Optional.ofNullable(indexEntity.getName()).ifPresent(currentIndex::setName);
                        Optional.ofNullable(indexEntity.getReferencedColumnName()).ifPresent(currentIndex::setReferencedColumnName);

                        indexRepository.save(currentIndex);
                        break;
                    }
                }

                break;
            }
        }
        return tableEntities;
    }

    @Override
    public List<TableEntity> update(Long id, List<TableEntity> tableEntities) {

        return List.of();
    }

    @Override
    public ColumnEntity createColumn(ColumnEntity columnEntity, Long tableId) {
        Optional<TableEntity> table = tableRepository.findById(tableId);
        if (table.isEmpty()) throw new TableNotFoundException("Can't find table with that id");
        columnEntity.setTable(table.get());
        columnEntity.setId(null);
        return columnRepository.save(columnEntity);
    }

    @Transactional
    @Override
    public void deleteColumn(Long columnId) {
        Optional<ColumnEntity> column = columnRepository.findById(columnId);
        if (column.isEmpty()) throw new ColumnNotFoundException("Can't find column with id " + columnId);
        columnRepository.deleteById(columnId);
    }

    @Override
    public ColumnEntity updateColumn(ColumnEntity columnEntity, Long tableId, Long columnId) {
        Optional<TableEntity> table = tableRepository.findById(tableId);
        if (table.isEmpty()) throw new TableNotFoundException("can't find table with id " + tableId);
        columnEntity.setTable(table.get());
        columnEntity.setId(columnId);
        if (columnEntity.getDataLength() == null) columnEntity.setDataLength(0);
        if (columnEntity.getFieldName() == null) columnEntity.setFieldName("");
        if (columnEntity.getDataType() == null) columnEntity.setDataType("");
        return columnRepository.save(columnEntity);
    }

    @Override
    public ConstraintEntity createConstraint(ConstraintEntity constraintEntity, Long tableId) {
        Optional<TableEntity> table = tableRepository.findById(tableId);
        if (table.isEmpty()) throw new TableNotFoundException("Can't find table with id " + tableId);
        constraintEntity.setTable(table.get());
        constraintEntity.setId(null);
        return constraintRepository.save(constraintEntity);
    }

    @Override
    public void deleteConstraint(Long constraintId) {
        Optional<ConstraintEntity> constraint = constraintRepository.findById(constraintId);
        if (constraint.isEmpty()) throw new ConstraintNotFoundException("Can't find constraint with id " + constraintId);
        constraintRepository.deleteById(constraintId);
    }

    @Override
    public IndexEntity createIndex(IndexEntity indexEntity, Long tableId) {
        Optional<TableEntity> table = tableRepository.findById(tableId);
        if (table.isEmpty()) throw new TableNotFoundException("Can't find table with id " + tableId);
        indexEntity.setTable(table.get());
        indexEntity.setId(null);
        return indexRepository.save(indexEntity);
    }

    @Override
    public void deleteIndex(Long indexId) {
        Optional<IndexEntity> index = indexRepository.findById(indexId);
        if (index.isEmpty()) throw new IndexNotFoundException("Can't find index with id " + indexId);
        indexRepository.deleteById(indexId);
    }

    @Override
    public List<ReportEntity> getAllReports() {
        return IteratorUtils.toList(reportRepository.findAll().iterator());
    }

    @Override
    public void deleteReport(Long reportId) {
        reportRepository.deleteById(reportId);
    }
}
