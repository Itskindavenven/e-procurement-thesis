package com.tugas_akhir.procurement_service.domain.procurementrequest.mapper;

import com.tugas_akhir.procurement_service.domain.procurementrequest.dto.ProcurementRequestDTOs.*;
import com.tugas_akhir.procurement_service.domain.procurementrequest.entity.ProcurementItem;
import com.tugas_akhir.procurement_service.domain.procurementrequest.entity.ProcurementRequest;
import com.tugas_akhir.procurement_service.domain.delivery.entity.DeliveryDetail;
import com.tugas_akhir.procurement_service.domain.additionaldocument.entity.AdditionalDocument;
import com.tugas_akhir.procurement_service.domain.audit.entity.ApprovalHistory;

import org.mapstruct.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * MapStruct mapper for ProcurementRequest and related entities
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProcurementRequestMapper {

    // ========== ProcurementRequest Mappings ==========

    /**
     * Map CreateDTO to Entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "operatorId", ignore = true) // Set by service
    @Mapping(target = "status", ignore = true) // Set by service to DRAFT
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true) // Set by service
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "approvalHistories", ignore = true) // Not in create DTO
    @Mapping(source = "deliveryDetail", target = "deliveryDetails", qualifiedByName = "singleDeliveryToList")
    ProcurementRequest toEntity(CreateProcurementRequestDTO dto);

    /**
     * Map Entity to ResponseDTO
     */
    @Mapping(target = "vendorName", ignore = true) // Populated by service from vendor client
    @Mapping(target = "locationName", ignore = true) // Populated by service from master data
    @Mapping(target = "status", expression = "java(entity.getStatus().name())")
    @Mapping(target = "totalAmount", expression = "java(calculateTotalAmount(entity.getItems()))")
    @Mapping(source = "deliveryDetails", target = "deliveryDetail", qualifiedByName = "firstDeliveryDetail")
    ProcurementRequestResponseDTO toResponseDTO(ProcurementRequest entity);

    /**
     * Map Entity to SummaryDTO
     */
    @Mapping(target = "vendorName", ignore = true) // Populated by service
    @Mapping(target = "status", expression = "java(entity.getStatus().name())")
    @Mapping(target = "totalAmount", expression = "java(calculateTotalAmount(entity.getItems()))")
    @Mapping(target = "itemCount", expression = "java(entity.getItems() != null ? entity.getItems().size() : 0)")
    ProcurementRequestSummaryDTO toSummaryDTO(ProcurementRequest entity);

    // ========== ProcurementItem Mappings ==========

    /**
     * Map Item DTO to Entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "procurementRequest", ignore = true) // Set by parent
    ProcurementItem toItemEntity(ProcurementItemDTO dto);

    /**
     * Map Item Entity to DTO
     */
    ProcurementItemDTO toItemDTO(ProcurementItem entity);

    List<ProcurementItem> toItemEntities(List<ProcurementItemDTO> dtos);

    List<ProcurementItemDTO> toItemDTOs(List<ProcurementItem> entities);

    // ========== DeliveryDetail Mappings ==========

    /**
     * Map Delivery DTO to Entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "procurementRequest", ignore = true) // Set by parent
    DeliveryDetail toDeliveryEntity(DeliveryDetailDTO dto);

    /**
     * Map Delivery Entity to DTO
     */
    DeliveryDetailDTO toDeliveryDTO(DeliveryDetail entity);

    List<DeliveryDetail> toDeliveryEntities(List<DeliveryDetailDTO> dtos);

    List<DeliveryDetailDTO> toDeliveryDTOs(List<DeliveryDetail> entities);

    // ========== AdditionalDocument Mappings ==========

    /**
     * Map Document DTO to Entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "procurementRequest", ignore = true) // Set by parent
    AdditionalDocument toDocumentEntity(AdditionalDocumentDTO dto);

    /**
     * Map Document Entity to DTO
     */
    AdditionalDocumentDTO toDocumentDTO(AdditionalDocument entity);

    List<AdditionalDocument> toDocumentEntities(List<AdditionalDocumentDTO> dtos);

    List<AdditionalDocumentDTO> toDocumentDTOs(List<AdditionalDocument> entities);

    // ========== Helper Methods ==========

    /**
     * Calculate total amount from items
     */
    default BigDecimal calculateTotalAmount(List<ProcurementItem> items) {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return items.stream()
                .map(item -> {
                    BigDecimal subtotal = item.getUnitPrice()
                            .multiply(BigDecimal.valueOf(item.getQuantity()));
                    if (item.getTaxPpn() != null) {
                        subtotal = subtotal.add(item.getTaxPpn());
                    }
                    return subtotal;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Get first delivery detail from list (for backward compatibility)
     */
    @Named("firstDeliveryDetail")
    default DeliveryDetailDTO firstDeliveryDetail(List<DeliveryDetail> deliveryDetails) {
        if (deliveryDetails == null || deliveryDetails.isEmpty()) {
            return null;
        }
        return toDeliveryDTO(deliveryDetails.get(0));
    }

    /**
     * Convert single DeliveryDetailDTO to List<DeliveryDetail>
     * Used when creating entity from DTO
     */
    @Named("singleDeliveryToList")
    default List<DeliveryDetail> singleDeliveryToList(DeliveryDetailDTO deliveryDetail) {
        if (deliveryDetail == null) {
            return null;
        }
        return List.of(toDeliveryEntity(deliveryDetail));
    }

    /**
     * Update entity from UpdateDTO (for PATCH operations)
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "operatorId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "items", ignore = true) // Handle separately in service
    @Mapping(target = "deliveryDetails", ignore = true) // Handle separately in service
    @Mapping(target = "documents", ignore = true) // Handle separately in service
    @Mapping(target = "approvalHistories", ignore = true)
    void updateEntityFromDTO(UpdateProcurementRequestDTO dto, @MappingTarget ProcurementRequest entity);
}
