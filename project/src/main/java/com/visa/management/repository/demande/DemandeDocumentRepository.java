package com.visa.management.repository.demande;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.visa.management.model.demande.DemandeDocument;

public interface DemandeDocumentRepository extends JpaRepository<DemandeDocument, Long> {

	List<DemandeDocument> findByIdDemande(Long idDemande);

	Optional<DemandeDocument> findByIdAndIdDemande(Long id, Long idDemande);

	@Modifying
	@Query("delete from DemandeDocument dd where dd.idDemande = :idDemande")
	int deleteAllByIdDemande(@Param("idDemande") Long idDemande);
}
