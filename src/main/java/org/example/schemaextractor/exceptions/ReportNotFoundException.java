package org.example.schemaextractor.exceptions;

public class ReportNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public ReportNotFoundException(String message) {
        super(message);
    }
}
