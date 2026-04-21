package com.visa.management.service.visa;

import com.visa.management.model.visa.CarteResidant;
import com.visa.management.repository.visa.CarteResidantRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CarteResidantServiceImpl implements CarteResidantService {

    private final CarteResidantRepository carteResidantRepository;

    public CarteResidantServiceImpl(CarteResidantRepository carteResidantRepository) {
        this.carteResidantRepository = carteResidantRepository;
    }

    @Override
    public List<CarteResidant> findAllCartesResidant() {
        return carteResidantRepository.findAll();
    }
}
