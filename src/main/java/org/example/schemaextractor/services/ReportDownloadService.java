package org.example.schemaextractor.services;

public interface ReportDownloadService {
    byte[] createDocxReportFile(Long id);

    byte[] createPdfReportFile(Long id);
}
