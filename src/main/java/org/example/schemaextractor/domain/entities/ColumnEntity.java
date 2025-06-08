package org.example.schemaextractor.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "columns")
public class ColumnEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String fieldName;
    @Column(nullable = false)
    private String dataType;
    @Column(nullable = false)
    private Integer dataLength;

    private Boolean nullable = false;
    private Boolean autoIncrement = false;
    private String keyType;
    private String defaultValue;
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "table_id")
    private TableEntity table;

    public ColumnEntity(String fieldName, String dataType, Integer dataLength, Boolean nullable, Boolean autoIncrement, String defaultValue, String keyType, TableEntity table) {
        this.fieldName = fieldName;
        this.dataType = dataType;
        this.dataLength = dataLength;
        this.table = table;
        this.nullable = nullable;
        this.autoIncrement = autoIncrement;
        this.defaultValue = defaultValue;
        this.keyType = keyType;
    }
}
