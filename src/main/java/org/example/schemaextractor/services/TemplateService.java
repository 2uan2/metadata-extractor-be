package org.example.schemaextractor.services;

import org.example.schemaextractor.domain.entities.TemplateEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TemplateService {
    Page<TemplateEntity> getTemplatePage(String query, Pageable page);

    List<TemplateEntity> getAllTemplates();

    TemplateEntity getTemplate(String templateId);

    TemplateEntity createTemplate(TemplateEntity templateEntity);

    TemplateEntity updateTemplate(String templateId, TemplateEntity templateEntity);

    void deleteTemplate(String templateId);
}
