
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

INSERT INTO demande_type_status (code, libelle) VALUES
('DOSSIER_CREE', 'Dossier cree');

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