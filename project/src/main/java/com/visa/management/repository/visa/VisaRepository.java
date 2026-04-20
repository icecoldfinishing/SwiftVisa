package com.visa.management.repository.visa;

import com.visa.management.model.visa.Visa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisaRepository extends JpaRepository<Visa, Long> {
}
