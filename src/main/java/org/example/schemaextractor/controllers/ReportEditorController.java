package org.example.schemaextractor.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Report Editor", description = "APIs for editing report")
public class ReportEditorController {

    private final ConstraintMapper constraintMapper;
    private final ReportEditorService reportEditorService;
    private final TableMapper tableMapper;
    private final ColumnMapper columnMapper;
    private final IndexMapper indexMapper;

    public ReportEditorController(ConstraintMapper constraintMapper, ReportEditorService reportEditorService, TableMapper tableMapper, ReportMapper reportMapper, ColumnMapper columnMapper, IndexMapper indexMapper, CatalogMapper catalogMapper) {
        this.constraintMapper = constraintMapper;
        this.reportEditorService = reportEditorService;
        this.tableMapper = tableMapper;
        this.columnMapper = columnMapper;
        this.indexMapper = indexMapper;
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
