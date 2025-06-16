package org.example.schemaextractor.controllers;

import org.example.schemaextractor.domain.dto.TableDto;
import org.example.schemaextractor.domain.entities.TableEntity;
import org.example.schemaextractor.mapper.TableMapper;
import org.example.schemaextractor.services.ReportEditorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TableEntityController {

    ReportEditorService reportEditorService;
    TableMapper tableMapper;

    public TableEntityController(ReportEditorService reportEditorService, TableMapper tableMapper) {
        this.reportEditorService = reportEditorService;
        this.tableMapper = tableMapper;
    }

    @GetMapping("/api/tables")
    public ResponseEntity<Page<TableDto>> getTablePage(
            @RequestParam(required = false) String query,
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
}
