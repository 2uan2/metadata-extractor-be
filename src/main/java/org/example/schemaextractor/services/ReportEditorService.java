package org.example.schemaextractor.services;

import org.example.schemaextractor.domain.entities.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReportEditorService {
    List<TableEntity> getReportTables(Long id);

    List<ColumnEntity> getReportColumns(Long id);

    List<ConstraintEntity> getReportConstraints(Long id);

    List<IndexEntity> getReportIndexes(Long id);

    List<TableEntity> partialUpdate(Long id, List<TableEntity> tableEntities);

    List<ReportEntity> getAllReports();

    void deleteReport(Long id);

    List<TableEntity> update(Long id, List<TableEntity> tableEntities);

    ColumnEntity createColumn(ColumnEntity columnEntity, Long tableId);

    void deleteColumn(Long columnId);

    ColumnEntity updateColumn(ColumnEntity columnEntity, Long tableId, Long columnId);

    ConstraintEntity createConstraint(ConstraintEntity constraintEntity, Long tableId);

    void deleteConstraint(Long constraintId);

    IndexEntity createIndex(IndexEntity indexEntity, Long tableId);

    void deleteIndex(Long indexId);

    Page<ReportEntity> getReportPage(String url, Pageable page);

    Page<ColumnEntity> getColumnPage(Pageable page);

    Page<CatalogEntity> getCatalogPage(String name, Pageable page);

    List<TableEntity> getCatalogTables(Long catalogId);

    void deleteCatalog(Long catalogId);

    Page<TableEntity> getTablePage(String name, String description, Pageable page);

    void deleteTable(Long tableId);

    TableEntity getTable(Long tableId);

    CatalogEntity createCatalog(Long reportId, CatalogEntity catalogEntity);

    TableEntity createTable(Long catalogId, TableEntity tableEntity);

    CatalogEntity getCatalog(Long catalogId);

    ReportEntity getReport(Long reportId);

    ReportEntity updateReport(Long reportId, ReportEntity reportEntity);

    TableEntity updateTable(Long catalogId, Long tableId, TableEntity tableEntity);

    CatalogEntity updateCatalog(Long reportId, Long catalogId, CatalogEntity catalogEntity);

    ConstraintEntity updateConstraint(ConstraintEntity constraintEntity, Long tableId, Long constraintId);

    IndexEntity updateIndex(IndexEntity indexEntity, Long tableId, Long indexId);
}
