USE Meditatii;

INSERT INTO Students(Name, Email, PhoneNumber, GradeLevel) 
VALUES ('Ana Pop', 'ana.pop@example.com', '0721111111', 9),
('Mihai Ionescu', 'mihai.ion@example.com', '0742222222', 10),
('Elena Georgescu', 'elena.12@gmail.com', '0733333333', 11),
('Andrei Vasilescu', 'andrei.v@example.com', '0722333441', 7),
('Ioana Dumitru', 'ioana.d@yahoo.com', '0756123456', 12),
('Vlad Marinescu', 'mvlad@hotmail.com', '0758123999', 10),
('Cristina Matei', 'cristina.m@example.com', '0756345112', 9),
('Radu Stan', 'radu_stan@yahoo.com', '0788913521', 9),
('Sorina Pavel', 'spavel@gmail.com', '0756889113', 12),
('Dan Petrescu', 'dandanp@hotmail.com', '0777777777', 11);

SELECT * FROM Students;

INSERT INTO Teachers(Name, Email, PhoneNumber, Experience)
Values 
('Laura Popa', 'laura.popa@school.ro', '0721000101', 6),
('Adrian Tudor', 'adrian.tudor@utcn.ro', '0721000102', 12),
('Sorin Dobre', 'sorin.dobre@ubb.ro', '0721000104', 9),
('Carmen Rusu', 'carmen.rusu@cnmv.ro', '0721000105', 10),
('Iulian Barbu', 'iul.barb@hotmail.com', '0721000106', 2),
('Monica Florescu', 'monica.f@ubb.ro', '0721000107', 7),
('George Ilie', 'george.ilie@school.ro', '0712345678', 1),
('Teodora Neagu', 'teo.neagu@ubb.ro', '0721000109', 11),
('Paul Cazacu', 'paul.c@utcn.ro', '0721000110', 4),
('Bianca Enache', 'bia_enache@gmail.com', '0755989123', 3);

SELECT * FROM Teachers;

INSERT INTO Subjects
VALUES
('Matematica'),
('Informatica'),
('Fizica'),
('Biologie'),
('Limba romana');

SELECT * FROM Subjects;

INSERT INTO Groups (Sid, Name, NumberOfMembers)
VALUES
(1, 'Matematica I', 1),
(1, 'Matematica II', 2),
(2, 'Informatica I', 1),
(2, 'Informatica II', 2),
(4, 'Biologie I', 1),
(4, 'Biologie II', 2),
(5, 'Limba romana I', 1),
(3, 'Fizica I', 2);

SELECT * FROM Groups;

INSERT INTO Classes (Name, Date, Gid)
VALUES
('Sedinta matematica 1', '2025-10-21 09:00:00', 2),
('Sedinta matematica 2', '2025-10-23 11:00:00', 2),
('Sedinta matematica 1', '2025-10-28 18:00:00', 3),
('Sedinta informatica 1', '2025-09-22 18:00:00', 4),
('Sedinta informatica 1', '2025-10-14 12:00:00', 5),
('Sedinta informatica 2', '2025-10-27 11:00:00', 5),
('Sedinta biologie 1', '2025-09-27 11:00:00', 6),
('Sedinta biologie 2', '2025-10-15 17:00:00', 6),
('Sedinta biologie 3', '2025-10-26 13:00:00', 6),
('Sedinta limba romana 1', '2025-09-14 15:00:00', 8),
('Sedinta limba romana 2', '2025-09-21 13:00:00', 8),
('Sedinta fizica 1', '2025-09-29 15:00:00', 9),
('Sedinta fizica 2', '2025-10-01 19:00:00', 9),
('Sedinta fizica 3', '2025-10-09 16:00:00', 9),
('Sedinta fizica 4', '2025-10-18 13:00:00', 9);

SELECT * FROM Classes;

INSERT INTO StudentGroup (Sid, Gid)
VALUES
(1, 2),
(2, 3),
(4, 5),
(3, 4),
(2, 5),
(10, 3),
(7, 6),
(6, 7),
(5, 8),
(4, 9),
(5, 5),
(9, 9),
(5, 9),
(5, 7);

SELECT * FROM StudentGroup;

INSERT INTO TeacherGroup(Tid, Gid) VALUES
(1, 2),
(2, 3),
(1, 3),
(3, 5),
(4, 2),
(5, 8),
(6, 4),
(7, 6),
(8, 8),
(9, 7),
(10, 9);

SELECT * FROM TeacherGroup;

INSERT INTO Ratings(Value, Sid, Tid) VALUES
(4, 1, 1),
(2, 5, 3),
(1, 9, 10),
(5, 4, 10),
(4, 5, 9),
(3, 6, 9),
(3, 7, 6);

SELECT * FROM Ratings;

INSERT INTO Homeworks(ProblemNumber, DueDate) VALUES
(1, '2025-10-21 18:00:00'),
(5, '2025-10-23 15:00:00'),
(5, '2025-11-05 16:00:00'),
(2, '2025-10-07 17:30:00'),
(3, '2025-11-09 23:59:00'),
(6, '2025-11-11 11:59:00'),
(4, '2025-11-13 12:00:00'),
(8, '2025-09-15 13:30:00');

SELECT * FROM Homeworks;

INSERT INTO HomeworkStudent(Hid, Sid) VALUES
(25, 1),
(26, 2),
(27, 3),
(28, 4),
(29, 5),
(30, 6),
(31, 7),
(32, 8),
(32, 9),
(27, 6),
(25, 2);

SELECT * FROM HomeworkStudent;




