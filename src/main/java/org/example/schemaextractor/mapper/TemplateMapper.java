package org.example.schemaextractor.mapper;

import org.example.schemaextractor.domain.dto.TemplateDto;
import org.example.schemaextractor.domain.entities.TemplateEntity;
import org.springframework.stereotype.Component;

@Component
public class TemplateMapper {
    public TemplateEntity toEntity(TemplateDto templateDto) {
        return new TemplateEntity(
                templateDto.getId(),
                templateDto.getName(),
                templateDto.getDescription()
        );
    }

    public TemplateDto toDto(TemplateEntity templateEntity) {
        return new TemplateDto(
                templateEntity.getId(),
                templateEntity.getName(),
                templateEntity.getDescription()
        );
    }
}
