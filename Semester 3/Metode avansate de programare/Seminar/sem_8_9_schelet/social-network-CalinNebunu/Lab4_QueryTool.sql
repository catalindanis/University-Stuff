-- CREAREA TABELELOR

CREATE TABLE users(
	id BIGSERIAL PRIMARY KEY,
	username VARCHAR(100) NOT NULL UNIQUE,
	email VARCHAR(100) NOT NULL UNIQUE,
	password VARCHAR(100) NOT NULL
);

CREATE TABLE cards (
    id BIGSERIAL PRIMARY KEY,
    nume_card VARCHAR(100) NOT NULL UNIQUE,
    tip_membri VARCHAR(100) NOT NULL
);


CREATE TABLE ducks (
    id BIGINT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
    viteza DOUBLE PRECISION NOT NULL,
    rezistenta DOUBLE PRECISION NOT NULL,
    card_id BIGINT REFERENCES cards(id) ON DELETE SET NULL,
    duck_type VARCHAR(20) NOT NULL
);



CREATE TABLE persoane(
	id BIGINT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
	nume VARCHAR(100) NOT NULL,
	prenume VARCHAR(100) NOT NULL, 
	data_nasterii DATE NOT NULL, 
	ocupatie VARCHAR(100) NOT NULL,
	nivel_empatie INT NOT NULL
);


-- ADAUGAREA CONSTRANGERILOR

-- --------------------------
-- USERS
-- --------------------------
ALTER TABLE users
ADD CONSTRAINT chk_user_username_len CHECK (char_length(username) BETWEEN 5 AND 50);

ALTER TABLE users
ADD CONSTRAINT chk_user_password_len CHECK (char_length(password) BETWEEN 5 AND 50);

ALTER TABLE users
ADD CONSTRAINT chk_user_email_len CHECK (char_length(email) BETWEEN 5 AND 50);

ALTER TABLE users
ADD CONSTRAINT chk_user_email_format CHECK (position('@' in email) > 1);


-- --------------------------
-- CARDS
-- --------------------------
ALTER TABLE cards
ADD CONSTRAINT chk_card_name_len CHECK (char_length(nume_card) BETWEEN 1 AND 20);

ALTER TABLE cards
ADD CONSTRAINT chk_card_type CHECK (tip_membri IN ('swimming', 'flying', 'hybrid'));

-- --------------------------
-- DUCKS
-- --------------------------
ALTER TABLE ducks
ADD CONSTRAINT chk_duck_viteza_positive CHECK (viteza > 0);

ALTER TABLE ducks
ADD CONSTRAINT chk_duck_rezistenta_positive CHECK (rezistenta > 0);

ALTER TABLE ducks
ADD CONSTRAINT chk_duck_type CHECK (duck_type IN ('swimming', 'flying', 'hybrid'));


-- --------------------------
-- PERSOANE
-- --------------------------
ALTER TABLE persoane
ADD CONSTRAINT chk_persoana_nume_len CHECK (char_length(nume) >= 2);

ALTER TABLE persoane
ADD CONSTRAINT chk_persoana_prenume_len CHECK (char_length(prenume) >= 2);

ALTER TABLE persoane
ADD CONSTRAINT chk_persoana_ocupatie_len CHECK (char_length(ocupatie) >= 2);

ALTER TABLE persoane
ADD CONSTRAINT chk_persoana_empatie_range CHECK (nivel_empatie BETWEEN 0 AND 10);

ALTER TABLE persoane
ADD CONSTRAINT chk_persoana_data_range CHECK (data_nasterii BETWEEN DATE '1960-01-01' AND DATE '2007-01-01');


-- --------------------------
-- USERS
-- --------------------------
INSERT INTO users (username, email, password) VALUES 
('quackMaster', 'quack@domain.com', 'secret123'),
('pondHero', 'hero@lake.com', 'mypassword'),
('johnDoe', 'john.doe@example.com', '12345');

-- --------------------------
-- CARDS
-- --------------------------
INSERT INTO cards (nume_card, tip_membri) VALUES
('Alpha Ducks', 'Duck'),
('Beta Squad', 'Duck');

-- --------------------------
-- DUCKS
-- --------------------------
INSERT INTO ducks (id, viteza, rezistenta, card_id) VALUES
(1, 12.5, 8.0, 1),
(2, 10.0, 9.5, 2);

-- --------------------------
-- PERSOANE
-- --------------------------
INSERT INTO persoane (id, nume, prenume, data_nasterii, ocupatie, nivel_empatie) VALUES
(3, 'Doe', 'John', '1998-06-15', 'Student', 7);

SELECT * FROM ducks d INNER JOIN users u ON u.id = d.id

SELECT * FROM cards
