USE Meditatii;

-- Afisarea tuturor profesorilor cu email oficial UBB
SELECT * FROM Teachers WHERE Teachers.Email LIKE '%@ubb.ro';

-- Afisare id, nume a tuturor studentilor care studiaza matematica
SELECT S.Sid, S.Name, G.Gid 
FROM Students S 
INNER JOIN StudentGroup SG ON S.sid = SG.Sid 
INNER JOIN Groups G ON SG.Gid = G.Gid 
WHERE G.Sid = 1;

-- Afisare id, nume, experienta a tuturor profesorilor care predau informatica
SELECT DISTINCT T.Tid, T.Name, T.Experience 
FROM Teachers T 
INNER JOIN TeacherGroup TG ON T.Tid = TG.Tid
INNER JOIN Groups G ON G.Sid = TG.Gid 
WHERE G.Sid = 2;

-- Afisare id, nume, numarul total de teme pentru fiecare student
SELECT S.Sid, S.Name, COUNT(H.Hid) AS "Numar teme" 
FROM Students S 
LEFT JOIN HomeworkStudent HS ON HS.Sid = S.Sid
LEFT JOIN Homeworks H ON H.Hid = HS.Hid
GROUP BY S.Sid, S.Name;

-- Afisare nume autor, nume destinatar, valoare pentru fiecare rating
SELECT R.Value "Valoare", S.Name "Nume autor", T.Name "Nume destinatar" from Ratings R
INNER JOIN Students S ON R.Sid = S.Sid
INNER JOIN Teachers T on R.Tid = T.Tid;

-- Afisare id, nume materie si nr de profesori care o predau pentru materiile predate de cel putin 2 profesori
SELECT S.Sid, S.Name, COUNT(TG.Gid) AS "Nr. profesori" FROM Subjects S
LEFT JOIN Groups G ON G.Sid = S.Sid
LEFT JOIN TeacherGroup TG ON G.Gid = TG.Gid
GROUP BY S.Sid, S.Name
HAVING COUNT(TG.Gid) >= 2;

-- Afisare id, numar problema, nr studenti cu aceasta tema pentru temele atribuite la mai mult de 1 student
SELECT H.Hid, H.ProblemNumber, COUNT(S.Sid) "Studenti" FROM Homeworks H
LEFT JOIN HomeworkStudent HS ON H.Hid = HS.Hid
LEFT JOIN Students S ON S.Sid = HS.Sid
GROUP BY H.Hid, H.ProblemNumber
HAVING COUNT(S.Sid) > 1;

-- Afisare nume student, grupul lui, data sedintei viitoare (de dupa 23.10.2025 sau inclusiv)
SELECT S.Name, G.Name, C.Date FROM Students S
INNER JOIN StudentGroup SG ON SG.Sid = S.Sid
INNER JOIN Groups G ON G.Gid = SG.Gid
INNER JOIN Classes C ON C.Gid = G.Gid
WHERE C.Date >= '2025-10-23';

-- Afisare email profesor si materia predata daca are o experienta mai mare de 3 ani
SELECT DISTINCT T.Email, S.Name FROM Teachers T
INNER JOIN TeacherGroup TG ON T.Tid = TG.Tid
INNER JOIN Groups G ON G.Gid = TG.Gid
INNER JOIN Subjects S ON S.Sid = G.Sid
WHERE T.Experience > 3;

-- Afisare student si nume grup pentru fiecare student si grup
SELECT Students.Name, G.Name FROM Students
INNER JOIN StudentGroup SG ON Students.Sid = SG.Sid
INNER JOIN Groups G ON G.Gid = SG.Gid;