package org.example.schemaextractor.exceptions;

public class IndexNotFoundException extends RuntimeException {
    public IndexNotFoundException(String message) {
        super(message);
    }
}
