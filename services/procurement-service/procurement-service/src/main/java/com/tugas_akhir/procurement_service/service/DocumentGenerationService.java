package com.tugas_akhir.procurement_service.service;

import com.tugas_akhir.procurement_service.domain.additionaldocument.entity.AdditionalDocument;
import com.tugas_akhir.procurement_service.domain.additionaldocument.repository.AdditionalDocumentRepository;
import com.tugas_akhir.procurement_service.domain.procurementrequest.entity.ProcurementRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service to handle auto-generation of documents (Use Case 1.1)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentGenerationService {

    private final AdditionalDocumentRepository documentRepository;

    /**
     * Auto-generate standard PR document
     * 
     * @param pr The procurement request
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void generateStandardPRDocument(ProcurementRequest pr) {
        log.info("Auto-generating standard document for PR: {}", pr.getId());

        try {
            // Mock PDF generation - in real app would use JasperReports or similar
            AdditionalDocument doc = AdditionalDocument.builder()
                    .procurementRequest(pr)
                    .fileName("PR-" + pr.getId().toString().substring(0, 8) + ".pdf")
                    .fileType("application/pdf")
                    .fileSize(1024L) // Mock size
                    .fileUrl("https://storage.example.com/generated/" + pr.getId()) // Mock URL
                    .build();

            documentRepository.save(doc);
            log.info("Document generated successfully: {}", doc.getFileName());

        } catch (Exception e) {
            log.error("Failed to generate document for PR: {}", pr.getId(), e);
        }
    }
}
