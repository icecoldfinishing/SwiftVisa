package com.visa.management.repository.passport;

import com.visa.management.model.passport.Passport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassportRepository extends JpaRepository<Passport, Long> {
}
