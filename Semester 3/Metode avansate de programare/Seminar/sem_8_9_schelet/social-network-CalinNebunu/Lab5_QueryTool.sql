-- CREAREA TABELELOR

CREATE TABLE friendships (
	id BIGSERIAL PRIMARY KEY,
	user1_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
	user2_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE events (
    id BIGSERIAL PRIMARY KEY,
    descriere VARCHAR(100) NOT NULL,
    tip_event VARCHAR(50) NOT NULL
);

CREATE TABLE race_events (
    id BIGINT PRIMARY KEY REFERENCES events(id) ON DELETE CASCADE,
    distanta_balize DOUBLE PRECISION[]
);

CREATE TABLE event_subscribers (
	event_id BIGINT NOT NULL,
	user_id BIGINT NOT NULL,

	PRIMARY KEY (event_id, user_id),

	CONSTRAINT fk_event FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
	CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ADAUGAREA CONSTRANGERILOR

ALTER TABLE friendships
ADD CONSTRAINT uq_friendship_pair UNIQUE (user1_id, user2_id);

ALTER TABLE friendships
ADD CONSTRAINT chk_friendship_order CHECK (user1_id < user2_id);

select * from users
select * from persoane
select * from ducks
select * from cards
select * from friendships
select * from events
select * from race_events
select * from event_subscribers