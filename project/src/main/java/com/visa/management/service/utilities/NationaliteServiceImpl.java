package com.visa.management.service.utilities;

import com.visa.management.model.utilities.Nationalite;
import com.visa.management.repository.utilities.NationaliteRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class NationaliteServiceImpl implements NationaliteService {

    private final NationaliteRepository nationaliteRepository;

    public NationaliteServiceImpl(NationaliteRepository nationaliteRepository) {
        this.nationaliteRepository = nationaliteRepository;
    }

    @Override
    public List<Nationalite> findAllNationalites() {
        return nationaliteRepository.findAll();
    }
}
