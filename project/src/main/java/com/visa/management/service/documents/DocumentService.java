package com.visa.management.service.documents;

import com.visa.management.model.documents.Document;
import java.util.List;

public interface DocumentService {

    List<Document> findAllDocuments();
}
