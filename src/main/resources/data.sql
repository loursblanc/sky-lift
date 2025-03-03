
INSERT INTO sports (name, description, active, season) VALUES 
('SKI', 'Glisse sur neige avec deux skis', TRUE, 'WINTER'),
('Surf', 'Glisse sur snowboard', TRUE, 'WINTER'),
('Snowscoot', 'Glisse avec un engin hybride entre ski et trottinette', TRUE, 'WINTER'),
('Luge', 'Descente sur un support de glisse', TRUE, 'WINTER'),
('X-Wing', 'Sport de glisse ultime inspiré du chasseur stellaire X-Wing. Équipement hybride entre ski et snowboard, permettant une manœuvrabilité exceptionnelle sur les pistes. Conçu pour les pilotes de piste les plus audacieux, le X-Wing offre une stabilité de type aile delta et une maniabilité comparable aux chasseurs de l''Alliance Rebelle. Équipé de fixations adaptables et de carres haute précision, ce sport permet des virages à 90 degrés aussi précis qu''une attaque sur l''Étoile Noire !', TRUE, 'WINTER');


INSERT INTO ski_lifts (name, type, status, comment, commissioning_date) VALUES 
('Sonier', 'TELESIEGE', 'OPEN', 'Télésièges principaux de la station', '1985-12-15'),
('Cascade', 'TELESKI', 'MAINTENANCE', 'Remontée avec vue panoramique', '1990-01-10'),
('Pra Meral', 'TELECABLE', 'OPEN', 'Dessert le versant nord', '2005-12-20'),
('Les Launes', 'TELESIEGE', 'CLOSED', 'Remontée historique du domaine', '1975-12-05'),
('Cime de Valberg', 'FENICULAR', 'OPEN', 'Remontée mécanique unique', '2010-12-15'),
('Goonies', 'TELESIEGE', 'OPEN', 'Un voyage aventureux comme dans le film culte "Les Goonies" où un groupe d''enfants part à la recherche d''un trésor pirate caché. Cette remontée vous emmène dans une quête tout aussi excitante à travers les pistes de ski !', '2015-12-10');

-- Attribution des sports aux remontées mécaniques
-- Sonier avec tous les sports
INSERT INTO ski_lift_sport (ski_lift_id, sport_id)
SELECT 1, id FROM sports;

-- Cascade avec un seul sport (ski)
INSERT INTO ski_lift_sport (ski_lift_id, sport_id)
SELECT 2, id FROM sports WHERE name = 'SKI';

-- Pra Meral avec 3 sports (ski, snowboard, snowscoot)
INSERT INTO ski_lift_sport (ski_lift_id, sport_id)
SELECT 3, id FROM sports WHERE name IN ('SKI', 'Surf', 'Snowscoot');

-- Les Launes avec 2 sports (ski et luge)
INSERT INTO ski_lift_sport (ski_lift_id, sport_id)
SELECT 4, id FROM sports WHERE name IN ('SKI', 'Luge');

-- Cime de Valberg avec snowboard et snowscoot
INSERT INTO ski_lift_sport (ski_lift_id, sport_id)
SELECT 5, id FROM sports WHERE name IN ('Surf', 'Snowscoot');

-- Goonies avec tous les sports sauf luge
INSERT INTO ski_lift_sport (ski_lift_id, sport_id)
SELECT 6, id FROM sports WHERE name IN ('X-Wing');