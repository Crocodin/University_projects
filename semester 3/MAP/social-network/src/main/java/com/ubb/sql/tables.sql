
CREATE TABLE users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE person (
    user_id BIGINT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    occupation VARCHAR(100),
    empathy_score INT,
    CONSTRAINT fk_person_user FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE duck (
    user_id BIGINT PRIMARY KEY,
    type VARCHAR(50),
    speed INT,
    endurance INT,
    CONSTRAINT fk_duck_user FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE friendship (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_one BIGINT REFERENCES users(id) ON DELETE CASCADE,
    user_two BIGINT REFERENCES users(id) ON DELETE CASCADE
);

create table events (
	id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	name varchar(100)
);

create table event_subscribers (
	event_id BIGINT REFERENCES events(id) ON DELETE CASCADE,
	user_id  BIGINT REFERENCES users(id)  ON DELETE CASCADE,
	PRIMARY KEY (event_id, user_id)
);

create table race_events (
	event_id BIGINT PRIMARY KEY REFERENCES events(id) ON DELETE CASCADE
);

create table lane (
	id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	distance INT
);

CREATE TABLE flock (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE flock_duck (
    flock_id BIGINT REFERENCES flock(id) ON DELETE CASCADE,
    duck_id  BIGINT REFERENCES duck(user_id)  ON DELETE CASCADE,
    PRIMARY KEY (flock_id, duck_id)
);


create table message (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_from BIGINT REFERENCES users(id),
    user_to BIGINT REFERENCES users(id),
    message TEXT,
    timestamp TIMESTAMP
);

create table reply_message (
    id BIGINT PRIMARY KEY,
    reply_to BIGINT REFERENCES message(id),
    CONSTRAINT fk_message_reply FOREIGN KEY (id)
        REFERENCES message(id) ON DELETE CASCADE
);
