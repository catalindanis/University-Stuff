use Meditatii;

-- Procedura creeaza tabela pentru legaturile pierdute in urma transformarilor
GO
CREATE OR ALTER PROCEDURE CreareTabelaLegaturi
AS
BEGIN
	CREATE TABLE LegaturiEliminate
	(NumeTabelSt VARCHAR(100),
	IdSt INT,
	NumeTabelDr VARCHAR(100),
	IdDr INT);
END;

-- Groups - Classes
-- 1 - n => n - 1
CREATE OR ALTER PROCEDURE Schimbare1
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        BEGIN TRAN;

        IF EXISTS (SELECT 1 FROM sys.foreign_keys WHERE name = 'FK__Classes__Gid__1F98B2C1')
            ALTER TABLE Classes DROP CONSTRAINT FK__Classes__Gid__1F98B2C1;

        IF COL_LENGTH('Groups','Cid') IS NULL
            ALTER TABLE Groups ADD Cid INT;

        DECLARE @sql1 NVARCHAR(MAX)=N'
            UPDATE Groups
            SET Cid = (
                SELECT MAX(Cid)
                FROM Classes 
                WHERE Classes.Gid = Groups.Gid
            );
        ';
        EXEC(@sql1);

        INSERT INTO LegaturiEliminate
        SELECT 'Groups', c1.Gid, 'Classes', c1.Cid
        FROM Classes c1
        WHERE c1.Cid NOT IN (
            SELECT MAX(C.Cid) 
            FROM Classes C 
            WHERE c1.Gid = C.Gid
        );

        IF COL_LENGTH('Classes','Gid') IS NOT NULL
        BEGIN
            DECLARE @sql2 NVARCHAR(MAX)=N'ALTER TABLE Classes DROP COLUMN Gid;';
            EXEC(@sql2);
        END        

        ALTER TABLE Groups
        ADD CONSTRAINT FK_Groups_Classes_Id
        FOREIGN KEY (Cid) REFERENCES Classes(Cid);

        COMMIT TRAN;
    END TRY
    BEGIN CATCH
        ROLLBACK TRAN;
        THROW;
    END CATCH
END;
GO

-- Ratings - Teachers
-- 1 - n => m - n
CREATE OR ALTER PROCEDURE Schimbare2
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        BEGIN TRAN;

        IF OBJECT_ID('RatingTeacher','U') IS NULL
        BEGIN
            EXEC ('
                CREATE TABLE RatingTeacher(
                    Rid INT,
                    Tid INT,
                    CONSTRAINT pk_rating_teacher PRIMARY KEY(Rid,Tid),
                    CONSTRAINT fk_rating FOREIGN KEY (Rid) REFERENCES Ratings(Rid),
                    CONSTRAINT fk_teacher FOREIGN KEY (Tid) REFERENCES Teachers(Tid)
                );
            ');
        END

        INSERT INTO RatingTeacher
        SELECT Rid, Tid FROM Ratings;

        IF EXISTS(SELECT 1 FROM sys.foreign_keys WHERE name='FK__Rating__Tid__534D60F1')
            ALTER TABLE Ratings DROP CONSTRAINT FK__Rating__Tid__534D60F1;

        IF COL_LENGTH('Ratings','Tid') IS NOT NULL
        BEGIN
            DECLARE @sql3 NVARCHAR(MAX)='ALTER TABLE Ratings DROP COLUMN Tid;';
            EXEC(@sql3);
        END

        COMMIT TRAN;
    END TRY
    BEGIN CATCH
        ROLLBACK TRAN;
        THROW;
    END CATCH
END;
GO

-- Students - HomeworkStudent - Homeworks
-- m - n => 1 - n
CREATE OR ALTER PROCEDURE Schimbare3
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        BEGIN TRAN;

        IF COL_LENGTH('Students','Hid') IS NULL
            ALTER TABLE Students ADD Hid INT;

        ALTER TABLE Students
        ADD CONSTRAINT FK_student_homework FOREIGN KEY (Hid) REFERENCES Homeworks(Hid);

        DECLARE @sql4 NVARCHAR(MAX)=N'
            UPDATE Students
            SET Hid = (
                SELECT MAX(Hid) 
                FROM HomeworkStudent 
                WHERE HomeworkStudent.Sid = Students.Sid
            );
        ';
        EXEC(@sql4);

        INSERT INTO LegaturiEliminate
        SELECT 'Homework',hs.Hid,'Student',hs.Sid
        FROM HomeworkStudent hs
        WHERE hs.Hid NOT IN (
            SELECT MAX(Hid) FROM HomeworkStudent WHERE Sid = hs.Sid
        );

        IF EXISTS (SELECT 1 FROM sys.foreign_keys WHERE name='FK_HomeworkStuHid_7B5B524B')
            ALTER TABLE HomeworkStudent DROP CONSTRAINT FK_HomeworkStuHid_7B5B524B;

        IF EXISTS (SELECT 1 FROM sys.foreign_keys WHERE name='FK_HomeworkStuSid_7C4F7684')
            ALTER TABLE HomeworkStudent DROP CONSTRAINT FK_HomeworkStuSid_7C4F7684;

        DROP TABLE HomeworkStudent;

        COMMIT TRAN;
    END TRY
    BEGIN CATCH
        ROLLBACK TRAN;
        THROW;
    END CATCH
END;
GO

-- Topics - Subjects
-- 1 - n => 1 - 1
CREATE OR ALTER PROCEDURE Schimbare4
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        BEGIN TRAN;

        IF OBJECT_ID('Topics','U') IS NULL
        BEGIN
            EXEC('
                CREATE TABLE Topics(
                    Tid INT PRIMARY KEY IDENTITY,
                    Sid INT,
                    Name VARCHAR(100)
                );
            ');
        END

        ALTER TABLE Topics
        ADD CONSTRAINT FK_Topic_subject FOREIGN KEY (Sid) REFERENCES Subjects(Sid);

        INSERT INTO Topics
	    (Sid, Name) VALUES
	    (1, 'Algebra'),
	    (1, 'Analiza'),
	    (1, 'Geometrie'),
	    (2, 'OOP'),
	    (2, 'MAP'),
	    (3, 'Termodinamica'),
	    (4, 'Sistemul digestiv'),
	    (4, 'Sistemul imunitar'),
	    (5, 'Ion'),
	    (5, 'Baltagul');

        IF COL_LENGTH('Subjects','Tid') IS NULL
            ALTER TABLE Subjects ADD Tid INT;

        ALTER TABLE Subjects
        ADD CONSTRAINT FK_Subjects_Topics FOREIGN KEY (Tid) REFERENCES Topics(Tid);

        DECLARE @sql5 NVARCHAR(MAX)=N'
            UPDATE Subjects
            SET Tid = (
                SELECT MAX(Tid)
                FROM Topics t
                WHERE Subjects.Sid = t.Sid
            );
        ';
        EXEC(@sql5);

        INSERT INTO LegaturiEliminate
        SELECT 'Subjects',t.Sid,'Topics',t.Tid
        FROM Topics t
        WHERE t.Tid NOT IN(
            SELECT MAX(t2.Tid) FROM Topics t2 WHERE t.Sid = t2.Sid
        );

        ALTER TABLE Subjects
        ADD CONSTRAINT fk_topic_unique UNIQUE(Tid);

        IF EXISTS(SELECT 1 FROM sys.foreign_keys WHERE name='FK_Topic_subject')
            ALTER TABLE Topics DROP CONSTRAINT FK_Topic_subject;

        IF COL_LENGTH('Topics','Sid') IS NOT NULL
        BEGIN
            DECLARE @sql6 NVARCHAR(MAX)=N'ALTER TABLE Topics DROP COLUMN Sid;';
            EXEC(@sql6);
        END

        COMMIT TRAN;
    END TRY
    BEGIN CATCH
        ROLLBACK TRAN;
        THROW;
    END CATCH
END;
GO

EXEC CreareTabelaLegaturi;
EXEC Schimbare1;
EXEC Schimbare2;
EXEC Schimbare3;
EXEC Schimbare4;

SELECT * FROM LegaturiEliminate;