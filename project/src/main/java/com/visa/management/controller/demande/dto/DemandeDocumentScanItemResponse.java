package com.visa.management.controller.demande.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DemandeDocumentScanItemResponse {

    private Long idDemandeDocument;
    private Long idDocument;
    private String documentLibelle;
    private boolean obligatoire;
    private String scanFileName;
    private Long scanFileSize;
    private boolean scanned;
}
