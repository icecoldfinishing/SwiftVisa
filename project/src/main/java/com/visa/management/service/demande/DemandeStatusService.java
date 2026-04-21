package com.visa.management.service.demande;

import com.visa.management.model.demande.DemandeStatus;
import java.util.List;

public interface DemandeStatusService {

    List<DemandeStatus> findAllDemandeStatuses();
}
