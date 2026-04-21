package com.visa.management.service.demande;

import com.visa.management.model.demande.DemandeTypeStatus;
import com.visa.management.repository.demande.DemandeTypeStatusRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DemandeTypeStatusServiceImpl implements DemandeTypeStatusService {

    private final DemandeTypeStatusRepository demandeTypeStatusRepository;

    public DemandeTypeStatusServiceImpl(DemandeTypeStatusRepository demandeTypeStatusRepository) {
        this.demandeTypeStatusRepository = demandeTypeStatusRepository;
    }

    @Override
    public List<DemandeTypeStatus> findAllDemandeTypeStatuses() {
        return demandeTypeStatusRepository.findAll();
    }
}
