package com.visa.management.service.documents;

import com.visa.management.model.documents.Document;
import com.visa.management.repository.documents.DocumentRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    public DocumentServiceImpl(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    public List<Document> findAllDocuments() {
        return documentRepository.findAll();
    }
}
