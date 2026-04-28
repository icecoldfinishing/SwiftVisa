package com.visa.management.repository.visa;

import com.visa.management.model.visa.Visa;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisaRepository extends JpaRepository<Visa, Long> {

	Optional<Visa> findByIdDemande(Long idDemande);
}
