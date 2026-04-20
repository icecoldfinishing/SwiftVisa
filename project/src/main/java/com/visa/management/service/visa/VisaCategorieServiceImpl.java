package com.visa.management.service.visa;

import com.visa.management.model.visa.VisaCategorie;
import com.visa.management.repository.visa.VisaCategorieRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class VisaCategorieServiceImpl implements VisaCategorieService {

    private final VisaCategorieRepository visaCategorieRepository;

    public VisaCategorieServiceImpl(VisaCategorieRepository visaCategorieRepository) {
        this.visaCategorieRepository = visaCategorieRepository;
    }

    @Override
    public List<VisaCategorie> findAllVisaCategories() {
        return visaCategorieRepository.findAll();
    }
}
