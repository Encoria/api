-- V1__Core_schema.sql
-- Core schema initialization

-- Enable pgcrypto for gen_random_uuid()
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- ENUM types
CREATE TYPE media_type_enum AS ENUM ('IMAGE', 'VIDEO');

-- Independent lookup tables
CREATE TABLE countries (
    id SERIAL PRIMARY KEY,
    code TEXT NOT NULL UNIQUE,
    name TEXT NOT NULL UNIQUE
);

CREATE TABLE user_roles (
    id SERIAL PRIMARY KEY,
    code TEXT NOT NULL UNIQUE,
    name TEXT NOT NULL UNIQUE
);

CREATE TABLE moderation_statuses (
    id SERIAL PRIMARY KEY,
    code TEXT NOT NULL UNIQUE,
    name TEXT NOT NULL UNIQUE,
    description TEXT
);

-- Core tables
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID NOT NULL UNIQUE DEFAULT gen_random_uuid(),
    external_auth_id TEXT NOT NULL UNIQUE,
    username TEXT NOT NULL UNIQUE,
    email TEXT NOT NULL UNIQUE,
    firstname TEXT,
    lastname TEXT,
    birthdate DATE,
    picture_url TEXT,
    created_at TIMESTAMPTZ DEFAULT now(),
    updated_at TIMESTAMPTZ,
    deleted_at TIMESTAMPTZ,
    country_id INTEGER,
    role_id INTEGER,
    status_id INTEGER,

    FOREIGN KEY (country_id) REFERENCES countries(id),
    FOREIGN KEY (role_id) REFERENCES user_roles(id),
    FOREIGN KEY (status_id) REFERENCES moderation_statuses(id)
);

CREATE TABLE artists (
    id SERIAL PRIMARY KEY,
    spotify_id TEXT NOT NULL UNIQUE,
    artist_name TEXT NOT NULL,
    picture_url TEXT
);

CREATE TABLE moments (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID NOT NULL UNIQUE DEFAULT gen_random_uuid(),
    title TEXT NOT NULL,
    description TEXT,
    latitude FLOAT NOT NULL,
    longitude FLOAT NOT NULL,
    date DATE NOT NULL,
    created_at TIMESTAMPTZ DEFAULT now(),
    updated_at TIMESTAMPTZ,
    deleted_at TIMESTAMPTZ,
    user_id BIGINT NOT NULL,
    status_id INTEGER,
    artist_id BIGINT,

    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (status_id) REFERENCES moderation_statuses(id),
    FOREIGN KEY (artist_id) REFERENCES artists(id)
);

CREATE TABLE publications (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID NOT NULL UNIQUE DEFAULT gen_random_uuid(),
    created_at TIMESTAMPTZ DEFAULT now(),
    updated_at TIMESTAMPTZ,
    deleted_at TIMESTAMPTZ,
    moment_id BIGINT NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    status_id INTEGER,

    FOREIGN KEY (moment_id) REFERENCES moments(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (status_id) REFERENCES moderation_statuses(id)
);
