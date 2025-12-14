package com.tugas_akhir.admin_service.notification.service;

import com.tugas_akhir.admin_service.notification.dto.NotificationTemplateDTO;
import com.tugas_akhir.admin_service.notification.entity.NotificationTemplate;
import com.tugas_akhir.admin_service.notification.entity.NotificationTemplatePlaceholder;
import com.tugas_akhir.admin_service.notification.mapper.NotificationMapper;
import com.tugas_akhir.admin_service.notification.repository.NotificationTemplatePlaceholderRepository;
import com.tugas_akhir.admin_service.notification.repository.NotificationTemplateRepository;
import com.tugas_akhir.admin_service.notification.repository.NotificationLogRepository;
import com.tugas_akhir.admin_service.notification.dto.NotificationLogDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationTemplateRepository templateRepository;
    private final NotificationTemplatePlaceholderRepository placeholderRepository;
    private final NotificationLogRepository logRepository;
    private final NotificationMapper notificationMapper;

    @Transactional(readOnly = true)
    public List<NotificationTemplateDTO> getAllTemplates() {
        return templateRepository.findAll().stream()
                .map(this::enrichDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NotificationLogDTO> getLogs() {
        return logRepository.findAll().stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public NotificationTemplateDTO getTemplateById(UUID id) {
        NotificationTemplate template = templateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Template not found with ID: " + id));
        return enrichDTO(template);
    }

    @Transactional
    public NotificationTemplateDTO createTemplate(NotificationTemplateDTO dto) {
        if (templateRepository.existsByTemplateCode(dto.getTemplateCode())) {
            throw new IllegalArgumentException("Template code already exists: " + dto.getTemplateCode());
        }

        NotificationTemplate template = notificationMapper.toEntity(dto);
        template.setStatus("ACTIVE");
        NotificationTemplate savedTemplate = templateRepository.save(template);

        if (dto.getPlaceholders() != null) {
            for (String key : dto.getPlaceholders()) {
                NotificationTemplatePlaceholder placeholder = new NotificationTemplatePlaceholder();
                placeholder.setTemplateId(savedTemplate.getTemplateId());
                placeholder.setPlaceholderKey(key);
                placeholderRepository.save(placeholder);
            }
        }

        return enrichDTO(savedTemplate);
    }

    @Transactional
    public NotificationTemplateDTO updateTemplate(UUID id, NotificationTemplateDTO dto) {
        NotificationTemplate template = templateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Template not found with ID: " + id));

        if (!template.getTemplateCode().equals(dto.getTemplateCode())
                && templateRepository.existsByTemplateCode(dto.getTemplateCode())) {
            throw new IllegalArgumentException("Template code already exists: " + dto.getTemplateCode());
        }

        notificationMapper.updateEntityFromDTO(dto, template);
        NotificationTemplate updatedTemplate = templateRepository.save(template);

        // Update placeholders if needed (simple implementation: delete all and re-add)
        if (dto.getPlaceholders() != null) {
            List<NotificationTemplatePlaceholder> existing = placeholderRepository.findByTemplateId(id);
            placeholderRepository.deleteAll(existing);

            for (String key : dto.getPlaceholders()) {
                NotificationTemplatePlaceholder placeholder = new NotificationTemplatePlaceholder();
                placeholder.setTemplateId(updatedTemplate.getTemplateId());
                placeholder.setPlaceholderKey(key);
                placeholderRepository.save(placeholder);
            }
        }

        return enrichDTO(updatedTemplate);
    }

    private NotificationTemplateDTO enrichDTO(NotificationTemplate template) {
        NotificationTemplateDTO dto = notificationMapper.toDTO(template);
        List<String> placeholders = placeholderRepository.findByTemplateId(template.getTemplateId()).stream()
                .map(NotificationTemplatePlaceholder::getPlaceholderKey)
                .collect(Collectors.toList());
        dto.setPlaceholders(placeholders);
        return dto;
    }
}
