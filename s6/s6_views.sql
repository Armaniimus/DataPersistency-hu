-- ------------------------------------------------------------------------
-- Data & Persistency
-- Opdracht S6: Views
--
-- (c) 2020 Hogeschool Utrecht
-- Tijmen Muller (tijmen.muller@hu.nl)
-- André Donk (andre.donk@hu.nl)
-- ------------------------------------------------------------------------


-- S6.1.
--
-- 1. Maak een view met de naam "deelnemers" waarmee je de volgende gegevens uit de tabellen inschrijvingen en uitvoering combineert:
--    inschrijvingen.cursist, inschrijvingen.cursus, inschrijvingen.begindatum, uitvoeringen.docent, uitvoeringen.locatie
DROP VIEW IF EXISTS deelnemers CASCADE;
CREATE OR REPLACE VIEW deelnemers AS 
SELECT inschrijvingen.cursist, inschrijvingen.cursus, inschrijvingen.begindatum, uitvoeringen.docent, uitvoeringen.locatie FROM inschrijvingen
INNER JOIN uitvoeringen
ON inschrijvingen.cursus = uitvoeringen.cursus;

-- 2. Gebruik de view in een query waarbij je de "deelnemers" view combineert met de "personeels" view (behandeld in de les):
DROP VIEW IF EXISTS personeel CASCADE;
CREATE OR REPLACE VIEW personeel AS
SELECT mnr, voorl, naam as medewerker, afd, functie, deelnemers.cursus, deelnemers.begindatum, deelnemers.docent, deelnemers.locatie
FROM medewerkers
INNER JOIN deelnemers
ON deelnemers.cursist = medewerkers.mnr;

-- 3. Is de view "deelnemers" updatable ? Waarom ?
-- /** 
-- * nee omdat de software ontwikkelaar van de database dit er niet ingezet heeft voor views die opgebouwd zijn uit joins net als dat je een normale join niet kan updaten.
-- */


-- S6.2.
--
-- 1. Maak een view met de naam "dagcursussen". Deze view dient de gegevens op te halen: 
--      code, omschrijving en type uit de tabel curssussen met als voorwaarde dat de lengte = 1. Toon aan dat de view werkt. 
DROP VIEW IF EXISTS dagcursussen CASCADE;
CREATE OR REPLACE VIEW dagcursussen AS
SELECT cursussen.code, cursussen.omschrijving, cursussen.type 
FROM cursussen
WHERE lengte = 1;

SELECT * FROM dagcursussen;

-- 2. Maak een tweede view met de naam "daguitvoeringen". 
-- Deze view dient de uitvoeringsgegevens op te halen voor de "dagcurssussen" (gebruik ook de view "dagcursussen"). Toon aan dat de view werkt

DROP VIEW IF EXISTS daguitvoeringen CASCADE;
CREATE OR REPLACE VIEW daguitvoeringen AS
SELECT dagcursussen.code AS cursus, dagcursussen.omschrijving, uitvoeringen.docent, uitvoeringen.locatie, uitvoeringen.begindatum, dagcursussen.type
FROM uitvoeringen
INNER JOIN dagcursussen
ON dagcursussen.code = uitvoeringen.cursus;

SELECT * FROM daguitvoeringen;

-- 3. Verwijder de views en laat zien wat de verschillen zijn bij DROP view <viewnaam> CASCADE en bij DROP view <viewnaam> RESTRICT
DROP VIEW IF EXISTS deelnemers CASCADE; 
-- DROP VIEW IF EXISTS personeel RESTRICT; --not needed because dropview personeel cascades its delete to deelnemers
DROP VIEW IF EXISTS daguitvoeringen RESTRICT;
DROP VIEW IF EXISTS dagcursussen RESTRICT;