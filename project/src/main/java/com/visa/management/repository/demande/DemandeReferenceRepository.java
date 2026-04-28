package com.visa.management.repository.demande;

import com.visa.management.model.demande.DemandeReference;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DemandeReferenceRepository extends JpaRepository<DemandeReference, Long> {

    List<DemandeReference> findByIdDemande(Long idDemande);

    @Modifying
    @Query("delete from DemandeReference dr where dr.idDemande = :idDemande")
    int deleteAllByIdDemande(@Param("idDemande") Long idDemande);
}
