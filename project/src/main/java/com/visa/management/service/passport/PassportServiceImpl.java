package com.visa.management.service.passport;

import com.visa.management.model.passport.Passport;
import com.visa.management.repository.passport.PassportRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PassportServiceImpl implements PassportService {

    private final PassportRepository passportRepository;

    public PassportServiceImpl(PassportRepository passportRepository) {
        this.passportRepository = passportRepository;
    }

    @Override
    public List<Passport> findAllPassports() {
        return passportRepository.findAll();
    }
}
