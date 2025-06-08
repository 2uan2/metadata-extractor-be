package org.example.schemaextractor.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConstraintDto {
    private Long id;

    private String keyName;

    private String columnName;

    private String keyType;

    private String referencedTableName;
    private String referencedColumnName;

//    private TableDto table;
}
