package com.visa.management.service.utilities;

import com.visa.management.model.utilities.SituationFamiliale;
import com.visa.management.repository.utilities.SituationFamilialeRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SituationFamilialeServiceImpl implements SituationFamilialeService {

    private final SituationFamilialeRepository situationFamilialeRepository;

    public SituationFamilialeServiceImpl(SituationFamilialeRepository situationFamilialeRepository) {
        this.situationFamilialeRepository = situationFamilialeRepository;
    }

    @Override
    public List<SituationFamiliale> findAllSituationsFamiliales() {
        return situationFamilialeRepository.findAll();
    }
}
