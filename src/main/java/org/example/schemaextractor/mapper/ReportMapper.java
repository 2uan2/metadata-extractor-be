package org.example.schemaextractor.mapper;

import org.example.schemaextractor.domain.dto.ReportDto;
import org.example.schemaextractor.domain.entities.ReportEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
public class ReportMapper {
    CatalogMapper catalogMapper;

    public ReportMapper(CatalogMapper catalogMapper) {
        this.catalogMapper = catalogMapper;
    }

    public ReportDto toDto(ReportEntity reportEntity) {
        return new ReportDto(
                reportEntity.getId(),
                reportEntity.getUrl(),
                reportEntity.getCreatedOn(),
                Optional.ofNullable(reportEntity.getCatalogs())
                        .orElse(Collections.emptyList())
                        .stream().map(catalogMapper::toDto)
                        .toList()
        );
    }

    public ReportEntity toEntity(ReportDto reportDto) {
        return new ReportEntity(
                reportDto.getId(),
                reportDto.getUrl(),
                reportDto.getCreatedOn(),
                Optional.ofNullable(reportDto.getCatalogs())
                       .orElse(Collections.emptyList())
                       .stream().map(catalogMapper::toEntity)
                       .toList()
        );
    }
}
