package org.example.schemaextractor.mapper;

import org.example.schemaextractor.domain.dto.IndexDto;
import org.example.schemaextractor.domain.entities.IndexEntity;
import org.springframework.stereotype.Component;

@Component
public class IndexMapper {

    public IndexDto toDto(IndexEntity indexEntity) {
        return new IndexDto(
                indexEntity.getId(),
                indexEntity.getName(),
                indexEntity.getReferencedColumnName()
        );
    }

    public IndexEntity toEntity(IndexDto indexDto) {
        return new IndexEntity(
                indexDto.getId(),
                indexDto.getName(),
                indexDto.getReferencedColumnName(),
                null
        );
    }

}
