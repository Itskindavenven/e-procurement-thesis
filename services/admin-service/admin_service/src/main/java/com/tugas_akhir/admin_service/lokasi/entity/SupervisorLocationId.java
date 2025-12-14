package com.tugas_akhir.admin_service.lokasi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupervisorLocationId implements Serializable {
    private UUID supervisorId;
    private UUID locationId;
}
