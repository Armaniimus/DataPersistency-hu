-- S1.1. Geslacht
ALTER TABLE medewerkers ADD geslacht char(1);
ALTER TABLE medewerkers ADD CONSTRAINT m_geslacht_chk CHECK(geslacht in ('M', 'V'));

-- S1.2. Nieuwe afdeling
INSERT INTO medewerkers(mnr, naam, voorl, functie, chef, gbdatum, maandsal, comm, afd, geslacht)
VALUES (8000, 'Donk', 'A', 'R&D', 7839, '1995-09-15', 1500, null, null, 'M');

INSERT INTO afdelingen(	anr, naam, locatie, hoofd) VALUES (50, 'ONDERZOEK', 'ZWOLLE', 8000);

UPDATE medewerkers SET afd = 50 WHERE mnr = 8000

-- S1.3. Verbetering op afdelingentabel
ALTER TABLE afdelingen ADD CONSTRAINT a_anr_chk2 CHECK (mod(anr, 10) = 0);

INSERT INTO afdelingen(	anr, naam, locatie, hoofd) VALUES (60, 'RETAIL', 'AMSTERDAM', 7839);
INSERT INTO afdelingen(	anr, naam, locatie, hoofd) VALUES (70, 'HANDEL', 'UTRECHT', 7839);
INSERT INTO afdelingen(	anr, naam, locatie, hoofd) VALUES (80, 'COMMUNICATIE', 'EINDHOVEN', 7839);
INSERT INTO afdelingen(	anr, naam, locatie, hoofd) VALUES (90, 'INTERNATIONAAL', 'LEIDEN', 7839);

ALTER TABLE afdelingen ALTER COLUMN anr TYPE NUMERIC(4,0);

-- S1.4. Adressen
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
ALTER TABLE medewerkers ADD CONSTRAINT m_verkoopcomm_chk CHECK(
	( comm IS NOT NULL AND functie = 'VERKOPER' ) OR
	( comm IS NULL AND functie != 'VERKOPER' )
);
