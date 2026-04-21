package com.visa.management.service.demande;

import com.visa.management.model.demande.DemandeType;
import com.visa.management.repository.demande.DemandeTypeRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DemandeTypeServiceImpl implements DemandeTypeService {

    private final DemandeTypeRepository demandeTypeRepository;

    public DemandeTypeServiceImpl(DemandeTypeRepository demandeTypeRepository) {
        this.demandeTypeRepository = demandeTypeRepository;
    }

    @Override
    public List<DemandeType> findAllDemandeTypes() {
        return demandeTypeRepository.findAll();
    }
}
