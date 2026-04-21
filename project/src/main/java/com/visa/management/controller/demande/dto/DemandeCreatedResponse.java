package com.visa.management.controller.demande.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DemandeCreatedResponse {

    private Long idDemande;
    private String statut;
}
