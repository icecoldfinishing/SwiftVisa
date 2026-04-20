package com.visa.management.service.demande;

import com.visa.management.model.demande.Demande;
import com.visa.management.repository.demande.DemandeRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DemandeServiceImpl implements DemandeService {

    private final DemandeRepository demandeRepository;

    public DemandeServiceImpl(DemandeRepository demandeRepository) {
        this.demandeRepository = demandeRepository;
    }

    @Override
    public List<Demande> findAllDemandes() {
        return demandeRepository.findAll();
    }
}
