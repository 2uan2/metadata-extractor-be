package org.example.schemaextractor.controllers;

import org.example.schemaextractor.domain.dto.CatalogDto;
import org.example.schemaextractor.domain.entities.CatalogEntity;
import org.example.schemaextractor.mapper.CatalogMapper;
import org.example.schemaextractor.services.ReportEditorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CatalogEntityEditor {
    CatalogMapper catalogMapper;
    ReportEditorService reportEditorService;

    public CatalogEntityEditor(CatalogMapper catalogMapper, ReportEditorService reportEditorService) {
        this.catalogMapper = catalogMapper;
        this.reportEditorService = reportEditorService;
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
}
