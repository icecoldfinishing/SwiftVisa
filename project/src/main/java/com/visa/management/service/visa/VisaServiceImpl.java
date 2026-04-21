package com.visa.management.service.visa;

import com.visa.management.model.visa.Visa;
import com.visa.management.repository.visa.VisaRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class VisaServiceImpl implements VisaService {

    private final VisaRepository visaRepository;

    public VisaServiceImpl(VisaRepository visaRepository) {
        this.visaRepository = visaRepository;
    }

    @Override
    public List<Visa> findAllVisas() {
        return visaRepository.findAll();
    }
}
