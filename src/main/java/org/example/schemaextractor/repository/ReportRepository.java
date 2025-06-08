package org.example.schemaextractor.repository;

import org.example.schemaextractor.domain.entities.ReportEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<ReportEntity, Long> {
    Page<ReportEntity> findAll(Pageable page);

    Page<ReportEntity> findByUrlContainingIgnoreCase(String url, Pageable pageable);
}
