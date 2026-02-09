USE ParcDistractii;

GO
-- CREATE OR ALTER PROCEDURE
CREATE PROCEDURE InsertSection @nume VARCHAR(50), @descriere VARCHAR(50)
AS
BEGIN
INSERT INTO Sectiuni(Nume, Descriere) VALUES
(@nume, @descriere);
END;
GO

SELECT * FROM Sectiuni;

Exec InsertSection 'sectiunea8', 'descrierea8';

GO
CREATE PROCEDURE UpdateEmail @Vid INT, @NewEmail VARCHAR(50)
AS
BEGIN
UPDATE Vizitatori SET Email=@NewEmail
WHERE Vid = @Vid;
END;

GO

EXEC UpdateEmail 7, 'newEmail7@example.com';

SELECT * FROM Vizitatori;

GO
CREATE OR ALTER PROCEDURE ShowVisiters
AS
BEGIN
SELECT V.Nume, V.Email, COUNT(VZ.Vid) AS 'Nr. note' FROM Vizitatori V
INNER JOIN VizitatorAtractie VZ ON V.Vid = VZ.Vid
GROUP BY V.Nume, V.Email;
END;

GO

EXEC ShowVisiters;

SELECT * FROM VizitatorAtractie;

GO
CREATE PROCEDURE InsertIfNotAlready @nume VARCHAR(50)
AS
BEGIN
DECLARE @nr_categorii INT;
SET @nr_categorii=0;
SELECT @nr_categorii=COUNT(CategoriiVizitatori.Nume) FROM CategoriiVizitatori WHERE CategoriiVizitatori.Nume = @nume;
if(@nr_categorii = 0)
	INSERT INTO CategoriiVizitatori(Nume) VALUES (@Nume);
END;
GO

SELECT * FROM CategoriiVizitatori;

EXEC InsertIfNotAlready 'categorie8';

GO
CREATE OR ALTER PROCEDURE InsertAtraction @nume VARCHAR(50), @descriere VARCHAR(50), @varsta_min INT, @nume_sectiune VARCHAR(50)
AS
BEGIN
DECLARE @id_sectiune INT;
SET @id_sectiune = -1;
SELECT @id_sectiune=Sectiuni.Sid FROM Sectiuni WHERE Sectiuni.Nume=@nume_sectiune;
IF @id_sectiune = -1
	INSERT INTO Sectiuni(Nume) VALUES (@nume_sectiune);
	SELECT @id_sectiune=Sectiuni.Sid FROM Sectiuni WHERE Sectiuni.Nume=@nume_sectiune;

INSERT INTO Atractii(Nume, Descriere, VarstaMinima, Sid) VALUES (@nume, @descriere, @varsta_min, @id_sectiune);
	
END;
GO

SELECT * FROM Sectiuni;

EXEC InsertAtraction 'nume', 'descriere', 12, 'sectiuneInserata';


GO
CREATE OR ALTER PROCEDURE GetVisiter @email VARCHAR(50),
@vid_id INT OUTPUT
AS
BEGIN
SELECT vid_id=Vizitatori.Vid FROM Vizitatori WHERE Vizitatori.Email=@email;
IF @vid_id = -1
	RAISERROR('Nu exista un vizitator cu acest email', 16, 1);
END;
GO

DECLARE @vid AS INT;

EXEC GetVisiter 'email1', @vid_id=@vid

