# Documentation du Sprint 1 - Visa Transformable

## 1. Contexte et objectif du Sprint 1

L’objectif de ce sprint est de permettre l’**enregistrement d’une nouvelle demande de visa transformable**, la gestion du dossier associe, ainsi que la preparation des pièces justificatives.  
Le visa est dit **transformable** : après epuisement du premier delai, il peut être renouvele en choisissant entre deux categories (`travailleur` ou `investisseur associe`).  
En revanche, en cas de retour direct avant epuisement, le visa n’est pas transformable.

Une règle fonctionnelle forte : l’etat doit être informe du caractère transformable du visa. Cette information n’est pas stockee en base de donnees mais geree au niveau applicatif.

---

## 2. Règles metier (Sprint 1)

| Règle | Description |
|-------|-------------|
| `R1` | Une demande de visa transformable peut être renouvelee après epuisement du premier visa. |
| `R2` | Deux categories de renouvellement : `travailleur` ou `investisseur`. |
| `R3` | Une demande ne peut pas depasser le statut `DOSSIER_CREE` si un champ ou une pièce optionnelle est oublie(e). |

---

## 3. Statuts possibles d’un dossier (Sprint 1)

| Statut | Description |
|--------|-------------|
| `DOSSIER_CREE` | Tous les champs obligatoires + pièces obligatoires sont presents (checkbox validee). meme si champ optionel pas rempli .|

---


> Selon le type de visa, certaines pièces sont **obligatoires** ou **optionnelles**.
