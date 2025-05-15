-- V2__Relations_settings_and_media.sql
-- Relation tables and dependent on core schema

-- User specific settings and relations
CREATE TABLE user_settings (
    user_id BIGINT PRIMARY KEY,
    is_private_profile BOOLEAN DEFAULT false,
    notify_comments BOOLEAN DEFAULT false,
    notify_likes BOOLEAN DEFAULT false,
    notify_follow BOOLEAN DEFAULT false,
    updated_at TIMESTAMPTZ,

    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE user_followers (
    user_id BIGINT NOT NULL,
    follower_id BIGINT NOT NULL,
    follows_since TIMESTAMPTZ DEFAULT now(),
    approved BOOLEAN NOT NULL DEFAULT false,

    PRIMARY KEY (user_id, follower_id),

    CONSTRAINT check_no_self_follow CHECK (user_id <> follower_id),

    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (follower_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Moment related tables
CREATE TABLE moment_media (
    id BIGSERIAL PRIMARY KEY,
    media_url TEXT NOT NULL,
    media_type media_type_enum NOT NULL,
    position INTEGER NOT NULL,
    created_at TIMESTAMPTZ DEFAULT now(),
    updated_at TIMESTAMPTZ,
    deleted_at TIMESTAMPTZ,
    moment_id BIGINT NOT NULL,

    FOREIGN KEY (moment_id) REFERENCES moments(id) ON DELETE CASCADE
);

CREATE TABLE moment_artists (
    artist_id INTEGER NOT NULL,
    moment_id BIGINT NOT NULL,

    PRIMARY KEY (artist_id, moment_id),

    FOREIGN KEY (artist_id) REFERENCES artists(id) ON DELETE CASCADE,
    FOREIGN KEY (moment_id) REFERENCES moments(id) ON DELETE CASCADE
);

-- Publication related tables
CREATE TABLE publication_comments (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID NOT NULL UNIQUE DEFAULT gen_random_uuid(),
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

CREATE TABLE publication_likes (
    user_id BIGINT NOT NULL,
    publication_id BIGINT NOT NULL,
    created_at TIMESTAMPTZ DEFAULT now(),

    PRIMARY KEY (user_id, publication_id),

    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (publication_id) REFERENCES publications(id) ON DELETE CASCADE
);
