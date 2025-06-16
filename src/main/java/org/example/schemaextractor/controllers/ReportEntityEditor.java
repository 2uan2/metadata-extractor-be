package org.example.schemaextractor.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.example.schemaextractor.domain.dto.ReportDto;
import org.example.schemaextractor.domain.dto.TableDto;
import org.example.schemaextractor.domain.entities.ReportEntity;
import org.example.schemaextractor.domain.entities.TableEntity;
import org.example.schemaextractor.mapper.ReportMapper;
import org.example.schemaextractor.services.ReportEditorService;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReportEntityEditor {
    ReportEditorService reportEditorService;
    ReportMapper reportMapper;

    public ReportEntityEditor(ReportMapper reportMapper, ReportEditorService reportEditorService) {
        this.reportEditorService = reportEditorService;
        this.reportMapper = reportMapper;
    }

    @Operation(summary = "Get all reports", description = "Retrieve a page of all reports based on page and size parameter")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved page",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReportEntity.class)))
    @ApiResponse(responseCode = "500", description = "Internal server error")
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

//    @PatchMapping("/api/reports/{reportId}")
//    public ResponseEntity<?> patchReport(
//            @PathVariable("reportId") Long reportId,
//            @RequestBody List<TableDto> tableDtos
//    ) {
//        System.out.println(tableDtos);
//
//        List<TableEntity> tableEntities = tableDtos
//                .stream()
//                .map(tableMapper::toEntity)
//                .toList();
//        List<TableDto> updatedTableDtos = reportEditorService.partialUpdate(reportId, tableEntities)
//                .stream()
//                .map(tableMapper::toDto)
//                .toList();
//        return new ResponseEntity<>(updatedTableDtos, HttpStatus.CREATED);
//    }

    @DeleteMapping("/api/reports/{reportId}")
    public ResponseEntity<?> deleteReport(@PathVariable("reportId") Long reportId) {
        System.out.println(reportId);
        reportEditorService.deleteReport(reportId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
