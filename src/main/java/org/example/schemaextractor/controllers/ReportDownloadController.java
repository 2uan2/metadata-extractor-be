package org.example.schemaextractor.controllers;

import org.example.schemaextractor.services.ReportDownloadService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportDownloadController {

    private final ReportDownloadService reportDownloadService;

    public ReportDownloadController(ReportDownloadService reportDownloadService) {
        this.reportDownloadService = reportDownloadService;
    }

    @GetMapping("/api/reports/{id}/download/docx")
    public ResponseEntity<?> downloadDocxReport(@PathVariable("id") Long id) {
        byte[] reportBytes = reportDownloadService.createDocxReportFile(id);
        String filename = "document.docx";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
                .body(new ByteArrayResource(reportBytes));
    }

    @GetMapping("/api/reports/{id}/download/pdf")
    public ResponseEntity<?> downloadPdfReport(@PathVariable("id") Long id) {
        byte[] reportBytes = reportDownloadService.createPdfReportFile(id);
        String filename = "document.pdf";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(new ByteArrayResource(reportBytes));
    }

}
