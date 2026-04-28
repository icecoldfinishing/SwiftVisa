package com.visa.management.repository.visa;

import com.visa.management.model.visa.CarteResidant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarteResidantRepository extends JpaRepository<CarteResidant, Long> {

	Optional<CarteResidant> findByIdDemande(Long idDemande);
}
