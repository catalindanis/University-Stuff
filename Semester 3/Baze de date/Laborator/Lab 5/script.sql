use Meditatii;

-- Functia returneaza 0 daca numele este invalid, respectiv 1 in caz contrar
CREATE OR ALTER FUNCTION testName (@name NVARCHAR(50))
RETURNS BIT
AS
BEGIN
    IF @name IS NULL OR LEN(@name) = 0
        RETURN 0;

    IF @name LIKE '%[^A-Za-z -]%'
        RETURN 0;

    RETURN 1;
END;

-- Functia returneaza 0 daca emailul este invalid, respectiv 1 in caz contrar
CREATE OR ALTER FUNCTION testEmail (@email NVARCHAR(50))
RETURNS BIT
AS
BEGIN
    IF @email IS NULL OR LEN(@email) = 0
        RETURN 0;

    IF @email NOT LIKE '%_@_%._%'
        RETURN 0;

    RETURN 1;
END;

-- Functia returneaza 0 daca numarul de telefon este invalid, respectiv 1 in caz contrar
CREATE OR ALTER FUNCTION testPhoneNumber (@phoneNumber NVARCHAR(20))
RETURNS BIT
AS
BEGIN
    IF @phoneNumber IS NULL OR LEN(@phoneNumber) = 0
        RETURN 0;

    IF @phoneNumber LIKE '%[^0-9]%'
        RETURN 0;

    RETURN 1;
END;

-- Functia returneaza 0 daca clasa este invalida, respectiv 1 in caz contrar
CREATE OR ALTER FUNCTION testGradeLevel(@gradeLevel INT)
RETURNS BIT
AS
BEGIN
    IF @gradeLevel IS NULL
        RETURN 0;

    IF @gradeLevel < 1 OR @gradeLevel > 12
        RETURN 0;

    RETURN 1;
END

-- Functia returneaza 0 daca numarul problemei e invalid, respectiv 1 in caz contrar
CREATE OR ALTER FUNCTION testProblemNumber(@problemNumber INT)
RETURNS BIT
AS
BEGIN
    IF @problemNumber IS NULL
        RETURN 0;

    IF @problemNumber < 1
        RETURN 0;

    RETURN 1;
END

-- Functia creeaza un student cu parametrii transmisi (daca parametrii sunt valizi)
CREATE OR ALTER PROCEDURE CreateStudent
@name varchar(50),
@email varchar(50),
@phoneNumber varchar(20),
@gradeLevel INT,
@newStudentId INT OUTPUT
AS
BEGIN
    IF dbo.testName(@name)=0
    BEGIN
        print 'Nume invalid'
        RETURN
    END

    IF dbo.testEmail(@email)=0
    BEGIN
        print 'Email invalid'
        RETURN
    END

    IF dbo.testPhoneNumber(@phoneNumber)=0
    BEGIN
        print 'Numar de telefon invalid'
        RETURN
    END

    IF dbo.testGradeLevel(@gradeLevel)=0
    BEGIN
        print 'Clasa invalida'
        RETURN
    END

	INSERT INTO Students(Name, Email, PhoneNumber, GradeLevel)
	VALUES(@name, @email, @phoneNumber, @gradeLevel)

    SET @newStudentId = SCOPE_IDENTITY();

    print 'Student creat cu succes'
END

-- Functia sterge studentul cu un id-ul transmis ca parametru
CREATE OR ALTER PROCEDURE DeleteStudent
@id INT
AS
BEGIN
    DELETE FROM Students WHERE Students.Sid = @id;
END
GO

-- Functia incrementeaza clasa studentului cu id-ul transmis ca parametru
CREATE OR ALTER PROCEDURE UpdateStudent
@id INT
AS
BEGIN
    UPDATE Students SET GradeLevel = GradeLevel + 1 WHERE Sid = @id AND GradeLevel < 12;
END
GO

-- Functia afiseaza toti studentii
CREATE OR ALTER PROCEDURE ReadStudents
AS
BEGIN
    SELECT * FROM Students;
END
GO

DECLARE @NewStudentId INT;
EXEC CreateStudent @name='test', @email='ana@yahoo.com', @phoneNumber='1234', @gradeLevel = 9, @newStudentId = @NewStudentId OUTPUT;
SELECT @NewStudentId AS NewStudentId;
EXEC DeleteStudent @id=26;
EXEC UpdateStudent @id=27;
EXEC ReadStudents;

CREATE TABLE StudentsLog(
    Id INT PRIMARY KEY IDENTITY,
    OperationType VARCHAR(20),
    OperationDate DATETIME,
    LoginName VARCHAR(100),
    Sid INT,
    Name VARCHAR(50),
    Email VARCHAR(50),
    PhoneNumber VARCHAR(20),
    GradeLevel INT
);

SELECT * FROM StudentsLog;

CREATE OR ALTER TRIGGER StudentsUpdateTrigger
ON Students
FOR UPDATE
AS
BEGIN
    INSERT INTO StudentsLog
    (OperationType, OperationDate, LoginName, Sid, Name, Email, PhoneNumber, GradeLevel)
    SELECT
    'UPDATE', GETDATE(), SUSER_SNAME(), d.Sid, d.Name, d.Email, d.PhoneNumber, d.GradeLevel
    FROM deleted d;
END

CREATE OR ALTER TRIGGER StudentsDeleteTrigger
ON Students
AFTER DELETE
AS
BEGIN
    INSERT INTO StudentsLog
    (OperationType, OperationDate, LoginName, Sid, Name, Email, PhoneNumber, GradeLevel)
    SELECT
    'DELETE', GETDATE(), SUSER_SNAME(), d.Sid, d.Name, d.Email, d.PhoneNumber, d.GradeLevel
    FROM deleted d;
END

-- Functia creeaza o legatura de tip homework-student daca parametrii sunt valizi
CREATE OR ALTER PROCEDURE CreateHomeworkStudent
@hid INT,
@sid INT
AS
BEGIN
    IF (SELECT COUNT(*) FROM Students WHERE Sid = @sid) < 1
    BEGIN
        PRINT 'Studentul nu exista';
        RETURN
    END

    IF (SELECT COUNT(*) FROM Homeworks WHERE Hid = @hid) < 1
    BEGIN
        PRINT 'Tema nu exista';
        RETURN
    END

    INSERT INTO HomeworkStudent(Hid, Sid)
    VALUES(@hid, @sid);
END
GO

-- Functia sterge o legatura de tip homework-student cu parametrii transmisi
CREATE OR ALTER PROCEDURE DeleteHomeworkStudent
@hid INT,
@sid INT
AS
BEGIN
    DELETE FROM HomeworkStudent WHERE Hid=@hid AND Sid=@sid;
END
GO

-- Functia actualizeaza tema dintr-o legatura de tip homework-student
CREATE OR ALTER PROCEDURE UpdateHomeworkStudent
@sid INT,
@hid INT,
@newHid INT
AS
BEGIN
    IF (SELECT COUNT(*) FROM HomeworkStudent WHERE Sid = @sid AND Hid = @hid) < 1
    BEGIN
        PRINT 'Legatura nu exista';
        RETURN
    END

    IF (SELECT COUNT(*) FROM Homeworks WHERE Hid = @newHid) < 1
    BEGIN
        PRINT 'Tema nu exista';
        RETURN
    END

    UPDATE HomeworkStudent SET Hid = @newHid WHERE Hid = @hid AND Sid = @sid;
END
GO

-- Functia afiseaza toate legaturile de tip homework-student
CREATE OR ALTER PROCEDURE ReadHomeworkStudent
AS
BEGIN
    SELECT * FROM HomeworkStudent;
END
GO

EXEC CreateHomeworkStudent @hid = 32, @sid = 21;
EXEC DeleteHomeworkStudent @hid = 26, @sid = 21;
EXEC UpdateHomeworkStudent @hid = 31, @sid = 21, @newHid = 26;
EXEC ReadHomeworkStudent;

CREATE TABLE HomeworksStudentsLog(
    Id INT PRIMARY KEY IDENTITY,
    OperationType VARCHAR(20),
    OperationDate DATETIME,
    LoginName VARCHAR(100),
    Hid INT,
    Sid INT
);

SELECT * FROM HomeworksStudentsLog;

CREATE OR ALTER TRIGGER HomeworksStudentsUpdateTrigger
ON HomeworkStudent
FOR UPDATE
AS
BEGIN
    INSERT INTO HomeworksStudentsLog
    (OperationType, OperationDate, LoginName, Hid, Sid)
    SELECT
    'UPDATE', GETDATE(), SUSER_SNAME(), d.Hid, d.Sid
    FROM deleted d;
END

CREATE OR ALTER TRIGGER HomeworksStudentsDeleteTrigger
ON HomeworkStudent
AFTER DELETE
AS
BEGIN
    INSERT INTO HomeworksStudentsLog
    (OperationType, OperationDate, LoginName, Hid, Sid)
    SELECT
    'DELETE', GETDATE(), SUSER_SNAME(), d.Hid, d.Sid
    FROM deleted d;
END

-- Functia creeaza un homework daca parametrii sunt valizi
CREATE OR ALTER PROCEDURE CreateHomework
@problemNumber INT,
@dueDate DATETIME,
@newHomeworkId INT OUTPUT
AS
BEGIN
    IF dbo.testProblemNumber(@problemNumber) = 0
        RETURN 0
    
    INSERT INTO Homeworks(ProblemNumber, DueDate) VALUES
    (@problemNumber, @dueDate);

    SET @newHomeworkId = SCOPE_IDENTITY()

    print 'Tema s-a creat cu succes'
END
GO

-- Functia sterge un homework dupa id
CREATE OR ALTER PROCEDURE DeleteHomework
@id INT
AS
BEGIN
    DELETE FROM Homeworks WHERE Hid = @id;
END
GO

-- Functia actualizeaza problemNumber-ul unui homework
CREATE OR ALTER PROCEDURE UpdateHomework
@id INT,
@problemNumber INT
AS
BEGIN
    IF dbo.testProblemNumber(@problemNumber) = 0
        RETURN

    UPDATE Homeworks SET ProblemNumber = @problemNumber WHERE Hid = @id;
END
GO

-- Functia afiseaza toate temele
CREATE OR ALTER PROCEDURE ReadHomeworks
AS
BEGIN
    SELECT * FROM Homeworks;
END
GO

DECLARE @NewHomeworkId INT;
EXEC CreateHomework @problemNumber=10, @dueDate='2025-01-15 14:30:00', @newHomeworkId = @NewHomeworkId OUTPUT;
SELECT @NewHomeworkId AS NewHomeworkId;
EXEC DeleteHomework @id = 35;
EXEC UpdateHomework @id = 35, @problemNumber = 101;
EXEC ReadHomeworks;

CREATE TABLE HomeworksLog(
    Id INT PRIMARY KEY IDENTITY,
    OperationType VARCHAR(20),
    OperationDate DATETIME,
    LoginName VARCHAR(100),
    Hid INT,
    ProblemNumber INT,
    DueDate DATETIME
);

SELECT * FROM HomeworksLog;

CREATE OR ALTER TRIGGER HomeworksUpdateTrigger
ON Homeworks
FOR UPDATE
AS
BEGIN
    INSERT INTO HomeworksLog
    (OperationType, OperationDate, LoginName, Hid, ProblemNumber, DueDate)
    SELECT
    'UPDATE', GETDATE(), SUSER_SNAME(), d.Hid, d.ProblemNumber, d.DueDate
    FROM deleted d;
END

CREATE OR ALTER TRIGGER HomeworksDeleteTrigger
ON Homeworks
AFTER DELETE
AS
BEGIN
    INSERT INTO HomeworksLog
    (OperationType, OperationDate, LoginName, Hid, ProblemNumber, DueDate)
    SELECT
    'DELETE', GETDATE(), SUSER_SNAME(), d.Hid, d.ProblemNumber, d.DueDate
    FROM deleted d;
END

-- View-ul contine toti studentii de la liceu
CREATE OR ALTER VIEW StudentsFromHighSchool
AS
(SELECT * FROM Students WHERE Students.GradeLevel >= 9);

-- Avand un index care retine studentii ordonati crescator dupa clasa,
-- putem mult mai rapid sa gasim toti elevii care au o clasa mai mare sau egala cu 9
CREATE INDEX IX_Students_GradeLevel_ASC
ON Students (GradeLevel ASC)
INCLUDE (Sid, Name, Email, PhoneNumber)

SELECT * FROM StudentsFromHighSchool;

-- View-ul contine studentii care au mai mult de o tema de facut
CREATE OR ALTER VIEW StudentsWithMoreThan1Homework
AS
(SELECT S.Name, COUNT(H.Hid) AS NumberOfHomeworks FROM Students S
INNER JOIN HomeworkStudent HS ON HS.Sid = S.Sid
INNER JOIN Homeworks H ON H.Hid = HS.Hid
GROUP BY S.Name, S.Sid);

SELECT * FROM StudentsWithMoreThan1Homework;

-- Index-ul acesta sorteaza legatura dintre Student si Homework (HomeworkStudent)
-- dupa id student crescator si id homework crescator, fiind mult mai rapid
-- sa se faca legatura dintre un student si temele lui
CREATE NONCLUSTERED INDEX IX_HS_Sid_Hid
ON HomeworkStudent (Sid, Hid);