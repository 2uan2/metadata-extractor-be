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
@Table(name = "constraints")
public class ConstraintEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String keyName;

    @Column(nullable = false)
    private String columnName;

    @Column(nullable = false)
    private String keyType;

    private String referencedTableName;
    private String referencedColumnName;

    @ManyToOne(optional = false)
    @JoinColumn(name = "table_id")
    private TableEntity table;

    public ConstraintEntity(String keyName, String columnName, String keyType, String referencedTableName, String referencedColumnName, TableEntity table) {
        this.keyName = keyName;
        this.columnName = columnName;
        this.keyType = keyType;
        this.referencedTableName = referencedTableName;
        this.referencedColumnName = referencedColumnName;
        this.table = table;
    }
}
