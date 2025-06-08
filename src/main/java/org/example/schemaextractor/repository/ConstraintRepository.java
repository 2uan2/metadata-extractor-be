package org.example.schemaextractor.repository;

import org.example.schemaextractor.domain.entities.ConstraintEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface ConstraintRepository extends CrudRepository<ConstraintEntity, Long> {
    List<ConstraintEntity> findByTable_Id(Long id);
}
