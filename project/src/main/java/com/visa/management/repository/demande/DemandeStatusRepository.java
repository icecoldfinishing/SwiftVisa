package com.visa.management.repository.demande;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.visa.management.model.demande.DemandeStatus;

public interface DemandeStatusRepository extends JpaRepository<DemandeStatus, Long> {

	Optional<DemandeStatus> findTopByIdDemandeOrderByIdDesc(Long idDemande);

	@Query(
		value = """
			SELECT dts.code
			FROM demande_status ds
			JOIN demande_type_status dts ON dts.id = ds.id_demande_type_status
			WHERE ds.id_demande = :idDemande
			ORDER BY ds.id DESC
			LIMIT 1
			""",
		nativeQuery = true
	)
	String findLatestStatusCodeByIdDemande(@Param("idDemande") Long idDemande);
}
