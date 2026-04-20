package com.visa.management.controller.documents;

import com.visa.management.model.documents.DocumentCategorieVisa;
import com.visa.management.service.documents.DocumentCategorieVisaService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/document-categories-visa")
public class DocumentCategorieVisaController {

    private final DocumentCategorieVisaService documentCategorieVisaService;

    public DocumentCategorieVisaController(DocumentCategorieVisaService documentCategorieVisaService) {
        this.documentCategorieVisaService = documentCategorieVisaService;
    }

    @GetMapping
    public List<DocumentCategorieVisa> listDocumentCategoriesVisa() {
        return documentCategorieVisaService.findAllDocumentCategorieVisas();
    }
}
