package org.example.schemaextractor.domain.dto;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.schemaextractor.domain.entities.TableEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ColumnDto {
    private Long id;

    private String fieldName;
    private String dataType;
    private Integer dataLength;

    private Boolean nullable = false;
    private Boolean autoIncrement = false;
    private String keyType;
    private String defaultValue;
    private String description;

    private Long tableId;
    private String tableName;
}
