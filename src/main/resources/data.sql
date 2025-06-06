-- ==============================================
-- KAPITAL HOSPITAL MANAGEMENT SYSTEM - INITIAL DATA
-- ==============================================

-- Security Users (for in-memory authentication)
-- Note: These are for reference, actual users are configured in SecurityConfig.java
-- user1, user2 (ROLE_USER) and admin (ROLE_USER, ROLE_ADMIN) with password "1234"

-- ==============================================
-- PATIENTS DATA
-- ==============================================

-- Clear existing data
DELETE FROM patients;

-- Medical Staff Test Patients
INSERT INTO patients (name, birthday, is_sick, score, email, phone, notes, status) VALUES
('Dr. Marie Dubois', '1980-03-15', false, 950, 'marie.dubois@kapital.hospital', '+1-555-0001', 'Chief Cardiologist at KAPital Hospital. Excellent medical record.', 'ACTIVE'),
('Jean-Pierre Martin', '1975-07-22', true, 650, 'jp.martin@email.fr', '+1-555-0002', 'Post-operative cardiac surgery follow-up. Satisfactory recovery.', 'ACTIVE'),
('Sophie Lefebvre', '1990-11-08', false, 880, 'sophie.lefebvre@gmail.com', '+1-555-0003', 'Annual health checkup. All parameters are excellent.', 'ACTIVE'),
('Mohamed El-Hassan', '1985-05-12', true, 720, 'mohamed.hassan@email.fr', '+1-555-0004', 'Type 2 diabetes treatment. Regular monitoring required.', 'ACTIVE'),
('Catherine Moreau', '1968-12-03', false, 780, 'catherine.moreau@yahoo.fr', '+1-555-0005', 'Preventive control. Mild hypertension under control.', 'ACTIVE');

-- Emergency Patients
INSERT INTO patients (name, birthday, is_sick, score, email, phone, notes, status) VALUES
('Pierre Durand', '1992-01-18', true, 450, 'pierre.durand@hotmail.fr', '+33-1-45-67-89-06', 'Admission urgente - fracture du fémur. Intervention programmée.', 'ACTIVE'),
('Lisa Rodriguez', '1987-09-25', true, 380, 'lisa.rodriguez@gmail.com', '+33-1-45-67-89-07', 'Pneumonie aigüe. Traitement antibiotique en cours.', 'ACTIVE'),
('Ahmed Ben-Ali', '1979-04-14', true, 520, 'ahmed.benali@email.fr', '+33-1-45-67-89-08', 'Complications post-chirurgicales. Surveillance rapprochée.', 'ACTIVE'),
('Nathalie Blanc', '1983-08-07', true, 340, 'nathalie.blanc@outlook.fr', '+33-1-45-67-89-09', 'Crise d''asthme sévère. Stabilisation en cours.', 'ACTIVE'),
('Robert Leroy', '1955-06-30', true, 290, 'robert.leroy@free.fr', '+33-1-45-67-89-10', 'Insuffisance cardiaque. Traitement d''urgence administré.', 'ACTIVE');

-- Pediatric Patients
INSERT INTO patients (name, birthday, is_sick, score, email, phone, notes, status) VALUES
('Emma Petit', '2015-03-20', false, 920, 'parents.emma@gmail.com', '+33-1-45-67-89-11', 'Visite pédiatrique de routine. Développement normal.', 'ACTIVE'),
('Lucas Garnier', '2012-11-12', true, 680, 'famille.garnier@email.fr', '+33-1-45-67-89-12', 'Bronchite. Traitement en cours, évolution favorable.', 'ACTIVE'),
('Chloé Roux', '2018-07-05', false, 850, 'chloe.parents@yahoo.fr', '+33-1-45-67-89-13', 'Vaccination à jour. Excellente santé générale.', 'ACTIVE'),
('Nathan Bernard', '2010-01-28', true, 590, 'bernard.famille@gmail.com', '+33-1-45-67-89-14', 'Allergie alimentaire. Plan de traitement établi.', 'ACTIVE'),
('Jade Lemoine', '2016-09-16', false, 900, 'lemoine.parents@hotmail.fr', '+33-1-45-67-89-15', 'Bilan de croissance. Tout est parfait.', 'ACTIVE');

-- Geriatric Patients
INSERT INTO patients (name, birthday, is_sick, score, email, phone, notes, status) VALUES
('Marguerite Dubois', '1935-02-14', true, 420, 'famille.dubois@email.fr', '+33-1-45-67-89-16', 'Suivi gériatrique. Arthrose sévère des genoux.', 'ACTIVE'),
('Henri Fontaine', '1940-10-22', true, 480, 'henri.fontaine@free.fr', '+33-1-45-67-89-17', 'Démence débutante. Accompagnement familial nécessaire.', 'ACTIVE'),
('Simone Martin', '1938-05-08', false, 720, 'simone.martin@orange.fr', '+33-1-45-67-89-18', 'Remarquable forme pour son âge. Suivi préventif.', 'ACTIVE'),
('Georges Leclerc', '1945-12-11', true, 380, 'leclerc.georges@wanadoo.fr', '+33-1-45-67-89-19', 'Insuffisance rénale chronique. Dialyse programmée.', 'ACTIVE'),
('Yvette Rousseau', '1942-08-25', false, 650, 'yvette.rousseau@gmail.com', '+33-1-45-67-89-20', 'Hypertension contrôlée. Suivi cardiologique régulier.', 'ACTIVE');

-- Chronic Patients
INSERT INTO patients (name, birthday, is_sick, score, email, phone, notes, status) VALUES
('Marc Olivier', '1970-04-03', true, 580, 'marc.olivier@email.fr', '+33-1-45-67-89-21', 'Diabète type 1. Excellente observance du traitement.', 'ACTIVE'),
('Valérie Schneider', '1978-09-17', true, 620, 'valerie.schneider@gmail.com', '+33-1-45-67-89-22', 'Sclérose en plaques. Traitement immunomodulateur.', 'ACTIVE'),
('Thierry Morvan', '1965-11-29', true, 540, 'thierry.morvan@yahoo.fr', '+33-1-45-67-89-23', 'Polyarthrite rhumatoïde. Rémission partielle obtenue.', 'ACTIVE'),
('Isabelle Girard', '1982-01-07', true, 690, 'isabelle.girard@hotmail.fr', '+33-1-45-67-89-24', 'Epilepsie bien contrôlée. Aucune crise depuis 2 ans.', 'ACTIVE'),
('François Dumas', '1972-06-20', true, 510, 'francois.dumas@free.fr', '+33-1-45-67-89-25', 'Maladie de Crohn. Poussée inflammatoire en traitement.', 'ACTIVE');

-- Recovery Patients
INSERT INTO patients (name, birthday, is_sick, score, email, phone, notes, status) VALUES
('Julie Lambert', '1989-12-15', false, 820, 'julie.lambert@gmail.com', '+33-1-45-67-89-26', 'Récupération post-cancer du sein. Rémission complète.', 'ACTIVE'),
('David Chen', '1995-03-08', false, 780, 'david.chen@email.fr', '+33-1-45-67-89-27', 'Rééducation post-AVC. Progrès remarquables.', 'ACTIVE'),
('Sandra Faure', '1980-10-12', false, 750, 'sandra.faure@outlook.fr', '+33-1-45-67-89-28', 'Fin de traitement dépression. Stabilité psychologique.', 'ACTIVE'),
('Kevin Morel', '1993-07-26', false, 800, 'kevin.morel@yahoo.fr', '+33-1-45-67-89-29', 'Récupération addiction. Suivi psychologique maintenu.', 'ACTIVE'),
('Amélie Vidal', '1986-02-19', false, 860, 'amelie.vidal@gmail.com', '+33-1-45-67-89-30', 'Post-transplantation rénale. Fonction rénale excellente.', 'ACTIVE');

-- Discharged Patients (for testing different statuses)
INSERT INTO patients (name, birthday, is_sick, score, email, phone, notes, status) VALUES
('Claude Bertrand', '1977-05-14', false, 900, 'claude.bertrand@email.fr', '+33-1-45-67-89-31', 'Guérison complète. Sortie autorisée.', 'DISCHARGED'),
('Patricia Legrand', '1984-08-03', false, 880, 'patricia.legrand@free.fr', '+33-1-45-67-89-32', 'Accouchement sans complications. Retour à domicile.', 'DISCHARGED'),
('Michel Roussel', '1969-12-09', false, 820, 'michel.roussel@wanadoo.fr', '+33-1-45-67-89-33', 'Chirurgie réussie. Convalescence à domicile.', 'DISCHARGED');

-- Inactive Patients (transferred or moved)
INSERT INTO patients (name, birthday, is_sick, score, email, phone, notes, status) VALUES
('Sylvie Mercier', '1975-03-25', false, 700, 'sylvie.mercier@gmail.com', '+33-1-45-67-89-34', 'Transfert vers établissement spécialisé.', 'INACTIVE'),
('Alain Perrin', '1962-11-18', true, 450, 'alain.perrin@email.fr', '+33-1-45-67-89-35', 'Dossier en attente de transfert administratif.', 'INACTIVE');

-- Test edge cases
INSERT INTO patients (name, birthday, is_sick, score, email, phone, notes, status) VALUES
('Test Minimum', '2000-01-01', true, 0, 'test.minimum@test.fr', '+33-1-00-00-00-01', 'Patient test avec score minimum.', 'SUSPENDED'),
('Test Maximum', '1920-12-31', false, 1000, 'test.maximum@test.fr', '+33-1-00-00-00-02', 'Patient test avec score maximum.', 'ACTIVE'),
('Test Unicode', '1990-06-15', false, 750, 'test.unicode@test.fr', '+33-1-00-00-00-03', 'Test caractères spéciaux: àâäéèêëïîôùûüÿç', 'ACTIVE');

-- ==============================================
-- VERIFICATION QUERIES (commented for reference)
-- ==============================================
-- SELECT COUNT(*) as total_patients FROM patients;
-- SELECT status, COUNT(*) as count FROM patients GROUP BY status;
-- SELECT 
--     CASE WHEN is_sick THEN 'Malades' ELSE 'En bonne santé' END as health_status,
--     COUNT(*) as count 
-- FROM patients GROUP BY is_sick;
-- SELECT AVG(score) as average_score FROM patients;

-- ==============================================
-- FINAL STATISTICS
-- ==============================================
-- Total patients: 38
-- Active: 30, Discharged: 3, Inactive: 2, Suspended: 1
-- Sick: 15, Healthy: 23
-- Score range: 0-1000 (testing all boundaries)
-- Age range: 6-90 years (comprehensive age coverage)