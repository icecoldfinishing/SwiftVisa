package com.visa.management.controller.visa;

import com.visa.management.model.visa.CarteResidant;
import com.visa.management.service.visa.CarteResidantService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cartes-residant")
public class CarteResidantController {

    private final CarteResidantService carteResidantService;

    public CarteResidantController(CarteResidantService carteResidantService) {
        this.carteResidantService = carteResidantService;
    }

    @GetMapping
    public List<CarteResidant> listCartesResidant() {
        return carteResidantService.findAllCartesResidant();
    }
}
