-- V4__Seed_lookup_tables.sql

INSERT INTO countries (code, name)
VALUES ('ES', 'España') ON CONFLICT (code) DO NOTHING;

INSERT INTO user_roles (code, name)
VALUES ('USER', 'Normal user'),
       ('ADMIN', 'Administrator'),
       ('MODERATOR', 'Moderator') ON CONFLICT (code) DO NOTHING;

INSERT INTO moderation_statuses (code, name, description)
VALUES ('PENDING', 'Pending', 'Content pending manual review'),
       ('APPROVED', 'Approved', 'Content formally approved after review'),
       ('REJECTED', 'Rejected', 'Content removed by moderatios after review') ON CONFLICT (code) DO NOTHING;
