package com.visa.management.service.demandeur;

import com.visa.management.model.demandeur.Demandeur;
import com.visa.management.repository.demandeur.DemandeurRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DemandeurServiceImpl implements DemandeurService {

    private final DemandeurRepository demandeurRepository;

    public DemandeurServiceImpl(DemandeurRepository demandeurRepository) {
        this.demandeurRepository = demandeurRepository;
    }

    @Override
    public List<Demandeur> findAllDemandeurs() {
        return demandeurRepository.findAll();
    }
}
