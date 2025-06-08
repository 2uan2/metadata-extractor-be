package org.example.schemaextractor.mapper;

import org.example.schemaextractor.domain.dto.TableDto;
import org.example.schemaextractor.domain.entities.CatalogEntity;
import org.example.schemaextractor.domain.entities.TableEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
public class TableMapper {
    ColumnMapper columnMapper;
    ConstraintMapper constraintMapper;
    IndexMapper indexMapper;

    public TableMapper(ColumnMapper columnMapper, ConstraintMapper constraintMapper, IndexMapper indexMapper) {
        this.columnMapper = columnMapper;
        this.constraintMapper = constraintMapper;
        this.indexMapper = indexMapper;
    }

    public TableDto toDto(TableEntity tableEntity) {
        return new TableDto(
                tableEntity.getId(),
                tableEntity.getName(),
                tableEntity.getDescription(),
                Optional.ofNullable(tableEntity.getColumns())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(columnEntity -> columnMapper.toDto(columnEntity))
                        .toList(),
                Optional.ofNullable(tableEntity.getConstraints())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(constraintEntity -> constraintMapper.toDto(constraintEntity))
                        .toList(),
                Optional.ofNullable(tableEntity.getIndexes())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(indexEntity -> indexMapper.toDto(indexEntity))
                        .toList(),
                Optional.ofNullable(tableEntity.getCatalog()).map(CatalogEntity::getId).orElse(null)
        );
    }

    public TableEntity toEntity(TableDto tableDto) {
        return new TableEntity(
                tableDto.getId(),
                tableDto.getName(),
                tableDto.getDescription(),
                Optional.ofNullable(tableDto.getColumns())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(columnDto -> columnMapper.toEntity(columnDto))
                        .toList(),
                Optional.ofNullable(tableDto.getConstraints())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(constraintDto -> constraintMapper.toEntity(constraintDto))
                        .toList(),
                Optional.ofNullable(tableDto.getIndexes())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(indexDto -> indexMapper.toEntity(indexDto))
                        .toList(),
                null
        );
    }

//    public TableDto toSimpleDto(TableEntity tableEntity) {
//        return new TableDto(
//                tableEntity.getId(),
//                tableEntity.getName(),
//                tableEntity.getDescription(),
//                null,
//                null,
//                null
//        );
//    }
}
