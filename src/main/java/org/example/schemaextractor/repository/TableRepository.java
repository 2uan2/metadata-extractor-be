package org.example.schemaextractor.repository;

import org.example.schemaextractor.domain.entities.TableEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TableRepository extends JpaRepository<TableEntity, Long> {
//    List<TableEntity> getTableEntitiesByCatalog_Id(Long catalogId);

    List<TableEntity> findByCatalog_Id(Long catalogId);

    Page<TableEntity> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description, Pageable page);
}
