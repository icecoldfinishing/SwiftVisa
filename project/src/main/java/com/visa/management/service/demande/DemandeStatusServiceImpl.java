package com.visa.management.service.demande;

import com.visa.management.model.demande.DemandeStatus;
import com.visa.management.repository.demande.DemandeStatusRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DemandeStatusServiceImpl implements DemandeStatusService {

    private final DemandeStatusRepository demandeStatusRepository;

    public DemandeStatusServiceImpl(DemandeStatusRepository demandeStatusRepository) {
        this.demandeStatusRepository = demandeStatusRepository;
    }

    @Override
    public List<DemandeStatus> findAllDemandeStatuses() {
        return demandeStatusRepository.findAll();
    }
}
