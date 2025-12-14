package com.tugas_akhir.admin_service.dto.kafka;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserEventDto {
    private String eventType;
    private String userId;
    private String username;
    private String email;
    private String status;
    private List<RoleDto> roles;
    private Long timestamp;

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RoleDto {
        private Long id;
        private String name;
    }
}
