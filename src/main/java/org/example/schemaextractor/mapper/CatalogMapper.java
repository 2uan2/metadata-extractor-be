package org.example.schemaextractor.mapper;

import org.example.schemaextractor.domain.dto.CatalogDto;
import org.example.schemaextractor.domain.entities.CatalogEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
public class CatalogMapper {
    TableMapper tableMapper;

    public CatalogMapper(TableMapper tableMapper) {
        this.tableMapper = tableMapper;
    }

    public CatalogEntity toEntity(CatalogDto catalogDto) {
        return new CatalogEntity(
                catalogDto.getId(),
                catalogDto.getName(),
                null,
                Optional.ofNullable(catalogDto.getTables())
                        .orElse(Collections.emptyList())
                        .stream().map(tableMapper::toEntity).toList()
        );
    }

    public CatalogDto toDto(CatalogEntity catalogEntity) {
        return new CatalogDto(
                catalogEntity.getId(),
                catalogEntity.getName(),
                catalogEntity.getReport().getId(),
                Optional.ofNullable(catalogEntity.getTables())
                        .orElse(Collections.emptyList())
                        .stream().map(tableMapper::toDto).toList()
        );
    }
}
