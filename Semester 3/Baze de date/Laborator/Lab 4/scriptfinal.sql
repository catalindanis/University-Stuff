-- Run this as a whole in the target DB context (outside of procedures)
USE Meditatii;
GO

----------------------------------------------------------------
-- Logging table: LegaturiEliminate
----------------------------------------------------------------
IF OBJECT_ID('dbo.LegaturiEliminate','U') IS NULL
BEGIN
    CREATE TABLE dbo.LegaturiEliminate
    (
        RemovedRelID INT IDENTITY PRIMARY KEY,
        LeftTableName  NVARCHAR(128) NOT NULL,
        IdLeft         BIGINT NULL,
        RightTableName NVARCHAR(128) NOT NULL,
        IdRight        BIGINT NULL,
        Description    NVARCHAR(400) NULL,
        ChangeDate     DATETIME NOT NULL DEFAULT(GETDATE())
    );
END
GO

----------------------------------------------------------------
-- PROC: Groups - Classes  (1:N -> N:1)
----------------------------------------------------------------
IF OBJECT_ID('proc_GroupsClasses_1N_to_N1','P') IS NOT NULL
    DROP PROCEDURE proc_GroupsClasses_1N_to_N1;
GO

CREATE PROCEDURE proc_GroupsClasses_1N_to_N1
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRAN;

    BEGIN TRY
        -- Validation
        IF OBJECT_ID('dbo.Groups','U') IS NULL THROW 50000, 'Table Groups missing', 1;
        IF OBJECT_ID('dbo.Classes','U') IS NULL THROW 50000, 'Table Classes missing', 1;
        IF OBJECT_ID('dbo.LegaturiEliminate','U') IS NULL THROW 50000, 'Table LegaturiEliminate missing', 1;

        -- 1) DROP FK on Classes.Gid -> Groups (if exists). Name may differ; try known name then lookup by column
        IF EXISTS (SELECT 1 FROM sys.foreign_keys WHERE name = 'FK__Classes__Gid__1F98B2C1')
        BEGIN
            ALTER TABLE dbo.Classes DROP CONSTRAINT FK__Classes__Gid__1F98B2C1;
        END
        ELSE
        BEGIN
            DECLARE @fkName nvarchar(200);
            SELECT TOP 1 @fkName = fk.name
            FROM sys.foreign_keys fk
            JOIN sys.foreign_key_columns fkc ON fk.object_id = fkc.constraint_object_id
            JOIN sys.columns c ON fkc.parent_object_id = c.object_id AND fkc.parent_column_id = c.column_id
            WHERE fk.parent_object_id = OBJECT_ID('dbo.Classes') AND c.name = 'Gid';

            IF @fkName IS NOT NULL
                EXEC('ALTER TABLE dbo.Classes DROP CONSTRAINT [' + @fkName + '];');
        END

        -- 2) Add Groups.Cid if not exist
        IF COL_LENGTH('dbo.Groups','Cid') IS NULL
            EXEC('ALTER TABLE dbo.Groups ADD Cid INT;');

        -- 3) Populate Groups.Cid = MAX(Classes.Cid) per group (only if Classes.Cid exists)
        IF COL_LENGTH('dbo.Classes','Cid') IS NOT NULL
        BEGIN
            EXEC('
                UPDATE G
                SET Cid = (
                    SELECT MAX(C.Cid) FROM dbo.Classes C WHERE C.Gid = G.Gid
                )
                FROM dbo.Groups G;
            ');
        END

        -- 4) Log eliminated Class rows (non-max Cid) into LegaturiEliminate
        IF COL_LENGTH('dbo.Classes','Cid') IS NOT NULL AND COL_LENGTH('dbo.Classes','Gid') IS NOT NULL
        BEGIN
            EXEC('
                INSERT INTO dbo.LegaturiEliminate (LeftTableName, IdLeft, RightTableName, IdRight, Description)
                SELECT ''Groups'', c1.Gid, ''Classes'', c1.Cid,
                       ''Groups–Classes 1:N -> N:1 (removed non-max)'' 
                FROM dbo.Classes c1
                WHERE c1.Cid NOT IN (
                    SELECT MAX(c2.Cid) FROM dbo.Classes c2 WHERE c2.Gid = c1.Gid
                );
            ');
        END

        -- 5) Drop Classes.Gid column if exists
        IF COL_LENGTH('dbo.Classes','Gid') IS NOT NULL
            EXEC('ALTER TABLE dbo.Classes DROP COLUMN Gid;');

        -- 6) Add FK Groups(Cid) -> Classes(Cid) if possible and not exists
        IF NOT EXISTS (SELECT 1 FROM sys.foreign_keys WHERE name = 'FK_Groups_Classes_Id')
        BEGIN
            IF COL_LENGTH('dbo.Groups','Cid') IS NOT NULL AND COL_LENGTH('dbo.Classes','Cid') IS NOT NULL
                EXEC('ALTER TABLE dbo.Groups ADD CONSTRAINT FK_Groups_Classes_Id FOREIGN KEY (Cid) REFERENCES dbo.Classes(Cid);');
        END

        COMMIT TRAN;
        PRINT 'Groups - Classes changed from 1:N to N:1';
    END TRY
    BEGIN CATCH
        ROLLBACK TRAN;
        DECLARE @msg NVARCHAR(4000) = ERROR_MESSAGE();
        RAISERROR(@msg,16,1);
    END CATCH
END;
GO

----------------------------------------------------------------
-- PROC: Ratings - Teachers (1:N -> M:N)
----------------------------------------------------------------
IF OBJECT_ID('proc_RatingsTeachers_1N_to_MN','P') IS NOT NULL
    DROP PROCEDURE proc_RatingsTeachers_1N_to_MN;
GO

CREATE PROCEDURE proc_RatingsTeachers_1N_to_MN
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRAN;

    BEGIN TRY
        -- Validation
        IF OBJECT_ID('dbo.Ratings','U') IS NULL THROW 50000, 'Table Ratings missing', 1;
        IF OBJECT_ID('dbo.Teachers','U') IS NULL THROW 50000, 'Table Teachers missing', 1;
        IF OBJECT_ID('dbo.LegaturiEliminate','U') IS NULL THROW 50000, 'Table LegaturiEliminate missing', 1;

        -- Create linking table RatingTeacher if not exists
        IF OBJECT_ID('dbo.RatingTeacher','U') IS NULL
        BEGIN
            EXEC('
                CREATE TABLE dbo.RatingTeacher
                (
                    Rid INT NOT NULL,
                    Tid INT NOT NULL,
                    CONSTRAINT pk_rating_teacher PRIMARY KEY (Rid, Tid)
                );
            ');

            -- add FKs if target columns exist
            IF COL_LENGTH('dbo.Ratings','Rid') IS NOT NULL AND COL_LENGTH('dbo.RatingTeacher','Rid') IS NOT NULL
                EXEC('ALTER TABLE dbo.RatingTeacher ADD CONSTRAINT fk_rating FOREIGN KEY (Rid) REFERENCES dbo.Ratings(Rid);');

            IF COL_LENGTH('dbo.Teachers','Tid') IS NOT NULL AND COL_LENGTH('dbo.RatingTeacher','Tid') IS NOT NULL
                EXEC('ALTER TABLE dbo.RatingTeacher ADD CONSTRAINT fk_teacher FOREIGN KEY (Tid) REFERENCES dbo.Teachers(Tid);');
        END

        -- Populate linking table from Ratings.Tid if that column exists
        IF COL_LENGTH('dbo.Ratings','Tid') IS NOT NULL
        BEGIN
            EXEC('
                INSERT INTO dbo.RatingTeacher (Rid, Tid)
                SELECT Rid, Tid FROM dbo.Ratings WHERE Tid IS NOT NULL;
            ');
        END

        -- Log: optional — if Ratings had multiple rows per Rid pointing to same Tid scenario, we skip here (no deletion from Ratings)
        -- Keep a safety: if any FK from Ratings to Teachers exists on Tid, drop it
        IF EXISTS (SELECT 1 FROM sys.foreign_keys WHERE name = 'FK__Rating__Tid__534D60F1')
            EXEC('ALTER TABLE dbo.Ratings DROP CONSTRAINT FK__Rating__Tid__534D60F1;');
        ELSE
        BEGIN
            DECLARE @fkRating nvarchar(200);
            SELECT TOP 1 @fkRating = fk.name
            FROM sys.foreign_keys fk
            JOIN sys.foreign_key_columns fkc ON fk.object_id = fkc.constraint_object_id
            JOIN sys.columns c ON fkc.parent_object_id = c.object_id AND fkc.parent_column_id = c.column_id
            WHERE fk.parent_object_id = OBJECT_ID('dbo.Ratings') AND c.name = 'Tid';

            IF @fkRating IS NOT NULL
                EXEC('ALTER TABLE dbo.Ratings DROP CONSTRAINT [' + @fkRating + '];');
        END

        -- Drop column Ratings.Tid if exists
        IF COL_LENGTH('dbo.Ratings','Tid') IS NOT NULL
            EXEC('ALTER TABLE dbo.Ratings DROP COLUMN Tid;');

        COMMIT TRAN;
        PRINT 'Ratings - Teachers changed from 1:N to M:N (RatingTeacher created)';
    END TRY
    BEGIN CATCH
        ROLLBACK TRAN;
        DECLARE @msg NVARCHAR(4000)=ERROR_MESSAGE();
        RAISERROR(@msg,16,1);
    END CATCH
END;
GO

----------------------------------------------------------------
-- PROC: Students - HomeworkStudent - Homeworks (M:N -> 1:N)
----------------------------------------------------------------
IF OBJECT_ID('proc_StudentsHomeworks_MN_to_1N','P') IS NOT NULL
    DROP PROCEDURE proc_StudentsHomeworks_MN_to_1N;
GO

CREATE PROCEDURE proc_StudentsHomeworks_MN_to_1N
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRAN;

    BEGIN TRY
        -- Validation
        IF OBJECT_ID('dbo.Students','U') IS NULL THROW 50000, 'Table Students missing', 1;
        IF OBJECT_ID('dbo.Homeworks','U') IS NULL THROW 50000, 'Table Homeworks missing', 1;
        IF OBJECT_ID('dbo.LegaturiEliminate','U') IS NULL THROW 50000, 'Table LegaturiEliminate missing', 1;

        -- If HomeworkStudent does not exist, nothing to convert, but still ensure Students has Hid column
        IF COL_LENGTH('dbo.Students','Hid') IS NULL
            EXEC('ALTER TABLE dbo.Students ADD Hid INT;');

        -- Add FK Students(Hid) -> Homeworks(Hid) if possible and not exists
        IF NOT EXISTS (SELECT 1 FROM sys.foreign_keys WHERE name = 'FK_student_homework')
        BEGIN
            IF COL_LENGTH('dbo.Students','Hid') IS NOT NULL AND COL_LENGTH('dbo.Homeworks','Hid') IS NOT NULL
                EXEC('ALTER TABLE dbo.Students ADD CONSTRAINT FK_student_homework FOREIGN KEY (Hid) REFERENCES dbo.Homeworks(Hid);');
        END

        -- If HomeworkStudent exists and has Sid,Hid -> populate Students.Hid and log eliminated links, then drop HomeworkStudent
        IF OBJECT_ID('dbo.HomeworkStudent','U') IS NOT NULL
           AND COL_LENGTH('dbo.HomeworkStudent','Sid') IS NOT NULL
           AND COL_LENGTH('dbo.HomeworkStudent','Hid') IS NOT NULL
        BEGIN
            -- populate Hid with MAX(Hid) per student
            EXEC('
                UPDATE S
                SET Hid = (
                    SELECT MAX(hs.Hid) FROM dbo.HomeworkStudent hs WHERE hs.Sid = S.Sid
                )
                FROM dbo.Students S;
            ');

            -- log non-max homework links
            EXEC('
                INSERT INTO dbo.LegaturiEliminate (LeftTableName, IdLeft, RightTableName, IdRight, Description)
                SELECT ''Student'', hs.Sid, ''Homework'', hs.Hid,
                       ''Student–Homework M:N -> 1:N (removed non-max homework link)'' 
                FROM dbo.HomeworkStudent hs
                WHERE hs.Hid NOT IN (
                    SELECT MAX(h2.Hid) FROM dbo.HomeworkStudent h2 WHERE h2.Sid = hs.Sid
                );
            ');

            -- drop FK constraints on HomeworkStudent if present
            DECLARE @fkHS1 nvarchar(200), @fkHS2 nvarchar(200);
            SELECT TOP 1 @fkHS1 = fk.name
            FROM sys.foreign_keys fk
            JOIN sys.foreign_key_columns fkc ON fk.object_id = fkc.constraint_object_id
            JOIN sys.columns c ON fkc.parent_object_id = c.object_id AND fkc.parent_column_id = c.column_id
            WHERE fk.parent_object_id = OBJECT_ID('dbo.HomeworkStudent') AND c.name = 'Hid';

            IF @fkHS1 IS NOT NULL
                EXEC('ALTER TABLE dbo.HomeworkStudent DROP CONSTRAINT [' + @fkHS1 + '];');

            SELECT TOP 1 @fkHS2 = fk.name
            FROM sys.foreign_keys fk
            JOIN sys.foreign_key_columns fkc ON fk.object_id = fkc.constraint_object_id
            JOIN sys.columns c ON fkc.parent_object_id = c.object_id AND fkc.parent_column_id = c.column_id
            WHERE fk.parent_object_id = OBJECT_ID('dbo.HomeworkStudent') AND c.name = 'Sid';

            IF @fkHS2 IS NOT NULL
                EXEC('ALTER TABLE dbo.HomeworkStudent DROP CONSTRAINT [' + @fkHS2 + '];');

            -- drop the HomeworkStudent table
            EXEC('DROP TABLE dbo.HomeworkStudent;');
        END

        COMMIT TRAN;
        PRINT 'Students - Homeworks changed from M:N to 1:N (Students.Hid populated)';
    END TRY
    BEGIN CATCH
        ROLLBACK TRAN;
        DECLARE @msg NVARCHAR(4000)=ERROR_MESSAGE();
        RAISERROR(@msg,16,1);
    END CATCH
END;
GO

----------------------------------------------------------------
-- PROC: Subjects - Topics (1:N -> 1:1)
----------------------------------------------------------------
IF OBJECT_ID('proc_SubjectsTopics_1N_to_11','P') IS NOT NULL
    DROP PROCEDURE proc_SubjectsTopics_1N_to_11;
GO

CREATE PROCEDURE proc_SubjectsTopics_1N_to_11
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRAN;

    BEGIN TRY
        -- Validation
        IF OBJECT_ID('dbo.Subjects','U') IS NULL THROW 50000, 'Table Subjects missing', 1;
        IF OBJECT_ID('dbo.LegaturiEliminate','U') IS NULL THROW 50000, 'Table LegaturiEliminate missing', 1;

        -- Create Topics if not exists (and add sample seeds only if table created)
        IF OBJECT_ID('dbo.Topics','U') IS NULL
        BEGIN
            EXEC('
                CREATE TABLE dbo.Topics
                (
                    Tid INT IDENTITY PRIMARY KEY,
                    Sid INT,
                    Name VARCHAR(100)
                );
            ');

            -- if Subjects.Sid exists, try to add FK from Topics.Sid -> Subjects.Sid
            IF COL_LENGTH('dbo.Subjects','Sid') IS NOT NULL
                EXEC('ALTER TABLE dbo.Topics ADD CONSTRAINT FK_Topic_subject FOREIGN KEY (Sid) REFERENCES dbo.Subjects(Sid);');

            -- Insert sample topics (only as in your original script)
            EXEC('
                INSERT INTO dbo.Topics (Sid, Name) VALUES
                    (1, ''Algebra''), (1, ''Analiza''), (1, ''Geometrie''),
                    (2, ''OOP''), (2, ''MAP''),
                    (3, ''Termodinamica''),
                    (4, ''Sistemul digestiv''), (4, ''Sistemul imunitar''),
                    (5, ''Ion''), (5, ''Baltagul'');
            ');
        END

        -- Add Subjects.Tid column if not exists
        IF COL_LENGTH('dbo.Subjects','Tid') IS NULL
            EXEC('ALTER TABLE dbo.Subjects ADD Tid INT;');

        -- Add FK Subjects.Tid -> Topics.Tid (if possible and not exists)
        IF NOT EXISTS (SELECT 1 FROM sys.foreign_keys WHERE name = 'FK_Subjects_Topics')
        BEGIN
            IF COL_LENGTH('dbo.Subjects','Tid') IS NOT NULL AND COL_LENGTH('dbo.Topics','Tid') IS NOT NULL
                EXEC('ALTER TABLE dbo.Subjects ADD CONSTRAINT FK_Subjects_Topics FOREIGN KEY (Tid) REFERENCES dbo.Topics(Tid);');
        END

        -- Populate Subjects.Tid = MAX(Topics.Tid) per subject (if Topics.Sid present)
        IF OBJECT_ID('dbo.Topics','U') IS NOT NULL AND COL_LENGTH('dbo.Topics','Tid') IS NOT NULL AND COL_LENGTH('dbo.Topics','Sid') IS NOT NULL
        BEGIN
            EXEC('
                UPDATE S
                SET Tid = (SELECT MAX(t.Tid) FROM dbo.Topics t WHERE t.Sid = S.Sid)
                FROM dbo.Subjects S;
            ');

            -- Log non-max topic rows
            EXEC('
                INSERT INTO dbo.LegaturiEliminate (LeftTableName, IdLeft, RightTableName, IdRight, Description)
                SELECT ''Subjects'', t.Sid, ''Topics'', t.Tid,
                       ''Subjects–Topics 1:N -> 1:1 (removed non-max topics)'' 
                FROM dbo.Topics t
                WHERE t.Tid NOT IN (
                    SELECT MAX(t2.Tid) FROM dbo.Topics t2 WHERE t2.Sid = t.Sid
                );
            ');

            -- Add unique constraint on Subjects.Tid if possible and safe (no duplicates)
            IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'fk_topic_unique' AND object_id = OBJECT_ID('dbo.Subjects'))
            BEGIN
                IF NOT EXISTS (
                    SELECT Tid FROM dbo.Subjects WHERE Tid IS NOT NULL GROUP BY Tid HAVING COUNT(*) > 1
                )
                BEGIN
                    EXEC('ALTER TABLE dbo.Subjects ADD CONSTRAINT fk_topic_unique UNIQUE (Tid);');
                END
            END

            -- Drop FK Topics.Sid -> Subjects.Sid and drop Topics.Sid if exists
            IF COL_LENGTH('dbo.Topics','Sid') IS NOT NULL
            BEGIN
                IF EXISTS (SELECT 1 FROM sys.foreign_keys WHERE name = 'FK_Topic_subject')
                    EXEC('ALTER TABLE dbo.Topics DROP CONSTRAINT FK_Topic_subject;')
                ELSE
                BEGIN
                    DECLARE @fkTopic nvarchar(200);
                    SELECT TOP 1 @fkTopic = fk.name
                    FROM sys.foreign_keys fk
                    JOIN sys.foreign_key_columns fkc ON fk.object_id = fkc.constraint_object_id
                    JOIN sys.columns c ON fkc.parent_object_id = c.object_id AND fkc.parent_column_id = c.column_id
                    WHERE fk.parent_object_id = OBJECT_ID('dbo.Topics') AND c.name = 'Sid';

                    IF @fkTopic IS NOT NULL
                        EXEC('ALTER TABLE dbo.Topics DROP CONSTRAINT [' + @fkTopic + '];');
                END

                EXEC('ALTER TABLE dbo.Topics DROP COLUMN Sid;');
            END
        END

        COMMIT TRAN;
        PRINT 'Subjects - Topics changed from 1:N to 1:1';
    END TRY
    BEGIN CATCH
        ROLLBACK TRAN;
        DECLARE @msg NVARCHAR(4000)=ERROR_MESSAGE();
        RAISERROR(@msg,16,1);
    END CATCH
END;
GO

----------------------------------------------------------------
-- Run all procedures (safe to re-run)
----------------------------------------------------------------
EXEC proc_GroupsClasses_1N_to_N1;
EXEC proc_RatingsTeachers_1N_to_MN;
EXEC proc_StudentsHomeworks_MN_to_1N;
EXEC proc_SubjectsTopics_1N_to_11;
GO

-- Inspect results / logs
SELECT TOP (100) * FROM dbo.LegaturiEliminate ORDER BY RemovedRelID DESC;
GO

-- Optional: show sample content if those tables exist
IF OBJECT_ID('dbo.Groups','U') IS NOT NULL SELECT TOP (50) * FROM dbo.Groups;
IF OBJECT_ID('dbo.Classes','U') IS NOT NULL SELECT TOP (50) * FROM dbo.Classes;
IF OBJECT_ID('dbo.RatingTeacher','U') IS NOT NULL SELECT TOP (50) * FROM dbo.RatingTeacher;
IF OBJECT_ID('dbo.Students','U') IS NOT NULL SELECT TOP (50) * FROM dbo.Students;
IF OBJECT_ID('dbo.Homeworks','U') IS NOT NULL SELECT TOP (50) * FROM dbo.Homeworks;
IF OBJECT_ID('dbo.Topics','U') IS NOT NULL SELECT TOP (50) * FROM dbo.Topics;
IF OBJECT_ID('dbo.Subjects','U') IS NOT NULL SELECT TOP (50) * FROM dbo.Subjects;
GO
