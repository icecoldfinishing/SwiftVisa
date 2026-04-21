package com.visa.management.service.documents;

import com.visa.management.model.documents.DocumentCategorieVisa;
import com.visa.management.repository.documents.DocumentCategorieVisaRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DocumentCategorieVisaServiceImpl implements DocumentCategorieVisaService {

    private final DocumentCategorieVisaRepository documentCategorieVisaRepository;

    public DocumentCategorieVisaServiceImpl(DocumentCategorieVisaRepository documentCategorieVisaRepository) {
        this.documentCategorieVisaRepository = documentCategorieVisaRepository;
    }

    @Override
    public List<DocumentCategorieVisa> findAllDocumentCategorieVisas() {
        return documentCategorieVisaRepository.findAll();
    }
}
