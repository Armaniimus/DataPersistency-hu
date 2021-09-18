-- S4.1.
-- Geef nummer, functie en geboortedatum van alle medewerkers die vóór 1980
-- geboren zijn, en trainer of verkoper zijn.
DROP VIEW IF EXISTS s4_1; CREATE OR REPLACE VIEW s4_1 AS                                                     -- [TEST]
SELECT mnr, functie, gbdatum FROM medewerkers
WHERE gbdatum < '1980-01-01'
AND (functie = 'VERKOPER' OR functie = 'TRAINER');

-- S4.2.
-- Geef de naam van de medewerkers met een tussenvoegsel (b.v. 'van der').
DROP VIEW IF EXISTS s4_2; CREATE OR REPLACE VIEW s4_2 AS                                                     -- [TEST]
SELECT naam FROM medewerkers
WHERE naam LIKE '% %';

-- S4.3.
-- Geef nu code, begindatum en aantal inschrijvingen (`aantal_inschrijvingen`) van alle
-- cursusuitvoeringen in 2019 met minstens drie inschrijvingen.
DROP VIEW IF EXISTS s4_3; CREATE OR REPLACE VIEW s4_3 AS                                                     -- [TEST]
SELECT cursus as code, begindatum, count(cursus) as aantal_inschrijvingen
FROM inschrijvingen
GROUP BY code, begindatum
HAVING count(cursus) >= 3 AND date_part('year', begindatum) = '2019';

-- S4.4.
-- Welke medewerkers hebben een bepaalde cursus meer dan één keer gevolgd?
-- Geef medewerkernummer en cursuscode.
DROP VIEW IF EXISTS s4_4; CREATE OR REPLACE VIEW s4_4 AS                                                     -- [TEST]
SELECT cursist, cursus FROM inschrijvingen
GROUP BY cursist, cursus
HAVING count(cursus) > 1;

-- S4.5.
-- Hoeveel uitvoeringen (`aantal`) zijn er gepland per cursus?
-- Een voorbeeld van het mogelijke resultaat staat hieronder.
--
--   cursus | aantal
--  --------+-----------
--   ERM    | 1
--   JAV    | 4
--   OAG    | 2
DROP VIEW IF EXISTS s4_5; CREATE OR REPLACE VIEW s4_5 AS                                                     -- [TEST]
SELECT cursus, count(cursus) as aantal
FROM uitvoeringen
GROUP BY cursus;

-- S4.6.
-- Bepaal hoeveel jaar leeftijdsverschil er zit tussen de oudste en de
-- jongste medewerker (`verschil`) en bepaal de gemiddelde leeftijd van
-- de medewerkers (`gemiddeld`).
-- Je mag hierbij aannemen dat elk jaar 365 dagen heeft.
-- DROP VIEW IF EXISTS s4_6; CREATE OR REPLACE VIEW s4_6 AS                                                     -- [TEST]
SELECT
    ( max(gbdatum) - min(gbdatum) ) / 365 AS verschil,
	avg(current_date - gbdatum) / 365 AS gemiddeld
FROM medewerkers;

-- S4.7.
-- Geef van het hele bedrijf een overzicht van het aantal medewerkers dat
-- er werkt (`aantal_medewerkers`), de gemiddelde commissie die ze
-- krijgen (`commissie_medewerkers`), en hoeveel dat gemiddeld
-- per verkoper is (`commissie_verkopers`).
DROP VIEW IF EXISTS s4_7; CREATE OR REPLACE VIEW s4_7 AS                                                     -- [TEST]
SELECT sum(1) AS aantal_medewerkers,
    sum(comm) / sum(1) AS commissie_medewerkers,
    sum(comm) / sum(case when functie = 'VERKOPER' then 1 else 0 end) AS commissie_verkopers
FROM medewerkers;


-- -------------------------[ HU TESTRAAMWERK ]--------------------------------
-- Met onderstaande query kun je je code testen. Zie bovenaan dit bestand
-- voor uitleg.

SELECT * FROM test_select('S4.1') AS resultaat
UNION
SELECT * FROM test_select('S4.2') AS resultaat
UNION
SELECT * FROM test_select('S4.3') AS resultaat
UNION
SELECT * FROM test_select('S4.4') AS resultaat
UNION
SELECT * FROM test_select('S4.5') AS resultaat
UNION
SELECT 'S4.6 wordt niet getest: geen test mogelijk.' AS resultaat
UNION
SELECT * FROM test_select('S4.7') AS resultaat
ORDER BY resultaat;