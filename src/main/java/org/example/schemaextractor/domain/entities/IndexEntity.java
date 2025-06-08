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
@Table(name = "indexes")
public class IndexEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String referencedColumnName;

    @ManyToOne(optional = false)
    @JoinColumn(name = "table_id")
    private TableEntity table;

    public IndexEntity(String name, String referencedColumnName, TableEntity table) {
        this.name = name;
        this.referencedColumnName = referencedColumnName;
        this.table = table;
    }
}
