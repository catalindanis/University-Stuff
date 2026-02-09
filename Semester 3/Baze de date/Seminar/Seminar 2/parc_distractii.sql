CREATE DATABASE ParcDistractii;

USE ParcDistractii;

CREATE TABLE Sectiuni
	(Sid INT PRIMARY KEY IDENTITY,
	Nume varchar(50) NOT NULL,
	Descriere varchar(50) DEFAULT '');

CREATE TABLE Atractii
	(Aid INT PRIMARY KEY IDENTITY,
	Nume varchar(50) NOT NULL,
	Descriere varchar(50) DEFAULT '',
	VarstaMinima INT NOT NULL,
	Sid INT FOREIGN KEY REFERENCES Sectiuni(Sid));

CREATE TABLE CategoriiVizitatori
	(CVid INT PRIMARY KEY IDENTITY);

ALTER TABLE CategoriiVizitatori
ADD Nume varchar(50) NOT NULL;

CREATE TABLE Vizitatori
	(Vid INT PRIMARY KEY,
	Nume varchar(50) NOT NULL,
	Email varchar(50) NOT NULL,
	CVid INT FOREIGN KEY REFERENCES CategoriiVizitatori(CVid));

CREATE TABLE VizitatorAtractie
	(Vid INT FOREIGN KEY REFERENCES Vizitatori(Vid),
	Aid INT FOREIGN KEY REFERENCES Atractii(Aid),
	CONSTRAINT VAid PRIMARY KEY (Vid, Aid));

ALTER TABLE VizitatorAtractie 
ADD Nota REAL CHECK(Nota >= 1 AND Nota <= 10);

INSERT INTO Sectiuni VALUES
('sectiunea1', 'descrierea1');

INSERT INTO Sectiuni VALUES
('sectiunea2', 'descrierea2'),
('sectiunea3', 'descrierea3');

INSERT INTO Sectiuni VALUES
('sectiunea4', 'descrierea4'),
('sectiunea5', 'descrierea5'),
('sectiunea6', 'descrierea6'),
('sectiunea7', 'descrierea7');


INSERT INTO Atractii VALUES
('atractie1', 'descriere1', 14, 1),
('atractie2', 'descriere2', 12, 2),
('atractie3', 'descriere3', 18, 3),
('atractie4', 'descriere4', 6, 1);

INSERT INTO Atractii VALUES
('atractie5', 'descriere5', 14, 7),
('atractie6', 'descriere6', 0, 4),
('atractie7', 'descriere7', 24, 5);

INSERT INTO CategoriiVizitatori VALUES
('categorie1'),
('categorie2'),
('categorie3'),
('categorie4'),
('categorie5'),
('categorie6'),
('categorie7');

INSERT INTO Vizitatori (Nume, Email, CVid) VALUES
('nume1', 'email1', 1),
('nume2', 'email2', 5),
('nume3', 'email3', 5),
('nume4', 'email4', 7),
('nume5', 'email5', 4),
('nume6', 'email6', 3),
('nume7', 'email7', 1);

INSERT INTO VizitatorAtractie (Vid, Aid, Nota) VALUES
(1, 4, 7.4),
(2, 8, 5.5),
(2, 7, 3.2),
(3, 9, 6),
(2, 10, 8.4),
(1, 5, 7.5),
(6, 7, 9.8);

SELECT * FROM Atractii;

UPDATE Sectiuni SET descriere='descriere_2 new'
WHERE nume='sectiunea2';

UPDATE Atractii SET nume='nume_4 new'
WHERE nume='atractie4';

UPDATE CategoriiVizitatori SET nume='categorie_vizitatori new'
WHERE nume='categorie7';

UPDATE Vizitatori SET email='myEmail@example.com', nume='myName'
WHERE nume='nume1';

DELETE FROM VizitatorAtractie 
WHERE Vid=1 AND Aid=4;

DELETE FROM Atractii
WHERE nume='atractie2';