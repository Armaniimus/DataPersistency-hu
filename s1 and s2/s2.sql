-- S2.1. Vier-daagse cursussen
--
-- Geef code en omschrijving van alle cursussen die precies vier dagen duren.
DROP VIEW IF EXISTS s2_1; CREATE OR REPLACE VIEW s2_1 AS
SELECT code, omschrijving FROM cursussen WHERE lengte = 4;

-- S2.2. Medewerkersoverzicht
--
-- Geef alle informatie van alle medewerkers, gesorteerd op functie,
-- en per functie op leeftijd (van jong naar oud).
DROP VIEW IF EXISTS s2_2; CREATE OR REPLACE VIEW s2_2 AS
SELECT * FROM medewerkers ORDER BY functie, gbdatum DESC

-- S2.3. Door het land
--
-- Welke cursussen zijn in Utrecht en/of in Maastricht uitgevoerd? Geef
-- code en begindatum.
DROP VIEW IF EXISTS s2_3; CREATE OR REPLACE VIEW s2_3 AS
SELECT cursus, begindatum FROM uitvoeringen
WHERE locatie IN ('UTRECHT', 'MAASTRICHT')

-- SELECT code, begindatum FROM cursussen
-- INNER JOIN uitvoeringen
-- ON cursussen.code = uitvoeringen.cursus
-- WHERE locatie IN ('UTRECHT', 'MAASTRICHT')

-- S2.4. Namen
--
-- Geef de naam en voorletters van alle medewerkers, behalve van R. Jansen.
DROP VIEW IF EXISTS s2_4; CREATE OR REPLACE VIEW s2_4 AS
SELECT voorl, naam FROM medewerkers
WHERE NOT (voorl = 'R' AND naam = 'JANSEN')

-- S2.5. Nieuwe SQL-cursus
--
-- Er wordt een nieuwe uitvoering gepland voor cursus S02, en wel op de
-- komende 2 maart. De cursus wordt gegeven in Leerdam door Nick Smit.
-- Voeg deze gegevens toe.
INSERT INTO uitvoeringen(cursus, begindatum, docent, locatie)
VALUES ('S02', '2022-03-01', 7369, 'LEERDAM');
ON CONFLICT DO NOTHING;                                                                                         -- [TEST]



-- S2.6. Stagiairs
--
-- Neem één van je collega-studenten aan als stagiair ('STAGIAIR') en
-- voer zijn of haar gegevens in. Kies een personeelnummer boven de 8000.
INSERT INTO public.medewerkers(mnr, naam, voorl, functie, chef, gbdatum, maandsal, comm, afd)
VALUES (8005, 'DE BRUIN', 'J', 'STAGIAR', 7839, '2000-05-25', 0, null, 10);
ON CONFLICT DO NOTHING;                                                                                         -- [TEST]


-- S2.7. Nieuwe schaal
--
-- We breiden het salarissysteem uit naar zes schalen. Voer een extra schaal in voor mensen die
-- tussen de 3001 en 4000 euro verdienen. Zij krijgen een toelage van 500 euro.
INSERT INTO schalen(snr, ondergrens, bovengrens, toelage)
VALUES (6, 4001, 9999, 500)
ON CONFLICT DO NOTHING;    

UPDATE schalen
SET bovengrens=4000
WHERE snr = 5

                                                                                     -- [TEST]


-- S2.8. Nieuwe cursus
--
-- Er wordt een nieuwe 6-daagse cursus 'Data & Persistency' in het programma opgenomen.
-- Voeg deze cursus met code 'D&P' toe, maak twee uitvoeringen in Leerdam en schrijf drie
-- mensen in.
INSERT INTO cursussen(code, omschrijving, type, lengte)
VALUES ('D&P', 'Data & Persistency', 'BLD', 6)
ON CONFLICT DO NOTHING;
                                                                   -- [TEST]
INSERT INTO uitvoeringen(cursus, begindatum, docent, locatie)
VALUES ('D&P', '2022-10-01', 7369, 'LEERDAM')
ON CONFLICT DO NOTHING;                                                                                         -- [TEST]

INSERT INTO uitvoeringen(cursus, begindatum, docent, locatie)
VALUES ('D&P', '2022-10-07', 7369, 'LEERDAM')
ON CONFLICT DO NOTHING;                                                                                         -- [TEST]

INSERT INTO inschrijvingen(cursist, cursus, begindatum, evaluatie)
VALUES (8005, 'D&P', '2022-10-01', null)
ON CONFLICT DO NOTHING;                                                                                         -- [TEST]
INSERT INTO inschrijvingen(cursist, cursus, begindatum, evaluatie)
VALUES (7876, 'D&P', '2022-10-01', null);
ON CONFLICT DO NOTHING;                                                                                         -- [TEST]
INSERT INTO inschrijvingen(cursist, cursus, begindatum, evaluatie)
VALUES (7839, 'D&P', '2022-10-07', null);
ON CONFLICT DO NOTHING;                                                                                         -- [TEST]


-- S2.9. Salarisverhoging
--
-- De medewerkers van de afdeling VERKOOP krijgen een salarisverhoging
-- van 5.5%, behalve de manager van de afdeling, deze krijgt namelijk meer: 7%.
-- Voer deze verhogingen door.
UPDATE medewerkers
SET maandsal = maandsal*1.05
WHERE afd = 30 AND functie != 'MANAGER';

UPDATE medewerkers
SET maandsal = maandsal*1.07
WHERE afd = 30 AND functie = 'MANAGER';

-- S2.10. Concurrent
--
-- Martens heeft als verkoper succes en wordt door de concurrent
-- weggekocht. Verwijder zijn gegevens.

-- Zijn collega Alders heeft ook plannen om te vertrekken. Verwijder ook zijn gegevens.
-- Waarom lukt dit (niet)?
DELETE FROM medewerkers
WHERE afd = 30 AND naam = 'MARTENS';

DELETE FROM medewerkers
WHERE afd = 30 AND naam = 'ALDERS';
-- kan niet uitgevoerd worden omdat hij nog is ingeschreven voor een cursus
-- en er een beperking in de database zit die dicteerd dat je geen medewerkers mag weggooien die nog ingeschreven staan voor een cursus


-- S2.11. Nieuwe afdeling
--
-- Je wordt hoofd van de nieuwe afdeling 'FINANCIEN' te Leerdam,
-- onder de hoede van De Koning. Kies een personeelnummer boven de 8000.
-- Zorg voor de juiste invoer van deze gegevens.
INSERT INTO medewerkers( mnr, naam, voorl, functie, chef, gbdatum, maandsal, comm, afd)
VALUES (8025, 'VAN ALPHEN', 'A', 'MANAGER', 7839, '1992-07-25', 3000,  null, null)
ON CONFLICT DO NOTHING;

INSERT INTO afdelingen(anr, naam, locatie, hoofd)
VALUES (50, 'FINANCIEN', 'LEERDAM', 8025)
ON CONFLICT DO NOTHING;

UPDATE medewerkers
SET afd = 50
WHERE mnr = 8025
