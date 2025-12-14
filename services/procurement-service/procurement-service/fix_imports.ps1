$root = "c:\Users\Asus\Desktop\tugas-akhir\backend\services\procurement-service\procurement-service\src\main\java\com\tugas_akhir\procurementservice"

# 1. Update Package Declarations
Get-ChildItem -Path $root -Recurse -Filter "*.java" | ForEach-Object {
    $filePath = $_.FullName
    $relativePath = $filePath.Substring($root.Length + 1)
    $folderPath = [System.IO.Path]::GetDirectoryName($relativePath)
    
    # Convert path to package: domain\procurementrequest\controller -> domain.procurementrequest.controller
    $packageSuffix = $folderPath.Replace("\", ".")
    $newPackage = "package com.tugas_akhir.procurementservice.$packageSuffix;"
    
    $content = Get-Content $filePath -Raw
    
    # Regex to replace existing package declaration
    $content = $content -replace "package com\.tugas_akhir\.procurementservice\..*;", $newPackage
    $content = $content -replace "package com\.tugas_akhir\.procurementservice;", $newPackage
    
    # 2. Update Imports (Hardcoded Mappings based on moves)
    
    # Procurement Request
    $content = $content -replace "import com\.tugas_akhir\.procurementservice\.entity\.ProcurementRequest;", "import com.tugas_akhir.procurementservice.domain.procurementrequest.entity.ProcurementRequest;"
    $content = $content -replace "import com\.tugas_akhir\.procurementservice\.entity\.ProcurementItem;", "import com.tugas_akhir.procurementservice.domain.procurementrequest.entity.ProcurementItem;"
    $content = $content -replace "import com\.tugas_akhir\.procurementservice\.repository\.ProcurementRequestRepository;", "import com.tugas_akhir.procurementservice.domain.procurementrequest.repository.ProcurementRequestRepository;"
    $content = $content -replace "import com\.tugas_akhir\.procurementservice\.repository\.ProcurementItemRepository;", "import com.tugas_akhir.procurementservice.domain.procurementrequest.repository.ProcurementItemRepository;"
    $content = $content -replace "import com\.tugas_akhir\.procurementservice\.dto\.ProcurementRequestDTO;", "import com.tugas_akhir.procurementservice.domain.procurementrequest.dto.ProcurementRequestDTO;"
    $content = $content -replace "import com\.tugas_akhir\.procurementservice\.dto\.ProcurementItemDTO;", "import com.tugas_akhir.procurementservice.domain.procurementrequest.dto.ProcurementItemDTO;"
    $content = $content -replace "import com\.tugas_akhir\.procurementservice\.service\.ProcurementRequestService;", "import com.tugas_akhir.procurementservice.domain.procurementrequest.service.ProcurementRequestService;"

    # Delivery
    $content = $content -replace "import com\.tugas_akhir\.procurementservice\.entity\.DeliveryDetail;", "import com.tugas_akhir.procurementservice.domain.delivery.entity.DeliveryDetail;"
    $content = $content -replace "import com\.tugas_akhir\.procurementservice\.repository\.DeliveryDetailRepository;", "import com.tugas_akhir.procurementservice.domain.delivery.repository.DeliveryDetailRepository;"
    $content = $content -replace "import com\.tugas_akhir\.procurementservice\.dto\.DeliveryDetailDTO;", "import com.tugas_akhir.procurementservice.domain.delivery.dto.DeliveryDetailDTO;"
    $content = $content -replace "import com\.tugas_akhir\.procurementservice\.service\.DeliveryService;", "import com.tugas_akhir.procurementservice.domain.delivery.service.DeliveryService;"

    # Procurement Order
    $content = $content -replace "import com\.tugas_akhir\.procurementservice\.entity\.POHeader;", "import com.tugas_akhir.procurementservice.domain.procurementorder.entity.POHeader;"
    $content = $content -replace "import com\.tugas_akhir\.procurementservice\.entity\.POItem;", "import com.tugas_akhir.procurementservice.domain.procurementorder.entity.POItem;"
    $content = $content -replace "import com\.tugas_akhir\.procurementservice\.repository\.POHeaderRepository;", "import com.tugas_akhir.procurementservice.domain.procurementorder.repository.POHeaderRepository;"
    $content = $content -replace "import com\.tugas_akhir\.procurementservice\.repository\.POItemRepository;", "import com.tugas_akhir.procurementservice.domain.procurementorder.repository.POItemRepository;"
    $content = $content -replace "import com\.tugas_akhir\.procurementservice\.dto\.POHeaderDTO;", "import com.tugas_akhir.procurementservice.domain.procurementorder.dto.POHeaderDTO;"
    
    # Service Termin
    $content = $content -replace "import com\.tugas_akhir\.procurementservice\.entity\.TerminDetails;", "import com.tugas_akhir.procurementservice.domain.serviceTermin.entity.TerminDetails;"
    $content = $content -replace "import com\.tugas_akhir\.procurementservice\.repository\.TerminDetailsRepository;", "import com.tugas_akhir.procurementservice.domain.serviceTermin.repository.TerminDetailsRepository;"
    $content = $content -replace "import com\.tugas_akhir\.procurementservice\.service\.TerminReviewService;", "import com.tugas_akhir.procurementservice.domain.serviceTermin.service.TerminReviewService;"

    # Receiving
    $content = $content -replace "import com\.tugas_akhir\.procurementservice\.service\.GoodsReceivingService;", "import com.tugas_akhir.procurementservice.domain.receiving.service.GoodsReceivingService;"
    
    # Inventory
    $content = $content -replace "import com\.tugas_akhir\.procurementservice\.inventory\.service", "import com.tugas_akhir.procurementservice.domain.inventory.service"
    $content = $content -replace "import com\.tugas_akhir\.procurementservice\.inventory\.entity", "import com.tugas_akhir.procurementservice.domain.inventory.entity"
    
    # Clients
    $content = $content -replace "import com\.tugas_akhir\.procurementservice\.client\.VendorServiceClient;", "import com.tugas_akhir.procurementservice.integration.vendor.VendorServiceClient;"
    $content = $content -replace "import com\.tugas_akhir\.procurementservice\.client\.NotificationServiceClient;", "import com.tugas_akhir.procurementservice.integration.notification.NotificationServiceClient;"

    # Dashboard/Reporting/Audit
    $content = $content -replace "import com\.tugas_akhir\.procurementservice\.service\.DashboardOperatorService;", "import com.tugas_akhir.procurementservice.domain.dashboard.service.DashboardOperatorService;"
    $content = $content -replace "import com\.tugas_akhir\.procurementservice\.service\.DashboardSupervisorService;", "import com.tugas_akhir.procurementservice.domain.dashboard.service.DashboardSupervisorService;"
    $content = $content -replace "import com\.tugas_akhir\.procurementservice\.service\.ReportingService;", "import com.tugas_akhir.procurementservice.domain.reporting.service.ReportingService;"
    $content = $content -replace "import com\.tugas_akhir\.procurementservice\.service\.AuditTrailService;", "import com.tugas_akhir.procurementservice.domain.audit.service.AuditTrailService;"
    
    Set-Content -Path $filePath -Value $content
}
Write-Host "Replaced Imports and Packages in all files."
