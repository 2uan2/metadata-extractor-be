package org.example.schemaextractor.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatalogDto {
    private Long id;

    private String name;

    private Long reportId;
    private List<TableDto> tables;
}
