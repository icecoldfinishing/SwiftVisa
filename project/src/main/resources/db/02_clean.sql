DROP DATABASE IF EXISTS swiftvisa_db;
CREATE DATABASE swiftvisa_db;
\c swiftvisa_db;

-- utilities
CREATE TABLE situation_familiale (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(100)
);

CREATE TABLE nationalite (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(100)
);

-- visa type 
CREATE TABLE visa_categorie (
    id SERIAL PRIMARY KEY,
    code VARCHAR(100),
    libelle VARCHAR(100) -- travailleur -- investisseur
);

-- demandeur
CREATE TABLE demandeur (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100),
    prenom VARCHAR(100),
    nom_jeune_fille VARCHAR(100),
    date_naissance DATE,
    lieu_naissance VARCHAR(100),
    adresse VARCHAR(255),
    telephone VARCHAR(20),
    id_situation_familiale INT,
    id_nationalite INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_situation_familiale) REFERENCES situation_familiale(id),
    FOREIGN KEY (id_nationalite) REFERENCES nationalite(id)
);

-- passport
CREATE TABLE passport (
    id SERIAL PRIMARY KEY,
    id_demandeur INT,
    numero VARCHAR(50),
    date_delivrance DATE,
    date_expiration DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_demandeur) REFERENCES demandeur(id)
);

-- document
CREATE TABLE document (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(100)
);

CREATE TABLE document_categorie_visa (
    id SERIAL PRIMARY KEY,
    id_document INT,
    id_categorie_visa INT,
    is_obligatoire BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_document) REFERENCES document(id),
    FOREIGN KEY (id_categorie_visa) REFERENCES visa_categorie(id),
    CONSTRAINT uq_document_categorie_visa UNIQUE (id_document, id_categorie_visa)
);

-- demande type
CREATE TABLE demande_type (
    id SERIAL PRIMARY KEY,
    code VARCHAR(100),
    libelle VARCHAR(100)
);

CREATE TABLE demande_type_donnees (
    id SERIAL PRIMARY KEY,
    code VARCHAR(100),
    libelle VARCHAR(100)
);

CREATE TABLE demande_type_donnees_option (
    id SERIAL PRIMARY KEY,
    id_demande_type INT NOT NULL,
    id_demande_donnees_type INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_demande_type) REFERENCES demande_type(id),
    FOREIGN KEY (id_demande_donnees_type) REFERENCES demande_type_donnees(id),
    CONSTRAINT uq_demande_type_donnees_option UNIQUE (id_demande_type, id_demande_donnees_type)
);

CREATE TABLE demande_reference_type (
    id SERIAL PRIMARY KEY,
    code VARCHAR(100),
    libelle VARCHAR(100)
);

-- demande
CREATE TABLE demande (
    id SERIAL PRIMARY KEY,
    id_demande_type INT,
    id_demande_donnees_type INT,
    id_demande_source INT,
    id_demandeur INT,
    id_categorie_visa INT,
    id_passport INT,
    date_demande DATE,
    lieu VARCHAR(100),
    date_entree DATE,
    date_expiration DATE,
    motif_demande VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_demande_type) REFERENCES demande_type(id),
    FOREIGN KEY (id_demandeur) REFERENCES demandeur(id),
    FOREIGN KEY (id_categorie_visa) REFERENCES visa_categorie(id),
    FOREIGN KEY (id_passport) REFERENCES passport(id),
    FOREIGN KEY (id_demande_donnees_type) REFERENCES demande_type_donnees(id),
    FOREIGN KEY (id_demande_source) REFERENCES demande(id)
);

-- status type
CREATE TABLE demande_type_status (
    id SERIAL PRIMARY KEY,
    code VARCHAR(100),
    libelle VARCHAR(100)
);

-- status
CREATE TABLE demande_status (
    id SERIAL PRIMARY KEY,
    id_demande INT,
    id_demande_type_status INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_demande) REFERENCES demande(id),
    FOREIGN KEY (id_demande_type_status) REFERENCES demande_type_status(id)
);

CREATE TABLE demande_document (
    id SERIAL PRIMARY KEY,
    id_demande INT NOT NULL,
    id_document INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_demande) REFERENCES demande(id),
    FOREIGN KEY (id_document) REFERENCES document(id),
    CONSTRAINT uq_demande_document UNIQUE (id_demande, id_document)
);

CREATE TABLE demande_reference (
    id SERIAL PRIMARY KEY,
    id_demande INT NOT NULL,
    id_demande_reference_type INT NOT NULL,
    valeur VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_demande) REFERENCES demande(id),
    FOREIGN KEY (id_demande_reference_type) REFERENCES demande_reference_type(id),
    CONSTRAINT uq_demande_reference UNIQUE (id_demande, id_demande_reference_type)
);

-- visa
CREATE TABLE visa (
    id SERIAL PRIMARY KEY,
    ref VARCHAR(50),
    id_demande INT,
    date_debut DATE,
    date_fin DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_demande) REFERENCES demande(id)
);

CREATE TABLE carte_residant (
    id SERIAL PRIMARY KEY,
    ref VARCHAR(50),
    id_demande INT,
    date_debut DATE,
    date_fin DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_demande) REFERENCES demande(id)
);

INSERT INTO demande_type (code, libelle) VALUES
('NOUVEAU_TITRE', 'nouveau titre'),
('DUPLICATAT_CARTE_RESIDENCE', 'duplicata de carte de residence'),
('TRANSFERT_VISA_NOUVEAU_PASSEPORT', 'transfert de visa sur nouveau passeport');

INSERT INTO demande_type_donnees (code, libelle) VALUES
('AVEC_DONNEES_ANTERIEUR', 'avec donnees anterieur'),
('SANS_DONNEES_ANTERIEUR', 'sans donnees anterieur');

INSERT INTO demande_type_donnees_option (id_demande_type, id_demande_donnees_type)
SELECT dt.id, dd.id
FROM demande_type dt
JOIN demande_type_donnees dd
    ON dd.code IN ('AVEC_DONNEES_ANTERIEUR', 'SANS_DONNEES_ANTERIEUR')
WHERE dt.code IN ('DUPLICATAT_CARTE_RESIDENCE', 'TRANSFERT_VISA_NOUVEAU_PASSEPORT');

INSERT INTO demande_reference_type (code, libelle) VALUES
('PASSPORT_ANTERIEUR', 'numero de passport anterieur'),
('VISA_ANTERIEUR', 'reference visa anterieur'),
('CARTE_RESIDANT_ANTERIEUR', 'reference carte residant anterieur');

INSERT INTO situation_familiale (libelle) VALUES
('Celibataire'),
('Marie(e)'),
('Divorce(e)'),
('Veuf/Veuve');

INSERT INTO nationalite (libelle) VALUES
('Malagache'),
('Français'),
('Comorien'),
('Mauricien'),
('Kenyan'),
('Sud-africain');

-- INSERT INTO passport (numero, date_delivrance, date_expiration) VALUES
-- ('P100001', '2020-01-15', '2030-01-15'),
-- ('P100002', '2019-06-20', '2029-06-20'),
-- ('P100003', '2021-03-10', '2031-03-10'),
-- ('P100004', '2018-11-05', '2028-11-05'),
-- ('P100005', '2022-07-25', '2032-07-25');

INSERT INTO document (libelle) VALUES 
('02 photos d''identite'),
('notice de renseignement'),
('demande adressee à Mr le Ministre de l''interieur et de la decentralisation (email + telephone)'),
('photocopie certifiee du visa en cours de validite'),
('photocopie certifiee de la première page du passport'),
('photocopie certifiee de la carte resident en cours de validite'),
('certificat de residence à Madagascar'),
('extrait de casier judiciaire à Madagascar');

INSERT INTO visa_categorie (code, libelle) VALUES
('TRAVAILLEUR', 'travailleur'),
('INVESTISSEUR', 'investisseur');

INSERT INTO demande_type_status (code, libelle) VALUES
('DOSSIER_CREE', 'Dossier cree'),
('DOSSIER_VALIDE', 'Dossier valide');

INSERT INTO document_categorie_visa (id_document, id_categorie_visa, is_obligatoire) VALUES
(1, 1, TRUE),
(2, 1, TRUE),
(3, 1, TRUE),
(5, 1, TRUE),
(1, 2, TRUE),
(2, 2, TRUE),
(3, 2, TRUE),
(5, 2, TRUE),
(7, 2, FALSE),
(8, 2, FALSE);