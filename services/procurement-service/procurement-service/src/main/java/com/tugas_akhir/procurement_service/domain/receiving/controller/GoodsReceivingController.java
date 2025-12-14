package com.tugas_akhir.procurement_service.domain.receiving.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tugas_akhir.procurement_service.domain.procurementorder.dto.POHeaderDTO;
import com.tugas_akhir.procurement_service.domain.receiving.service.GoodsReceivingService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/goods-receiving")
@RequiredArgsConstructor
public class GoodsReceivingController {

    private final GoodsReceivingService service;

    @PostMapping("/{poId}/accept")
    public ResponseEntity<POHeaderDTO> acceptGoods(@PathVariable UUID poId) {
        return ResponseEntity.ok(service.acceptGoods(poId));
    }
}

