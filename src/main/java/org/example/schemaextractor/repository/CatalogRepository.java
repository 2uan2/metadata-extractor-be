package org.example.schemaextractor.repository;

import org.example.schemaextractor.domain.entities.CatalogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CatalogRepository extends JpaRepository<CatalogEntity, Long> {
//    CatalogEntity[] getCatalogEntitiesUsingDatabaseId(Long id);

//    CatalogEntity[] getCatalogEntitiesUsingDatabase_Id(Long id);

//    List<CatalogEntity> getCatalogEntitiesUsingDatabaseId(Long id);

    List<CatalogEntity> findByReport_Id(Long databaseId);

    Page<CatalogEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
