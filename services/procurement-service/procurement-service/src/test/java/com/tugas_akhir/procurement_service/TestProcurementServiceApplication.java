package com.tugas_akhir.procurement_service;

import org.springframework.boot.SpringApplication;

import com.tugas_akhir.procurement_service.ProcurementServiceApplication;

public class TestProcurementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(ProcurementServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
