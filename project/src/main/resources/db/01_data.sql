
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

INSERT INTO passport (numero, date_delivrance, date_expiration) VALUES
('P100001', '2020-01-15', '2030-01-15'),
('P100002', '2019-06-20', '2029-06-20'),
('P100003', '2021-03-10', '2031-03-10'),
('P100004', '2018-11-05', '2028-11-05'),
('P100005', '2022-07-25', '2032-07-25');

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

INSERT INTO demande_type_status (code, libelle)
SELECT 'DOSSIER_CREE', 'Dossier cree'
WHERE NOT EXISTS (SELECT 1 FROM demande_type_status WHERE code = 'DOSSIER_CREE');

INSERT INTO demande_type_status (code, libelle)
SELECT 'DOSSIER_VALIDE', 'Dossier valide'
WHERE NOT EXISTS (SELECT 1 FROM demande_type_status WHERE code = 'DOSSIER_VALIDE');

INSERT INTO demande_type_status (code, libelle)
SELECT 'DOCUMENT_SCANNER', 'Document scanner'
WHERE NOT EXISTS (SELECT 1 FROM demande_type_status WHERE code = 'DOCUMENT_SCANNER');

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
(8, 2, FALSE)
ON CONFLICT (id_document, id_categorie_visa) DO NOTHING;

-- ======================================================
-- TEST DATA SPRINT 3 (dossiers + scan documents)
-- ======================================================

-- Demandeurs de test
INSERT INTO demandeur (
	nom,
	prenom,
	nom_jeune_fille,
	date_naissance,
	lieu_naissance,
	adresse,
	telephone,
	id_situation_familiale,
	id_nationalite
)
SELECT
	'TEST_SCAN',
	'ALICE',
	NULL,
	DATE '1992-04-18',
	'Antananarivo',
	'Lot II A 100, Antananarivo',
	'+261340000001',
	(SELECT MIN(id) FROM situation_familiale WHERE libelle = 'Celibataire'),
	(SELECT MIN(id) FROM nationalite WHERE libelle = 'Malagache')
WHERE NOT EXISTS (
	SELECT 1 FROM demandeur WHERE nom = 'TEST_SCAN' AND prenom = 'ALICE'
);

INSERT INTO demandeur (
	nom,
	prenom,
	nom_jeune_fille,
	date_naissance,
	lieu_naissance,
	adresse,
	telephone,
	id_situation_familiale,
	id_nationalite
)
SELECT
	'TEST_SCAN',
	'BOB',
	NULL,
	DATE '1988-11-03',
	'Toamasina',
	'Lot III B 20, Toamasina',
	'+261340000002',
	(SELECT MIN(id) FROM situation_familiale WHERE libelle = 'Marie(e)'),
	(SELECT MIN(id) FROM nationalite WHERE libelle = 'Français')
WHERE NOT EXISTS (
	SELECT 1 FROM demandeur WHERE nom = 'TEST_SCAN' AND prenom = 'BOB'
);

-- Passeports de test (lies aux demandeurs ci-dessus)
INSERT INTO passport (id_demandeur, numero, date_delivrance, date_expiration)
SELECT
	(SELECT MIN(id) FROM demandeur WHERE nom = 'TEST_SCAN' AND prenom = 'ALICE'),
	'PTESTSCAN001',
	DATE '2024-01-10',
	DATE '2034-01-10'
WHERE NOT EXISTS (
	SELECT 1 FROM passport WHERE numero = 'PTESTSCAN001'
);

INSERT INTO passport (id_demandeur, numero, date_delivrance, date_expiration)
SELECT
	(SELECT MIN(id) FROM demandeur WHERE nom = 'TEST_SCAN' AND prenom = 'BOB'),
	'PTESTSCAN002',
	DATE '2023-06-15',
	DATE '2033-06-15'
WHERE NOT EXISTS (
	SELECT 1 FROM passport WHERE numero = 'PTESTSCAN002'
);

-- Dossier A: reste en DOSSIER_CREE (ideal pour tester le bouton Scanner)
INSERT INTO demande (
	id_demande_type,
	id_demande_donnees_type,
	id_demande_source,
	id_demandeur,
	id_categorie_visa,
	id_passport,
	date_demande,
	lieu,
	date_entree,
	date_expiration,
	motif_demande
)
SELECT
	(SELECT MIN(id) FROM demande_type WHERE code = 'NOUVEAU_TITRE'),
	NULL,
	NULL,
	(SELECT MIN(id) FROM demandeur WHERE nom = 'TEST_SCAN' AND prenom = 'ALICE'),
	(SELECT MIN(id) FROM visa_categorie WHERE code = 'TRAVAILLEUR'),
	(SELECT MIN(id) FROM passport WHERE numero = 'PTESTSCAN001'),
	CURRENT_DATE,
	'Antananarivo',
	CURRENT_DATE,
	CURRENT_DATE + 365,
	'Dossier test sprint3 - scan en attente'
WHERE NOT EXISTS (
	SELECT 1 FROM demande WHERE motif_demande = 'Dossier test sprint3 - scan en attente'
);

-- Dossier B: deja en DOCUMENT_SCANNER + scans fictifs presents
INSERT INTO demande (
	id_demande_type,
	id_demande_donnees_type,
	id_demande_source,
	id_demandeur,
	id_categorie_visa,
	id_passport,
	date_demande,
	lieu,
	date_entree,
	date_expiration,
	motif_demande
)
SELECT
	(SELECT MIN(id) FROM demande_type WHERE code = 'NOUVEAU_TITRE'),
	NULL,
	NULL,
	(SELECT MIN(id) FROM demandeur WHERE nom = 'TEST_SCAN' AND prenom = 'BOB'),
	(SELECT MIN(id) FROM visa_categorie WHERE code = 'INVESTISSEUR'),
	(SELECT MIN(id) FROM passport WHERE numero = 'PTESTSCAN002'),
	CURRENT_DATE,
	'Toamasina',
	CURRENT_DATE,
	CURRENT_DATE + 730,
	'Dossier test sprint3 - scan termine'
WHERE NOT EXISTS (
	SELECT 1 FROM demande WHERE motif_demande = 'Dossier test sprint3 - scan termine'
);

-- Lier les documents requis aux deux dossiers de test
INSERT INTO demande_document (id_demande, id_document)
SELECT
	d.id,
	dcv.id_document
FROM demande d
JOIN document_categorie_visa dcv ON dcv.id_categorie_visa = d.id_categorie_visa
WHERE d.motif_demande IN (
	'Dossier test sprint3 - scan en attente',
	'Dossier test sprint3 - scan termine'
)
AND NOT EXISTS (
	SELECT 1
	FROM demande_document dd
	WHERE dd.id_demande = d.id AND dd.id_document = dcv.id_document
);

-- Statut du dossier A: DOSSIER_CREE
INSERT INTO demande_status (id_demande, id_demande_type_status)
SELECT
	d.id,
	(SELECT MIN(id) FROM demande_type_status WHERE code = 'DOSSIER_CREE')
FROM demande d
WHERE d.motif_demande = 'Dossier test sprint3 - scan en attente'
AND NOT EXISTS (
	SELECT 1
	FROM demande_status ds
	JOIN demande_type_status dts ON dts.id = ds.id_demande_type_status
	WHERE ds.id_demande = d.id AND dts.code = 'DOSSIER_CREE'
);

-- Statut du dossier B: DOCUMENT_SCANNER
INSERT INTO demande_status (id_demande, id_demande_type_status)
SELECT
	d.id,
	(SELECT MIN(id) FROM demande_type_status WHERE code = 'DOCUMENT_SCANNER')
FROM demande d
WHERE d.motif_demande = 'Dossier test sprint3 - scan termine'
AND NOT EXISTS (
	SELECT 1
	FROM demande_status ds
	JOIN demande_type_status dts ON dts.id = ds.id_demande_type_status
	WHERE ds.id_demande = d.id AND dts.code = 'DOCUMENT_SCANNER'
);

-- Scans fictifs sur le dossier B (pour simuler un dossier deja scanne)
INSERT INTO demande_document_scan (
	id_demande_document,
	file_name,
	file_path,
	mime_type,
	file_size
)
SELECT
	dd.id,
	'scan-' || dd.id || '.pdf',
	'uploads/scans/demande-' || dd.id_demande || '/scan-' || dd.id || '.pdf',
	'application/pdf',
	245760
FROM demande_document dd
JOIN demande d ON d.id = dd.id_demande
WHERE d.motif_demande = 'Dossier test sprint3 - scan termine'
AND NOT EXISTS (
	SELECT 1 FROM demande_document_scan dds WHERE dds.id_demande_document = dd.id
);