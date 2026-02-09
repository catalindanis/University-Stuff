CREATE DATABASE Practic;
use Practic;

CREATE TABLE Adulti(
	AId INT PRIMARY KEY IDENTITY,
	Nume VARCHAR(50),
	Prenume VARCHAR(50),
	Varsta INT,
	Gen INT
);

INSERT INTO Adulti(Nume, Prenume, Varsta, Gen) VALUES
('Danis', 'Catalin', 20, 2),
('Miclea', 'Alex', 23, 2),
('Minea', 'Catrinel', 18, 1),
('Crisan', 'Carina', 25, 1);

SELECT * FROM Adulti;

CREATE TABLE Faini(
	FId INT PRIMARY KEY IDENTITY,
	Denumire VARCHAR(50),
	Cantitate INT,
	Gluten BIT,
	Descriere VARCHAR(50)
);

INSERT INTO Faini(Denumire, Cantitate, Gluten, Descriere) VALUES
('Faina alba', 13, 1, 'O descriere pentru faina alba'),
('Faina de grau', 22, 1, 'O descriere pentru faina de grau'),
('Faina de malai', 40, 0, 'O descriere pentru faina de malai'),
('Faina de secara', 5, 0, 'O descriere pentru faina de secara');

SELECT * FROM Faini;

CREATE TABLE Paini(
	PId INT PRIMARY KEY IDENTITY,
	Denumire VARCHAR(50),
	Gramaj INT,
	Pret FLOAT,
	FId INT FOREIGN KEY REFERENCES Faini(FId)
);

INSERT INTO Paini(Denumire, Gramaj, Pret, FId) VALUES
('Paine alba', 900, 9.5, 1),
('Paine neagra', 400, 11.2, 2),
('Paine integrala', 600, 7.8, 3),
('Paine cu cartofi', 750, 13.4, 4);

SELECT * FROM Paini;

CREATE TABLE Palanete(
	PId INT PRIMARY KEY IDENTITY,
	Denumire VARCHAR(50),
	Umplutura VARCHAR(50),
	Gramaj INT,
	Pret FLOAT,
	AId INT FOREIGN KEY REFERENCES Adulti(AId)
);

INSERT INTO Palanete(Denumire, Umplutura, Gramaj, Pret, AId) VALUES
('Palanet cu branza', 'Branza', 350, 6.5, 1),
('Palanet cu sunca', 'Sunca', 400, 8.9, 2),
('Palanet cu varza', 'Varza', 280, 7, 3),
('Palanet cu visine', 'Visine', 320, 7.5, 4);

SELECT * FROM Palanete;

CREATE TABLE AdultPaine(
	AId INT FOREIGN KEY REFERENCES Adulti(AId),
	PId INT FOREIGN KEY REFERENCES Paini(PId),
	CONSTRAINT PK_AP PRIMARY KEY(AId, PId),
	Cantitate INT,
	Pret FLOAT,
	DataExpirare DATE
);

SELECT * FROM AdultPaine;

CREATE OR ALTER PROCEDURE AdaugaPaine
@PId INT,
@AId INT,
@Cantitate INT,
@Pret FLOAT,
@DataExpirare DATE
AS
BEGIN
	IF EXISTS (SELECT * FROM AdultPaine AP WHERE AP.AId = @AId AND AP.PId = @PId)
	BEGIN
		UPDATE AdultPaine SET Cantitate=@Cantitate, Pret=@Pret, DataExpirare=@DataExpirare 
		WHERE AId = @AId AND PId = @PId;
		RETURN
	END

	INSERT INTO AdultPaine(AId, PId, Cantitate, Pret, DataExpirare) VALUES
	(@AId, @PId, @Cantitate, @Pret, @DataExpirare);
END

EXEC AdaugaPaine 1, 4, 4, 10.5, "2026-01-13";
EXEC AdaugaPaine 3, 2, 4, 24.5, "2026-01-04";

SELECT * FROM AdultPaine;

CREATE OR ALTER FUNCTION AdultiCantitateMedie()
RETURNS TABLE
AS
RETURN (
    SELECT A.Nume, A.Prenume, AP.Cantitate
    FROM Adulti A
    INNER JOIN AdultPaine AP ON A.AId = AP.AId
    WHERE AP.Cantitate = (
        SELECT AVG(Cantitate)
        FROM AdultPaine
    )
);

SELECT * FROM dbo.AdultiCantitateMedie()
