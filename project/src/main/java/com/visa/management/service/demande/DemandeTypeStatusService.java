package com.visa.management.service.demande;

import com.visa.management.model.demande.DemandeTypeStatus;
import java.util.List;

public interface DemandeTypeStatusService {

    List<DemandeTypeStatus> findAllDemandeTypeStatuses();
}
