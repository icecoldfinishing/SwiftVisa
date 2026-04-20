package com.visa.management.controller.demande;

import com.visa.management.model.demande.DemandeStatus;
import com.visa.management.service.demande.DemandeStatusService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demande-statuses")
public class DemandeStatusController {

    private final DemandeStatusService demandeStatusService;

    public DemandeStatusController(DemandeStatusService demandeStatusService) {
        this.demandeStatusService = demandeStatusService;
    }

    @GetMapping
    public List<DemandeStatus> listDemandeStatuses() {
        return demandeStatusService.findAllDemandeStatuses();
    }
}
