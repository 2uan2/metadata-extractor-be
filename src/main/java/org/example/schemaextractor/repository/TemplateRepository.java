package org.example.schemaextractor.repository;

import org.example.schemaextractor.domain.entities.TemplateEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface TemplateRepository extends JpaRepository<TemplateEntity, String> {
    Page<TemplateEntity> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description, Pageable pageable);

    Page<TemplateEntity> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrIdContainingIgnoreCase(String name, String description, String id, Pageable pageable);
}
