package com.tugas_akhir.procurement_service.domain.inventory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "stock_mutations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockMutation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "inventory_item_id", nullable = false)
    private InventoryItem inventoryItem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MutationType type;

    @Column(nullable = false)
    private Integer quantity;

    private String reason;

    private String referenceId; // e.g., PO-123 or Manually entered ID

    @CreationTimestamp
    private LocalDateTime createdAt;

    public enum MutationType {
        IN, OUT, ADJUSTMENT
    }
}

