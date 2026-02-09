CREATE DATABASE ProduseMagazin;
use ProduseMagazin;

CREATE TABLE Localitati(
	LId INT PRIMARY KEY IDENTITY,
	Strada VARCHAR(50),
	Numar INT,
	CodPostal INT
);

INSERT INTO Localitati(Strada, Numar, CodPostal) VALUES
('Calea Victoriei', 124, 401058),
('B.P. Hasdeu', 90, 401023),
('Ploiesti', 26, 500134),
('Calea Motilor', 10, 310492);

CREATE TABLE Clienti(
	CId INT PRIMARY KEY IDENTITY,
	Nume VARCHAR(50),
	Prenume VARCHAR(50),
	Gen INT,
	DataNastere DATE
);

INSERT INTO Clienti(Nume, Prenume, Gen, DataNastere) VALUES
('Danis', 'Catalin', 1, '2025-10-21 09:00:00'),
('Silaghi', 'Alin', 1, '2024-09-13 19:00:00'),
('Valean', 'Eric', 1, '2020-05-30 14:00:00'),
('Borza', 'Alex', 1, '2022-01-02 17:30:00');

CREATE TABLE Magazine(
	MId INT PRIMARY KEY IDENTITY,
	Denumire VARCHAR(50),
	AnDeschidere INT,
	LId INT FOREIGN KEY REFERENCES Localitati(LId)
);

INSERT INTO Magazine(Denumire, AnDeschidere, LId) VALUES
('DM', 2000, 1),
('H&M', 2010, 2),
('LC Waikiki', 2015, 3),
('Kaufland', 2007, 4);

CREATE TABLE ProduseFavorite(
	PFId INT PRIMARY KEY IDENTITY,
	Denumire VARCHAR(50),
	Pret FLOAT,
	Reducere INT,
	CId INT FOREIGN KEY REFERENCES Clienti(CId)
);

INSERT INTO ProduseFavorite(Denumire, Pret, Reducere, CId) VALUES
('Bratara', 100, 5, 5),
('Minge', 140, 10, 6),
('Salam', 190, 15),
('Cauciuc', 300, 20);

INSERT INTO ProduseFavorite(Denumire, Pret, Reducere, CId) VALUES
('Parizer', 230, 10, 5);

CREATE TABLE ClientMagazin(
	CId INT FOREIGN KEY REFERENCES Clienti(CId),
	MId INT FOREIGN KEY REFERENCES Magazine(MId),
	CONSTRAINT PK_CM PRIMARY KEY (CId, MId),
	DataCumparaturi DATE,
	Pret FLOAT
);

CREATE OR ALTER PROCEDURE UpsertCumparatura
@CId INT,
@MId INT,
@DataCumparaturi DATE,
@Pret FLOAT
AS
BEGIN
	IF EXISTS (SELECT * FROM ClientMagazin CM WHERE CM.CId=@CId AND CM.MId=@MId)
	BEGIN
		UPDATE ClientMagazin SET Pret=@Pret, DataCumparaturi=@DataCumparaturi WHERE MId = @MId AND CId = @CId;
		RETURN
	END

	INSERT INTO ClientMagazin(Cid, Mid, DataCumparaturi, Pret) VALUES
	(@Cid, @Mid, @DataCumparaturi, @Pret);
END

CREATE OR ALTER VIEW AfisareClientiMax3Prod AS
SELECT 
    C.CId,
    C.Nume,
    C.Prenume,
    C.Gen,
    C.DataNastere,
	COUNT(PF.CId) AS NrProduseFavorite
FROM Clienti AS C
INNER JOIN ProduseFavorite AS PF ON PF.CId = C.CId
GROUP BY 
    C.CId,
    C.Nume,
    C.Prenume,
    C.Gen,
    C.DataNastere
HAVING COUNT(PF.CId) <= 3;

SELECT * FROM AfisareClientiMax3Prod;

EXEC UpsertCumparatura 5, 2, "2026-10-21 09:00:00", 250;
SELECT * FROM ClientMagazin;

CREATE OR ALTER VIEW CompetitiiCuCeiMaiMultiParticipanti AS
SELECT C.Denumire
FROM Competitii C
JOIN Participari P ON P.CompId = C.CompId
GROUP BY C.CompId, C.Denumire
HAVING COUNT(P.PartId) = (
    SELECT MAX(NrParticipanti)
    FROM (
        SELECT COUNT(P2.PartId) AS NrParticipanti
        FROM Participari P2
        GROUP BY P2.CompId
    ) AS T
);

CREATE OR ALTER FUNCTION MasiniCuPeste3Curse()
RETURNS TABLE
AS
RETURN
(
    SELECT 
        M.MasinaId,
        M.NrInmatriculare,
        M.Brand,
        M.Model,
        COUNT(MC.CursaId) AS NrCurse
    FROM Masini M
    JOIN MasinaCursa MC ON MC.MasinaId = M.MasinaId
    GROUP BY 
        M.MasinaId,
        M.NrInmatriculare,
        M.Brand,
        M.Model
    HAVING COUNT(MC.CursaId) > 3
);
