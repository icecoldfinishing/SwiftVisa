package com.visa.management.repository.documents;

import com.visa.management.model.documents.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}
