package org.example.schemaextractor.domain.dto;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.schemaextractor.domain.entities.CatalogEntity;
import org.example.schemaextractor.domain.entities.ColumnEntity;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableDto {
    private Long id;

    private String name;
    private String description;

    private List<ColumnDto> columns;
    private List<ConstraintDto> constraints;
    private List<IndexDto> indexes;

    private Long catalogId;
}
