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
public class IndexDto {
    private Long id;

    private String name;

    private String referencedColumnName;

//    private TableDto table;
}
