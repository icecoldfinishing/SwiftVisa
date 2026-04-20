package com.visa.management.service.demande;

import com.visa.management.model.demande.Demande;
import java.util.List;

public interface DemandeService {

    List<Demande> findAllDemandes();
}
