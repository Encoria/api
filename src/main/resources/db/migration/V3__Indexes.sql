-- V3__Indexes.sql

-- Indexes for Foreign Keys and frequently queried columns
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_external_auth_id ON users(external_auth_id);

CREATE INDEX IF NOT EXISTS idx_users_country_id ON users(country_id);
CREATE INDEX IF NOT EXISTS idx_users_role_id ON users(role_id);
CREATE INDEX IF NOT EXISTS idx_users_status_id ON users(status_id);

CREATE INDEX IF NOT EXISTS idx_moments_user_id ON moments(user_id);
CREATE INDEX IF NOT EXISTS idx_moments_status_id ON moments(status_id);

CREATE INDEX IF NOT EXISTS idx_moment_media_moment_id ON moment_media(moment_id);

CREATE INDEX IF NOT EXISTS idx_publications_user_id ON publications(user_id);
CREATE INDEX IF NOT EXISTS idx_publications_moment_id ON publications(moment_id);
CREATE INDEX IF NOT EXISTS idx_publications_status_id ON publications(status_id);

CREATE INDEX IF NOT EXISTS idx_publication_comments_user_id ON publication_comments(user_id);
CREATE INDEX IF NOT EXISTS idx_publication_comments_publication_id ON publication_comments(publication_id);
CREATE INDEX IF NOT EXISTS idx_publication_comments_status_id ON publication_comments(status_id);

CREATE INDEX IF NOT EXISTS idx_publication_likes_publication_id ON publication_likes(publication_id);

CREATE INDEX IF NOT EXISTS idx_user_followers_follower_id ON user_followers(follower_id);
