package com.tugas_akhir.procurement_service.common.base;

import org.springframework.http.ResponseEntity;

import com.tugas_akhir.procurement_service.common.BaseResponse;

public abstract class BaseController {

    protected <T> ResponseEntity<BaseResponse<T>> success(T data) {
        return ResponseEntity.ok(BaseResponse.success("Success", data));
    }

    protected <T> ResponseEntity<BaseResponse<T>> success(String message, T data) {
        return ResponseEntity.ok(BaseResponse.success(message, data));
    }

    // Additional helpers can be added here
}

