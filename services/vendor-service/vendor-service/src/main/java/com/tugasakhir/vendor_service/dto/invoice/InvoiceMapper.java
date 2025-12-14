package com.tugasakhir.vendor_service.dto.invoice;

import com.tugasakhir.vendor_service.model.invoice.Invoice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {
    @Mapping(source = "totalTagihan", target = "totalAmount")
    @Mapping(source = "ppn", target = "taxAmount")
    @Mapping(source = "nomorInvoice", target = "invoiceNumber")
    @Mapping(source = "terminRef", target = "paymentTermId")
    InvoiceDTO toDto(Invoice entity);

    @Mapping(source = "totalAmount", target = "totalTagihan")
    @Mapping(source = "taxAmount", target = "ppn")
    @Mapping(source = "invoiceNumber", target = "nomorInvoice")
    @Mapping(source = "paymentTermId", target = "terminRef")
    Invoice toEntity(InvoiceDTO dto);
}
