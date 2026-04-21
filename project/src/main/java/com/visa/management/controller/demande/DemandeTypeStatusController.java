package com.visa.management.controller.demande;

import com.visa.management.model.demande.DemandeTypeStatus;
import com.visa.management.service.demande.DemandeTypeStatusService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demande-type-statuses")
public class DemandeTypeStatusController {

    private final DemandeTypeStatusService demandeTypeStatusService;

    public DemandeTypeStatusController(DemandeTypeStatusService demandeTypeStatusService) {
        this.demandeTypeStatusService = demandeTypeStatusService;
    }

    @GetMapping
    public List<DemandeTypeStatus> listDemandeTypeStatuses() {
        return demandeTypeStatusService.findAllDemandeTypeStatuses();
    }
}
