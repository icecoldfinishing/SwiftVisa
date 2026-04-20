package com.visa.management.controller.visa;

import com.visa.management.model.visa.Visa;
import com.visa.management.service.visa.VisaService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/visas")
public class VisaController {

    private final VisaService visaService;

    public VisaController(VisaService visaService) {
        this.visaService = visaService;
    }

    @GetMapping
    public List<Visa> listVisas() {
        return visaService.findAllVisas();
    }
}
