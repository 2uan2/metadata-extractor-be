package org.example.schemaextractor.controllers;

import lombok.extern.slf4j.Slf4j;
import org.example.schemaextractor.domain.dto.ConnectionRequestDto;
import org.example.schemaextractor.domain.dto.ConnectionResponseDto;
import org.example.schemaextractor.services.DatabaseConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.*;

@Slf4j
@Controller
public class DatabaseConnectionController {

    DatabaseConnectionService databaseConnectionService;

    public DatabaseConnectionController(DatabaseConnectionService databaseConnectionService) {
        this.databaseConnectionService = databaseConnectionService;
    }

    @PostMapping("/api/connect")
    public ResponseEntity<ConnectionResponseDto> connect(@RequestBody ConnectionRequestDto connectionRequest) {
        try {
            Long id = databaseConnectionService.connect(
                    connectionRequest.getUrl(),
                    connectionRequest.getUsername(),
                    connectionRequest.getPassword());

            return new ResponseEntity<>(new ConnectionResponseDto(id), HttpStatus.CREATED);
        } catch (SQLException e) {
            log.error("Error connecting to database: ", e);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
