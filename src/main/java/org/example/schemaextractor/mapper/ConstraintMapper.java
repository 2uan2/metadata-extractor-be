package org.example.schemaextractor.mapper;

import org.example.schemaextractor.domain.dto.ConstraintDto;
import org.example.schemaextractor.domain.entities.ConstraintEntity;
import org.springframework.stereotype.Component;

@Component
public class ConstraintMapper {

    public ConstraintDto toDto(ConstraintEntity constraintEntity) {
        return new ConstraintDto(
                constraintEntity.getId(),
                constraintEntity.getKeyName(),
                constraintEntity.getColumnName(),
                constraintEntity.getKeyType(),
                constraintEntity.getReferencedTableName(),
                constraintEntity.getReferencedColumnName()
        );
    }

    public ConstraintEntity toEntity(ConstraintDto constraintDto) {
        return new ConstraintEntity(
                constraintDto.getId(),
                constraintDto.getKeyName(),
                constraintDto.getColumnName(),
                constraintDto.getKeyType(),
                constraintDto.getReferencedTableName(),
                constraintDto.getReferencedColumnName(),
                null
        );
    }

}
