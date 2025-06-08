package org.example.schemaextractor.services.impl;

import org.example.schemaextractor.domain.entities.TemplateEntity;
import org.example.schemaextractor.exceptions.NoTemplateIdException;
import org.example.schemaextractor.exceptions.TemplateNotFoundException;
import org.example.schemaextractor.repository.TemplateRepository;
import org.example.schemaextractor.services.TemplateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TemplateServiceImpl implements TemplateService {
    TemplateRepository templateRepository;

    public TemplateServiceImpl(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    @Override
    public Page<TemplateEntity> getTemplatePage(String query, Pageable page) {
        if (query == null) return templateRepository.findAll(page);
        return templateRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrIdContainingIgnoreCase(query, query, query, page);
    }

    @Override
    public List<TemplateEntity> getAllTemplates() {
        return templateRepository.findAll();
    }

    @Override
    public TemplateEntity getTemplate(String templateId) {
        Optional<TemplateEntity> template = templateRepository.findById(templateId);
        if (template.isEmpty()) throw new TemplateNotFoundException("Can't find template with id " + templateId);
        return template.get();
    }

    @Override
    public TemplateEntity createTemplate(TemplateEntity templateEntity) {
        if (templateEntity.getId() == null)  throw new NoTemplateIdException("No id have been provided for the template");
        return templateRepository.save(templateEntity);
    }

    @Override
    public TemplateEntity updateTemplate(String templateId, TemplateEntity templateEntity) {
        templateEntity.setId(templateId);
        return templateRepository.save(templateEntity);
    }

    @Override
    public void deleteTemplate(String templateId) {
        templateRepository.deleteById(templateId);
    }
}
