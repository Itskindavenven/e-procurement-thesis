package com.tugas_akhir.procurement_service.domain.serviceTermin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tugas_akhir.procurement_service.domain.serviceTermin.entity.TerminDetails;

import java.util.UUID;

@Repository
public interface TerminDetailsRepository extends JpaRepository<TerminDetails, UUID> {
}

