package com.visa.management.service.passport;

import com.visa.management.model.passport.Passport;
import java.util.List;

public interface PassportService {

    List<Passport> findAllPassports();
}
