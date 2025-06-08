package org.example.schemaextractor.controllers;

import org.example.schemaextractor.domain.dto.*;
import org.example.schemaextractor.domain.entities.*;
import org.example.schemaextractor.mapper.*;
import org.example.schemaextractor.services.ReportEditorService;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReportEditorController {

    private final ConstraintMapper constraintMapper;
    private final ReportEditorService reportEditorService;
    private final TableMapper tableMapper;
    private final ReportMapper reportMapper;
    private final ColumnMapper columnMapper;
    private final IndexMapper indexMapper;
    private final CatalogMapper catalogMapper;

    public ReportEditorController(ConstraintMapper constraintMapper, ReportEditorService reportEditorService, TableMapper tableMapper, ReportMapper reportMapper, ColumnMapper columnMapper, IndexMapper indexMapper, CatalogMapper catalogMapper) {
        this.constraintMapper = constraintMapper;
        this.reportEditorService = reportEditorService;
        this.tableMapper = tableMapper;
        this.reportMapper = reportMapper;
        this.columnMapper = columnMapper;
        this.indexMapper = indexMapper;
        this.catalogMapper = catalogMapper;
    }

//    @GetMapping("/api/reports")
//    public ResponseEntity<List<DatabaseDto>> getAlLReports() {
//        List<DatabaseEntity> reports = reportEditorService.getAllReports();
//        List<DatabaseDto> reportDtos = reports
//                .stream()
//                .map(databaseEntity -> databaseMapper.toDto(databaseEntity))
//                .toList();
//        return new ResponseEntity<>(reportDtos, HttpStatus.OK);
//    }

    @GetMapping("/api/reports")
    public ResponseEntity<Page<ReportDto>> getReportPage(
        @RequestParam(required = false) String url,
        Pageable page
    ) {
        Pageable sortedPage =  PageRequest.of(page.getPageNumber(), page.getPageSize(), Sort.by(Sort.Direction.DESC, "createdOn"));
        Page<ReportEntity> reportEntities = reportEditorService.getReportPage(url, sortedPage);
        List<ReportDto> reportDtos = reportEntities.getContent().stream().map(reportMapper::toDto).toList();
        Page<ReportDto> databasePage = new PageImpl<>(reportDtos, reportEntities.getPageable(), reportEntities.getTotalElements());
        return new ResponseEntity<>(databasePage, HttpStatus.OK);
    }

    @GetMapping("/api/catalogs")
    public ResponseEntity<Page<CatalogDto>> getReportCatalogPage(
            @RequestParam(required = false) String name,
            Pageable page
    ) {
        Page<CatalogEntity> catalogEntities = reportEditorService.getCatalogPage(name, page);
        List<CatalogDto> catalogDtos = catalogEntities.get().map(catalogMapper::toDto).toList();
        Page<CatalogDto> catalogPage = new PageImpl<>(catalogDtos, catalogEntities.getPageable(), catalogEntities.getTotalElements());
        return new ResponseEntity<>(catalogPage, HttpStatus.OK);
    }

    @GetMapping("/api/catalogs/{catalogId}")
    public ResponseEntity<CatalogDto> getCatalog(@PathVariable("catalogId") Long catalogId) {
        CatalogEntity catalogEntity = reportEditorService.getCatalog(catalogId);
        return new ResponseEntity<>(catalogMapper.toDto(catalogEntity), HttpStatus.OK);
//        List<TableEntity> tableEntities = reportEditorService.getCatalogTables(catalogId);
//        List<TableDto> tableDtos = tableEntities.stream().map(tableMapper::toDto).toList();
//        return new ResponseEntity<>(tableDtos, HttpStatus.OK);
    }

    @PostMapping("/api/reports/{reportId}/catalogs")
    public ResponseEntity<CatalogDto> createCatalog(
            @PathVariable("reportId") Long reportId,
            @RequestBody CatalogDto catalogDto
    ) {
        CatalogEntity catalogEntity = catalogMapper.toEntity(catalogDto);
        CatalogEntity savedEntity = reportEditorService.createCatalog(reportId, catalogEntity);
        CatalogDto savedDto = catalogMapper.toDto(savedEntity);
        return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
    }

    @PutMapping("/api/reports/{reportId}/catalogs/{catalogId}")
    public ResponseEntity<CatalogDto> updateCatalog(
            @PathVariable("reportId") Long reportId,
            @PathVariable("catalogId") Long catalogId,
            @RequestBody CatalogDto catalogDto
    ) {
        CatalogEntity catalogEntity = catalogMapper.toEntity(catalogDto);
        CatalogEntity updatedEntity = reportEditorService.updateCatalog(reportId, catalogId, catalogEntity);
        return new ResponseEntity<>(catalogMapper.toDto(updatedEntity), HttpStatus.OK);
    }

    @DeleteMapping("/api/catalogs/{catalogId}")
    public ResponseEntity<?> deleteCatalog(@PathVariable("catalogId") Long catalogId) {
        reportEditorService.deleteCatalog(catalogId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<Page<TableDto>> getTablePage(
            @RequestParam(required = false) String query,
//            @RequestParam(required = false) String description,
            Pageable page
    ) {
        Page<TableEntity> tableEntities = reportEditorService.getTablePage(query, query, page);
        List<TableDto> tableDtos = tableEntities.getContent().stream().map(tableMapper::toDto).toList();
        Page<TableDto> tablePage = new PageImpl<>(tableDtos, tableEntities.getPageable(), tableEntities.getTotalElements());
        return new ResponseEntity<>(tablePage, HttpStatus.OK);
    }

    @GetMapping("/api/tables/{tableId}")
    public ResponseEntity<TableDto> getTable(
            @PathVariable("tableId") Long tableId
    ) {
        TableEntity tableEntity = reportEditorService.getTable(tableId);
        TableDto tableDto = tableMapper.toDto(tableEntity);
        return new ResponseEntity<>(tableDto, HttpStatus.OK);
    }

    @PostMapping("/api/catalogs/{catalogId}/tables")
    public ResponseEntity<TableDto> createTable(
            @PathVariable("catalogId") Long catalogId,
            @RequestBody TableDto tableDto
    ) {
        System.out.println(tableDto);
        TableEntity tableEntity = tableMapper.toEntity(tableDto);
        TableEntity createdTable = reportEditorService.createTable(catalogId, tableEntity);
        TableDto createdDto = tableMapper.toDto(createdTable);
        return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
    }

    @PutMapping("/api/catalogs/{catalogId}/tables/{tableId}")
    public ResponseEntity<TableDto> updateTable(
            @PathVariable("catalogId") Long catalogId,
            @PathVariable("tableId") Long tableId,
            @RequestBody TableDto tableDto
    ) {
        TableEntity tableEntity = tableMapper.toEntity(tableDto);
        TableEntity updatedEntity = reportEditorService.updateTable(catalogId, tableId, tableEntity);
        return new ResponseEntity<>(tableMapper.toDto(updatedEntity), HttpStatus.OK);
    }

    @DeleteMapping("/api/tables/{tableId}")
    public ResponseEntity<?> deleteTable(@PathVariable("tableId") Long tableId) {
        reportEditorService.deleteTable(tableId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/api/reports/{reportId}/old")
    public ResponseEntity<List<TableDto>> getReportOld(@PathVariable("reportId") Long reportId) {
//        ReportEntity report = reportEditorService.getReport(reportId);
//        return new ResponseEntity<>(reportMapper.toDto(report), HttpStatus.OK);
        List<TableEntity> tableEntities = reportEditorService.getReportTables(reportId);
        List<TableDto> tableDtos = tableEntities
                .stream()
                .map(tableMapper::toDto)
                .toList();
        return new ResponseEntity<>(tableDtos, HttpStatus.OK);
    }

    @GetMapping("/api/reports/{reportId}")
    public ResponseEntity<ReportDto> getReport(@PathVariable("reportId") Long reportId) {
        ReportEntity reportEntity = reportEditorService.getReport(reportId);
        return new ResponseEntity<>(reportMapper.toDto(reportEntity), HttpStatus.OK);
    }

    @PutMapping("/api/reports/{reportId}")
    public ResponseEntity<ReportDto> updateReport(
            @PathVariable("reportId") Long reportId,
            @RequestBody ReportDto reportDto
    ) {
        ReportEntity reportEntity = reportMapper.toEntity(reportDto);
        ReportEntity updatedEntity = reportEditorService.updateReport(reportId, reportEntity);
        return new ResponseEntity<>(reportMapper.toDto(updatedEntity), HttpStatus.OK);
    }

//    @PutMapping("/report/{id}")
//    public ResponseEntity<?> putReport(
//            @PathVariable("id") Long id,
//            @RequestBody List<TableDto> tableDtos
//    ) {
//        List<TableEntity> tableEntities = tableDtos
//                .stream()
//                .map(tableDto -> tableMapper.toEntity(tableDto))
//                .toList();
//        List<TableEntity> updated = reportEditorService.update(id, tableEntities);
//    }

    @PatchMapping("/api/reports/{reportId}")
    public ResponseEntity<?> patchReport(
            @PathVariable("reportId") Long reportId,
            @RequestBody List<TableDto> tableDtos
    ) {
        System.out.println(tableDtos);

        List<TableEntity> tableEntities = tableDtos
                .stream()
                .map(tableMapper::toEntity)
                .toList();
        List<TableDto> updatedTableDtos = reportEditorService.partialUpdate(reportId, tableEntities)
                .stream()
                .map(tableMapper::toDto)
                .toList();
        return new ResponseEntity<>(updatedTableDtos, HttpStatus.CREATED);
    }

    @DeleteMapping("/api/reports/{reportId}")
    public ResponseEntity<?> deleteReport(@PathVariable("reportId") Long reportId) {
        System.out.println(reportId);
        reportEditorService.deleteReport(reportId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/api/columns")
    public ResponseEntity<Page<ColumnDto>> getColumns(
            Pageable page
    ) {
        Page<ColumnEntity> columnEntities = reportEditorService.getColumnPage(page);
        List<ColumnDto> columnDtos = columnEntities.get().map(columnEntity -> columnMapper.toDto(columnEntity)).toList();
        Page<ColumnDto> columnPage = new PageImpl<>(columnDtos, columnEntities.getPageable(), columnEntities.getTotalElements());
        return new ResponseEntity<>(columnPage, HttpStatus.OK);
    }

    @PostMapping("/api/tables/{tableId}/columns")
    public ResponseEntity<ColumnDto> createColumn(
            @PathVariable("tableId") Long tableId,
            @RequestBody ColumnDto columnDto
    ) {
        ColumnEntity columnEntity = columnMapper.toEntity(columnDto);
        ColumnEntity savedEntity = reportEditorService.createColumn(columnEntity, tableId);
        return new ResponseEntity<>(columnMapper.toDto(savedEntity), HttpStatus.CREATED);
    }

    @PutMapping("/api/tables/{tableId}/columns/{columnId}")
    public ResponseEntity<ColumnDto> updateColumn(
            @PathVariable("tableId") Long tableId,
            @PathVariable("columnId") Long columnId,
            @RequestBody ColumnDto columnDto
    ) {
        ColumnEntity columnEntity = columnMapper.toEntity(columnDto);
        ColumnEntity savedEntity = reportEditorService.updateColumn(columnEntity, tableId, columnId);
        return new ResponseEntity<>(columnMapper.toDto(savedEntity), HttpStatus.CREATED);
    }

    @DeleteMapping("/api/columns/{columnId}")
    public ResponseEntity<?> deleteColumn(
            @PathVariable("columnId") Long columnId
    ) {
        reportEditorService.deleteColumn(columnId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/api/tables/{tableId}/constraints")
    public ResponseEntity<ConstraintDto> createConstraint(
            @PathVariable("tableId") Long tableId,
            @RequestBody ConstraintDto constraintDto
    ) {
        ConstraintEntity constraintEntity = constraintMapper.toEntity(constraintDto);
        ConstraintEntity savedEntity = reportEditorService.createConstraint(constraintEntity, tableId);
        return new ResponseEntity<>(constraintMapper.toDto(savedEntity), HttpStatus.CREATED);
    }

    @PutMapping("/api/tables/{tableId}/constraints/{constraintId}")
    public ResponseEntity<ConstraintDto> updateConstraint(
            @PathVariable("tableId") Long tableId,
            @PathVariable("constraintId") Long constraintId,
            @RequestBody ConstraintDto constraintDto
    ) {
        ConstraintEntity constraintEntity = constraintMapper.toEntity(constraintDto);
        ConstraintEntity savedEntity = reportEditorService.updateConstraint(constraintEntity, tableId, constraintId);
        return new ResponseEntity<>(constraintMapper.toDto(savedEntity), HttpStatus.CREATED);
    }

    @DeleteMapping("/api/constraints/{constraintId}")
    public ResponseEntity<?> deleteConstraint(
            @PathVariable("constraintId") Long constraintId
    ) {
        reportEditorService.deleteConstraint(constraintId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/api/tables/{tableId}/indexes")
    public ResponseEntity<IndexDto> createIndex(
            @PathVariable("tableId") Long tableId,
            @RequestBody IndexDto indexDto
    ) {
        IndexEntity indexEntity = indexMapper.toEntity(indexDto);
        IndexEntity savedEntity = reportEditorService.createIndex(indexEntity, tableId);
        return new ResponseEntity<>(indexMapper.toDto(savedEntity), HttpStatus.CREATED);
    }

    @PutMapping("/api/tables/{tableId}/indexes/{indexId}")
    public ResponseEntity<IndexDto> updateIndex(
            @PathVariable("tableId") Long tableId,
            @PathVariable("indexId") Long indexId,
            @RequestBody IndexDto indexDto
    ) {
        IndexEntity indexEntity = indexMapper.toEntity(indexDto);
        IndexEntity savedEntity = reportEditorService.updateIndex(indexEntity, tableId, indexId);
        return new ResponseEntity<>(indexMapper.toDto(savedEntity), HttpStatus.CREATED);
    }

    @DeleteMapping("/api/indexes/{indexId}")
    public ResponseEntity<?> deleteIndex(
            @PathVariable("indexId") Long indexId
    ) {
        reportEditorService.deleteIndex(indexId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
