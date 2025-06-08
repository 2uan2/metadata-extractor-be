package org.example.schemaextractor.controllers;

import org.example.schemaextractor.domain.dto.TemplateDto;
import org.example.schemaextractor.domain.entities.TemplateEntity;
import org.example.schemaextractor.mapper.TemplateMapper;
import org.example.schemaextractor.services.TemplateService;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TemplateController {
    TemplateService templateService;
    TemplateMapper templateMapper;

    public TemplateController(TemplateService templateService, TemplateMapper templateMapper) {
        this.templateService = templateService;
        this.templateMapper = templateMapper;
    }

    @GetMapping("/api/templates")
    public ResponseEntity<Page<TemplateDto>> getTemplates(
            @RequestParam(required = false) String query,
            Pageable page
    ) {
        Page<TemplateEntity> templates = templateService.getTemplatePage(query, page);
        List<TemplateDto> templateDtos = templates.stream().map(templateMapper::toDto).toList();
        Page<TemplateDto> templateDtoPage = new PageImpl<>(templateDtos, templates.getPageable(), templates.getTotalElements());
        return new ResponseEntity<>(templateDtoPage, HttpStatus.OK);
    }

//    @GetMapping("/api/templates")
//    public ResponseEntity<List<TemplateDto>> getAllTemplates() {
//        List<TemplateEntity> templateEntities = templateService.getAllTemplates();
//        List<TemplateDto> templateDtos = templateEntities.stream().map(templateMapper::toDto).toList();
//        return new ResponseEntity<>(templateDtos, HttpStatus.OK);
//    }

    @PostMapping("/api/templates")
    public ResponseEntity<TemplateDto> createTemplate(@RequestBody TemplateDto templateDto) {
        TemplateEntity templateEntity = templateMapper.toEntity(templateDto);
        TemplateDto savedTemplateDto = templateMapper.toDto(templateService.createTemplate(templateEntity));
        return new ResponseEntity<>(savedTemplateDto, HttpStatus.CREATED);
    }

    @GetMapping("/api/templates/{templateId}")
    public ResponseEntity<TemplateDto> getTemplate(@PathVariable("templateId") String templateId) {
        TemplateEntity template = templateService.getTemplate(templateId);
        return new ResponseEntity<>(templateMapper.toDto(template), HttpStatus.OK);
    }

    @PutMapping("/api/templates/{templateId}")
    public ResponseEntity<TemplateDto> updateTemplate(
            @PathVariable("templateId") String templateId,
            @RequestBody TemplateDto templateDto
    ) {
        TemplateEntity templateEntity = templateMapper.toEntity(templateDto);
        TemplateEntity updatedEntity = templateService.updateTemplate(templateId, templateEntity);
        return new ResponseEntity<>(templateMapper.toDto(updatedEntity), HttpStatus.OK);
    }

    @DeleteMapping("/api/templates/{templateId}")
    public ResponseEntity<?> deleteTemplate(@PathVariable("templateId") String templateId) {
        templateService.deleteTemplate(templateId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
