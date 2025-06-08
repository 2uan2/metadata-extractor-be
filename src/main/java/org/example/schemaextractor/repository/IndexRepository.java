package org.example.schemaextractor.repository;

import org.example.schemaextractor.domain.entities.IndexEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IndexRepository extends CrudRepository<IndexEntity, Long> {
    List<IndexEntity> findByTable_Id(Long id);
}
