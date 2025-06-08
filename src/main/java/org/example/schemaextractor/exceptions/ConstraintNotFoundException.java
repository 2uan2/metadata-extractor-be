package org.example.schemaextractor.exceptions;

public class ConstraintNotFoundException extends RuntimeException {
    public ConstraintNotFoundException(String message) {
        super(message);
    }
}
