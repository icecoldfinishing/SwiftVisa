package com.visa.management.repository.demande;

import com.visa.management.model.demande.Demande;
import com.visa.management.repository.demande.projection.DemandeSearchProjection;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DemandeRepository extends JpaRepository<Demande, Long> {

	@Query(
		value = """
			SELECT
				d.id AS id,
				dm.nom AS nom,
				dm.prenom AS prenom,
				dm.telephone AS contact,
				dm.adresse AS adresse,
				d.id_categorie_visa AS idCategorieVisa,
				d.date_demande AS dateDemande,
				d.lieu AS lieu,
				d.date_entree AS dateEntree,
				d.date_expiration AS dateExpiration,
				d.motif_demande AS motifDemande,
				dts.code AS statut,
				p.numero AS numeroPassport
			FROM demande d
			JOIN demandeur dm ON dm.id = d.id_demandeur
			LEFT JOIN passport p ON p.id = d.id_passport
			LEFT JOIN demande_status ds ON ds.id_demande = d.id
			LEFT JOIN demande_type_status dts ON dts.id = ds.id_demande_type_status
			WHERE (:statut IS NULL OR UPPER(dts.code) = UPPER(:statut))
			  AND (:idCategorieVisa IS NULL OR d.id_categorie_visa = :idCategorieVisa)
			  AND (:idNationalite IS NULL OR dm.id_nationalite = :idNationalite)
			  AND (:dateDebut IS NULL OR d.date_demande >= :dateDebut)
			  AND (:dateFin IS NULL OR d.date_demande <= :dateFin)
			  AND (
					:recherche IS NULL
					OR UPPER(dm.nom) LIKE UPPER(CONCAT('%', :recherche, '%'))
					OR UPPER(dm.prenom) LIKE UPPER(CONCAT('%', :recherche, '%'))
					OR UPPER(COALESCE(p.numero, '')) LIKE UPPER(CONCAT('%', :recherche, '%'))
					OR UPPER(COALESCE(d.motif_demande, '')) LIKE UPPER(CONCAT('%', :recherche, '%'))
			  )
			ORDER BY d.id DESC
			""",
		nativeQuery = true
	)
	List<DemandeSearchProjection> searchDemandes(
		@Param("statut") String statut,
		@Param("idCategorieVisa") Long idCategorieVisa,
		@Param("idNationalite") Long idNationalite,
		@Param("dateDebut") LocalDate dateDebut,
		@Param("dateFin") LocalDate dateFin,
		@Param("recherche") String recherche
	);
}
