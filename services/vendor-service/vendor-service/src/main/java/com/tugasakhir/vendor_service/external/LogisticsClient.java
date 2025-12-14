package com.tugasakhir.vendor_service.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "logistics-service", url = "${application.config.logistics-url:http://localhost:8084}")
public interface LogisticsClient {

    @PostMapping("/api/logistics/shipment")
    String createShipment(@RequestParam("orderId") String orderId, @RequestParam("address") String address);
}
