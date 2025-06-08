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
@Table(name = "templates")
public class TemplateEntity {
    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private String id;

//    @Column(nullable = true)
    private String name;

    private String description;
}
