package org.example.schemaextractor.services;

import java.sql.SQLException;

public interface DatabaseConnectionService {
    Long connect(String url, String username, String password) throws SQLException;
}
