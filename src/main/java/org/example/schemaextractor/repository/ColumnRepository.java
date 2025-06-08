package org.example.schemaextractor.repository;

import org.example.schemaextractor.domain.entities.ColumnEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ColumnRepository extends JpaRepository<ColumnEntity, Long> {
    List<ColumnEntity> findByTable_Id(Long tableId);
}
