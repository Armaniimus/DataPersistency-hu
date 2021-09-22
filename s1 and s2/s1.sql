-- S1.1. Geslacht
--
-- Voeg een kolom `geslacht` toe aan de medewerkerstabel.
-- Voeg ook een beperkingsregel `m_geslacht_chk` toe aan deze kolom,
-- die ervoor zorgt dat alleen 'M' of 'V' als geldige waarde wordt
-- geaccepteerd. Test deze regel en neem de gegooide foutmelding op als
-- commentaar in de uitwerking.
ALTER TABLE medewerkers ADD geslacht char(1);
ALTER TABLE medewerkers ADD CONSTRAINT m_geslacht_chk CHECK(geslacht in ('M', 'V'));


-- S1.2. Nieuwe afdeling
--
-- Het bedrijf krijgt een nieuwe onderzoeksafdeling 'ONDERZOEK' in Zwolle.
-- Om de onderzoeksafdeling op te zetten en daarna te leiden wordt de
-- nieuwe medewerker A DONK aangenomen. Hij krijgt medewerkersnummer 8000
-- en valt direct onder de directeur.
-- Voeg de nieuwe afdeling en de nieuwe medewerker toe aan de database.
INSERT INTO medewerkers(mnr, naam, voorl, functie, chef, gbdatum, maandsal, comm, afd, geslacht)
VALUES (8000, 'Donk', 'A', 'R&D', 7839, '1995-09-15', 1500, null, null, 'M');

INSERT INTO afdelingen(	anr, naam, locatie, hoofd) VALUES (50, 'ONDERZOEK', 'ZWOLLE', 8000);

UPDATE medewerkers SET afd = 50 WHERE mnr = 8000


-- S1.3. Verbetering op afdelingentabel
--
-- We gaan een aantal verbeteringen doorvoeren aan de tabel `afdelingen`:
--   a) Maak een sequence die afdelingsnummers genereert. Denk aan de beperking
--      dat afdelingsnummers veelvouden van 10 zijn.
--   b) Voeg een aantal afdelingen toe aan de tabel, maak daarbij gebruik van
--      de nieuwe sequence.
--   c) Op enig moment gaat het mis. De betreffende kolommen zijn te klein voor
--      nummers van 3 cijfers. Los dit probleem op.

DROP SEQUENCE IF EXISTS anr ; CREATE SEQUENCE anr START 50 INCREMENT 10 MINVALUE 0;
ALTER TABLE afdelingen DROP CONSTRAINT IF EXISTS a_anr_chk2 ;
ALTER TABLE afdelingen ADD CONSTRAINT a_anr_chk2 CHECK (mod(anr, 10) = 0);
ALTER TABLE afdelingen ALTER COLUMN anr TYPE NUMERIC(4,0);

INSERT INTO afdelingen(anr, naam, locatie, hoofd) VALUES (nextval('anr'), 'RETAIL', 'AMSTERDAM', 7839);
INSERT INTO afdelingen(anr, naam, locatie, hoofd) VALUES (nextval('anr'), 'HANDEL', 'UTRECHT', 7839);
INSERT INTO afdelingen(anr, naam, locatie, hoofd) VALUES (nextval('anr'), 'COMMUNICATIE', 'EINDHOVEN', 7839);
INSERT INTO afdelingen(anr, naam, locatie, hoofd) VALUES (nextval('anr'), 'INTERNATIONAAL', 'LEIDEN', 7839);

--reset s1.3
-- DROP SEQUENCE IF EXISTS anr ;
-- ALTER TABLE afdelingen DROP CONSTRAINT IF EXISTS a_anr_chk2 ;
-- ALTER TABLE afdelingen ALTER COLUMN anr TYPE NUMERIC(2,0);
-- DELETE FROM afdelingen WHERE anr > 40;

-- S1.4. Adressen
--
-- Maak een tabel `adressen`, waarin de adressen van de medewerkers worden
-- opgeslagen (inclusief adreshistorie). De tabel bestaat uit onderstaande
-- kolommen. Voeg minimaal één rij met adresgegevens van A DONK toe.
--
--    postcode      PK, bestaande uit 6 karakters (4 cijfers en 2 letters)
--    huisnummer    PK
--    ingangsdatum  PK
--    einddatum     moet na de ingangsdatum liggen
--    telefoon      10 cijfers, uniek
--    med_mnr       FK, verplicht
CREATE TABLE IF NOT EXISTS adressen (
    postcode character varying(6),
    huisnummer integer,
    ingangsdatum date,
    einddatum date,
    telefoon numeric(10, 0),
    med_mnr integer NOT NULL,
    PRIMARY KEY (postcode, huisnummer, ingangsdatum),
    CONSTRAINT adres_tel_unique UNIQUE (telefoon),
    CONSTRAINT "Adres_eindDatum_check" CHECK (ingangsdatum < einddatum)
);
INSERT INTO adressen(postcode, huisnummer, ingangsdatum, einddatum, telefoon, med_mnr)
VALUES ('3494FE', 98, '2021-01-01', '2022-01-01', 0612345678, 8000)

-- S1.5. Commissie
--
-- De commissie van een medewerker (kolom `comm`) moet een bedrag bevatten als de medewerker een functie als
-- 'VERKOPER' heeft, anders moet de commissie NULL zijn. Schrijf hiervoor een beperkingsregel. 

ALTER TABLE medewerkers ADD CONSTRAINT m_verkoopcomm_chk CHECK(
	( comm IS NOT NULL AND functie = 'VERKOPER' ) OR
	( comm IS NULL AND functie != 'VERKOPER' )
);

-- -------------------------[ HU TESTRAAMWERK ]--------------------------------
-- Met onderstaande query kun je je code testen. Zie bovenaan dit bestand
-- voor uitleg.

SELECT * FROM test_exists('S1.1', 1) AS resultaat
UNION
SELECT * FROM test_exists('S1.2', 1) AS resultaat
UNION
SELECT 'S1.3 wordt niet getest: geen test mogelijk.' AS resultaat
UNION
SELECT * FROM test_exists('S1.4', 6) AS resultaat
UNION
SELECT 'S1.5 wordt niet getest: handmatige test beschikbaar.' AS resultaat
ORDER BY resultaat;


-- Draai alle wijzigingen terug om conflicten in komende opdrachten te voorkomen.
DROP TABLE IF EXISTS adressen;
UPDATE medewerkers SET afd = NULL WHERE mnr < 7369 OR mnr > 7934;
UPDATE afdelingen SET hoofd = NULL WHERE anr > 40;
DELETE FROM afdelingen WHERE anr > 40;
DELETE FROM medewerkers WHERE mnr < 7369 OR mnr > 7934;
ALTER TABLE medewerkers DROP CONSTRAINT IF EXISTS m_geslacht_chk;
ALTER TABLE medewerkers DROP COLUMN IF EXISTS geslacht;

