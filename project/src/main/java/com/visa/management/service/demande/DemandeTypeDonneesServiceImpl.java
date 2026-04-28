package com.visa.management.service.demande;

import com.visa.management.model.demande.DemandeTypeDonnees;
import com.visa.management.repository.demande.DemandeTypeDonneesRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DemandeTypeDonneesServiceImpl implements DemandeTypeDonneesService {

    private final DemandeTypeDonneesRepository demandeTypeDonneesRepository;

    public DemandeTypeDonneesServiceImpl(DemandeTypeDonneesRepository demandeTypeDonneesRepository) {
        this.demandeTypeDonneesRepository = demandeTypeDonneesRepository;
    }

    @Override
    public List<DemandeTypeDonnees> findAllDemandeTypeDonnees() {
        return demandeTypeDonneesRepository.findAll();
    }
}