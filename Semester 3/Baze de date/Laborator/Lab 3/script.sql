USE Meditatii;

-- Procedura v1 modifica coloana
-- Value din Ratings in REAL
CREATE OR ALTER PROCEDURE v1
AS
BEGIN
	ALTER TABLE Ratings
	ALTER COLUMN Value REAL;
	print 'coloana Value din Ratings a fost modificata la REAL'
END;

-- Procedura v1n modifica coloana
-- Value din Ratings in INT
CREATE OR ALTER PROCEDURE v1n
AS
BEGIN
	ALTER TABLE Ratings
	ALTER COLUMN Value INT;
	print 'coloana Value din Ratings a fost modificata la INT'
END;

EXEC v1;
EXEC v1n;

-- Procedura v2 adauga constrangerea
-- de default value (0) pentru coloana ProblemNumber
CREATE OR ALTER PROCEDURE v2
AS
BEGIN
	ALTER TABLE Homeworks
	ADD CONSTRAINT df_0 DEFAULT 0
	FOR ProblemNumber;
	print 'coloana ProblemNumber din Homeworks a primit o constrangere'
END;

-- Procedura v2 sterge constrangerea
-- de default value (0) pentru coloana ProblemNumber
CREATE OR ALTER PROCEDURE v2n
AS
BEGIN
	ALTER TABLE Homeworks
	DROP CONSTRAINT df_0;
	print 'coloana ProblemNumber din Homeworks a scapat de o constrangere'
END;

EXEC v2;
EXEC v2n;

-- Procedura v3 creeaza tabela Grades
CREATE OR ALTER PROCEDURE v3
AS
BEGIN
	CREATE TABLE Grades
		(Sid INT NOT NULL FOREIGN KEY REFERENCES Students(Sid),
		Tid INT NOT NULL FOREIGN KEY REFERENCES Teachers(Tid),
		Hid INT NOT NULL FOREIGN KEY REFERENCES Homeworks(Hid));
	print 'tabela Grades a fost creata'
END;

-- Procedura v3n sterge tabela Grades
CREATE OR ALTER PROCEDURE v3n
AS
BEGIN
	DROP TABLE Grades;	
	print 'tabela Grades a fost stearsa'
END;

EXEC v3;
EXEC v3n;

-- Procedura v4 modifica tabela Grades adaugand coloana Value
CREATE OR ALTER PROCEDURE v4
AS
BEGIN
	ALTER TABLE Grades
	ADD Value REAL;
	print 'coloana Value a fost adaugata'
END;

-- Procedura v4n modifica tabela Grades stergand coloana Value
CREATE OR ALTER PROCEDURE v4n
AS
BEGIN
	ALTER TABLE Grades
	DROP COLUMN Value;
	print 'coloana Value a fost stearsa'
END;

EXEC v4;
EXEC v4n;

-- Procedura v5 adauga constrangerea de primary key in Grades
CREATE OR ALTER PROCEDURE v5
AS 
BEGIN
	ALTER TABLE Grades
	ADD CONSTRAINT pk_Grades PRIMARY KEY (Hid, Sid, Tid);
	print 'constrangerea de primary key a fost adaugata'
END

-- Procedura v5n sterge constrangerea de primary key din Grades
CREATE OR ALTER PROCEDURE v5n
AS
BEGIN
	ALTER TABLE Grades
	DROP CONSTRAINT pk_Grades;
	print 'constrangerea de primary key a fost stearsa'
END

EXEC v5;
EXEC v5n;

-- Crearea tabelei de versiuni a bazei de date
CREATE TABLE DBVersion
	(ID INT PRIMARY KEY IDENTITY,
	Version INT NOT NULL DEFAULT 0);

-- Inserarea valorii default in tabela de versiuni 
INSERT INTO DBVersion(Version) VALUES
	(0);

-- Procedura primeste ca parametru versiunea la care
-- vrem sa aducem baza de date (int), si aduce baza de date
-- la acea versiune
CREATE OR ALTER PROCEDURE SelectVersion
@version INT
AS
BEGIN
	IF @version < 0 OR @version > 5
	BEGIN
		print 'Versiune incorecta'
		RETURN
	END

	DECLARE @current_version INT;
	SELECT @current_version = Version FROM DBVersion WHERE ID = 1

	IF @current_version = @version
	BEGIN
		PRINT 'Baza de date este deja la aceasta versiune'
		RETURN
	END

	DECLARE @sql NVARCHAR(MAX)
	IF @current_version < @version
	BEGIN
		SET @current_version = @current_version + 1
		WHILE @current_version <= @version
		BEGIN
			SET @sql = 'EXEC v' + CAST(@current_version AS NVARCHAR(1));
			-- PRINT @sql;

			EXEC(@sql);

			SET @current_version = @current_version + 1;
		END

		UPDATE DBVersion
		SET Version = @current_version - 1;
		RETURN
	END

	WHILE @current_version > @version
	BEGIN
		SET @sql = 'EXEC v' + CAST(@current_version AS NVARCHAR(1)) + 'n';
		-- PRINT @sql;

		EXEC(@sql);

		SET @current_version = @current_version - 1
	END

	UPDATE DBVersion
	SET Version = @current_version;
END;

EXEC SelectVersion 1;