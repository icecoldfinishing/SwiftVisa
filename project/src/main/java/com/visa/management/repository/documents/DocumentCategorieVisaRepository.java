package com.visa.management.repository.documents;

import com.visa.management.model.documents.DocumentCategorieVisa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DocumentCategorieVisaRepository extends JpaRepository<DocumentCategorieVisa, Long> {

	@Query("select dcv.idDocument from DocumentCategorieVisa dcv where dcv.idCategorieVisa = :idCategorieVisa and dcv.obligatoire = true")
	List<Long> findRequiredDocumentIdsByCategorieVisa(@Param("idCategorieVisa") Long idCategorieVisa);
}
