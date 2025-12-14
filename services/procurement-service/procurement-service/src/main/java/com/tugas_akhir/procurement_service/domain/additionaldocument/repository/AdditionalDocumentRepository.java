package com.tugas_akhir.procurement_service.domain.additionaldocument.repository;

import com.tugas_akhir.procurement_service.domain.additionaldocument.entity.AdditionalDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AdditionalDocumentRepository extends JpaRepository<AdditionalDocument, UUID> {
}
