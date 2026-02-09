CREATE DATABASE Varianta1Seminar;

use Varianta1Seminar;

CREATE TABLE Type(
	Id INT PRIMARY KEY IDENTITY,
	Description VARCHAR(100)
)

CREATE TABLE Train(
	Name VARCHAR(50),
	TypeId INT NOT NULL
)

ALTER TABLE Train
ADD CONSTRAINT fk_type FOREIGN KEY (TypeId) REFERENCES Type(Id);

ALTER TABLE Train
ADD Id INT PRIMARY KEY IDENTITY;

CREATE TABLE Station(
	Id INT PRIMARY KEY IDENTITY,
	Name VARCHAR(50)
);

CREATE TABLE Route(
	Id INT PRIMARY KEY IDENTITY,
	TrainId INT FOREIGN KEY REFERENCES Train(Id)
)

ALTER TABLE Route
ADD Name VARCHAR(50);

CREATE TABLE RouteStation(
	RouteId INT FOREIGN KEY REFERENCES Route(Id),
	StationId INT FOREIGN KEY REFERENCES Station(Id),
	ArriveTime TIME NOT NULL,
	LeaveTime TIME NOT NULL
)

ALTER TABLE RouteStation
ALTER COLUMN RouteId INT NOT NULL;

ALTER TABLE RouteStation
ALTER COLUMN StationId INT NOT NULL;

ALTER TABLE RouteStation ADD CONSTRAINT
pk_route_station PRIMARY KEY (RouteId, StationId);

INSERT INTO Type(Description) VALUES
('Interregional'),
('Regional'),
('International');

INSERT INTO Train(Name, TypeId) VALUES
('Tren1', 1),
('Tren2', 1),
('Tren3', 2),
('Tren4', 3),
('Tren5', 2);

INSERT INTO Station(Name) VALUES
('Micro 3'),
('Centru'),
('Materna');

INSERT INTO Route(TrainId) VALUES
(1),
(2),
(3),
(4);

INSERT INTO RouteStation(RouteId, StationId, ArriveTime, LeaveTime) VALUES
(1, 1, '8:30', '8:40'),
(2, 3, '9:20', '9:30'),
(3, 2, '12:00', '12:10'),
(4, 1, '15:40', '15:50'),
(1, 2, '19:50', '20:00'),
(2, 1, '21:20', '21:30'),
(3, 3, '23:30', '23:40');

CREATE OR ALTER PROCEDURE InsertStation
@RouteId INT,
@StationId INT,
@ArriveTime TIME,
@LeaveTime TIME
AS
BEGIN
	IF (SELECT COUNT(*) FROM RouteStation WHERE RouteId = @RouteId AND StationId = @StationId) = 0
	BEGIN
		INSERT INTO RouteStation(RouteId, StationId, ArriveTime, LeaveTime) VALUES
		(@RouteId, @StationId, @ArriveTime, @LeaveTime);
		RETURN
	END

	UPDATE RouteStation 
	SET ArriveTime = @ArriveTime, LeaveTime = @LeaveTime
	WHERE RouteId = @RouteId AND StationId = @StationId;
END

EXEC InsertStation @RouteId = 1, @StationId = 3, @ArriveTime = '1:00', @LeaveTime = '1:10';

SELECT * FROM RouteStation;

CREATE OR ALTER View AllStationRoutes
AS
SELECT R.Name FROM Route R WHERE
(SELECT COUNT(*) FROM RouteStation WHERE RouteId = R.Id) =
(SELECT COUNT(*) FROM Station);

CREATE OR ALTER View AllStationRoutes2
AS
SELECT R.Name FROM Route R 
INNER JOIN RouteStation RS ON RS.RouteId = R.Id
GROUP BY R.Id, R.Name
HAVING COUNT(R.id) = (SELECT COUNT(*) FROM Station);

SELECT * FROM AllStationRoutes2;