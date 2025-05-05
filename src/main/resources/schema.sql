-- schema.sql
-- Based on ERD 5.1

-- Create ENUM types first
CREATE TYPE media_type_enum AS ENUM ('image', 'video');

-- Independent lookup tables first
CREATE TABLE IF NOT EXISTS countries (
    id SERIAL PRIMARY KEY,
    code TEXT NOT NULL UNIQUE,
    name TEXT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS user_roles (
    id SERIAL PRIMARY KEY,
    code TEXT NOT NULL UNIQUE,
    name TEXT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS moderation_statuses (
    id SERIAL PRIMARY KEY,
    code TEXT NOT NULL UNIQUE,
    name TEXT NOT NULL UNIQUE,
    description TEXT
);

-- Core tables
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID NOT NULL UNIQUE,
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

CREATE TABLE IF NOT EXISTS user_settings (
    user_id BIGINT PRIMARY KEY,
    is_private_profile BOOLEAN DEFAULT false,
    notify_comments BOOLEAN DEFAULT false,
    notify_likes BOOLEAN DEFAULT false,
    notify_follow BOOLEAN DEFAULT false,
    updated_at TIMESTAMPTZ,

    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_followers (
    user_id BIGINT NOT NULL,
    follower_id BIGINT NOT NULL,
    follows_since TIMESTAMPTZ DEFAULT now(),
    approved BOOLEAN NOT NULL DEFAULT false,

    PRIMARY KEY (user_id, follower_id), -- Composite primary key

    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (follower_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS artists (
    id SERIAL PRIMARY KEY,
    spotify_id TEXT NOT NULL UNIQUE,
    artist_name TEXT NOT NULL,
    picture_url TEXT
);

CREATE TABLE IF NOT EXISTS moments (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID NOT NULL UNIQUE,
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

    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (status_id) REFERENCES moderation_statuses(id)
);

CREATE TABLE IF NOT EXISTS moment_media (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID NOT NULL UNIQUE,
    media_url TEXT NOT NULL,
    media_type media_type_enum NOT NULL,
    position INTEGER NOT NULL,
    created_at TIMESTAMPTZ DEFAULT now(),
    updated_at TIMESTAMPTZ,
    deleted_at TIMESTAMPTZ,
    moment_id BIGINT NOT NULL,

    FOREIGN KEY (moment_id) REFERENCES moments(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS moment_artists (
    artist_id INTEGER NOT NULL,
    moment_id BIGINT NOT NULL,

    PRIMARY KEY (artist_id, moment_id), -- Composite primary key

    FOREIGN KEY (artist_id) REFERENCES artists(id) ON DELETE CASCADE,
    FOREIGN KEY (moment_id) REFERENCES moments(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS publications (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID NOT NULL UNIQUE,
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

CREATE TABLE IF NOT EXISTS publication_comments (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID NOT NULL UNIQUE,
    content TEXT NOT NULL,
    created_at TIMESTAMPTZ DEFAULT now(),
    updated_at TIMESTAMPTZ,
    deleted_at TIMESTAMPTZ,
    user_id BIGINT NOT NULL,
    publication_id BIGINT NOT NULL,
    status_id INTEGER,

    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (publication_id) REFERENCES publications(id) ON DELETE CASCADE,
    FOREIGN KEY (status_id) REFERENCES moderation_statuses(id)
);

CREATE TABLE IF NOT EXISTS publication_likes (
    user_id BIGINT NOT NULL,
    publication_id BIGINT NOT NULL,
    created_at TIMESTAMPTZ DEFAULT now(),

    PRIMARY KEY (user_id, publication_id),

    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (publication_id) REFERENCES publications(id) ON DELETE CASCADE
);

-- Add Indexes for Foreign Keys and frequently queried columns
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_external_auth_id ON users(external_auth_id);
CREATE INDEX IF NOT EXISTS idx_publications_user_id ON publications(user_id);
CREATE INDEX IF NOT EXISTS idx_publication_comments_publication_id ON publication_comments(publication_id);
CREATE INDEX IF NOT EXISTS idx_moment_media_moment_id ON moment_media(moment_id);
