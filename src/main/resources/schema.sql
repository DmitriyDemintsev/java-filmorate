CREATE TABLE IF NOT EXISTS users (
user_id  INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
email    varchar NOT NULL UNIQUE,
login    varchar NOT NULL UNIQUE,
name     varchar NOT NULL,
birthday DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS friends (
user_id  INTEGER NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
friend_id  INTEGER NOT NULL REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS mpa (
mpa_id        INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
name          varchar NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS films (
film_id        INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
name           varchar NOT NULL,
description    varchar(200),
releaseDate    DATE NOT NULL,
duration       INTEGER CHECK (duration>0),
mpa_id         INTEGER REFERENCES mpa (mpa_id)
);

CREATE TABLE IF NOT EXISTS genres (
genre_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
name     varchar NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS film_genre (
film_id  INTEGER REFERENCES films (film_id) ON DELETE CASCADE,
genre_id INTEGER REFERENCES genres (genre_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS likes (
likes_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
film_id  INTEGER NOT NULL REFERENCES films (film_id) ON DELETE CASCADE,
user_id  INTEGER NOT NULL REFERENCES users (user_id) ON DELETE CASCADE
);
