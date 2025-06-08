package org.example.schemaextractor.mapper;

import org.example.schemaextractor.domain.dto.ColumnDto;
import org.example.schemaextractor.domain.entities.ColumnEntity;
import org.example.schemaextractor.domain.entities.TableEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ColumnMapper {

    public ColumnDto toDto(ColumnEntity columnEntity) {
        return new ColumnDto(
                columnEntity.getId(),
                columnEntity.getFieldName(),
                columnEntity.getDataType(),
                columnEntity.getDataLength(),
                columnEntity.getNullable(),
                columnEntity.getAutoIncrement(),
                columnEntity.getKeyType(),
                columnEntity.getDefaultValue(),
                columnEntity.getDescription(),
                Optional.ofNullable(columnEntity.getTable()).map(TableEntity::getId).orElse(null),
                (columnEntity.getTable() != null) ? columnEntity.getTable().getName() : null
        );
    }

    public ColumnEntity toEntity(ColumnDto columnDto) {
        return new ColumnEntity(
                columnDto.getId(),
                columnDto.getFieldName(),
                columnDto.getDataType(),
                columnDto.getDataLength(),
                columnDto.getNullable(),
                columnDto.getAutoIncrement(),
                columnDto.getKeyType(),
                columnDto.getDefaultValue(),
                columnDto.getDescription(),
                null
        );
    }
//
//    public ColumnDto toSimpleDto(ColumnEntity columnEntity) {
//        return new ColumnDto(
//                columnEntity.getId(),
//                columnEntity.getFieldName(),
//                columnEntity.getDataType(),
//                columnEntity.getDataLength(),
//                columnEntity.getNullable(),
//                columnEntity.getAutoIncrement(),
//                columnEntity.getKeyType(),
//                columnEntity.getDefaultValue(),
//                columnEntity.getDescription()
//        );
//    }
}
