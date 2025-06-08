package org.example.schemaextractor.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDto {

    private Long id;

    private String url;

    private Instant createdOn;

    private List<CatalogDto> catalogs;
}
