-- V5__Seed_users_and_artists.sql

-- Users
INSERT INTO users (uuid, external_auth_id, username, email, firstname, lastname, birthdate, picture_url, created_at,
                   country_id, role_id, status_id)
VALUES (gen_random_uuid(), 'auth_user_esp_001', 'pablo_ruiz', 'pablo.ruiz@example.com', 'Pablo', 'Ruiz', '1992-05-20',
        'http://example.com/pics/pablo_r.jpg', NOW() - INTERVAL '100 days', 1, 1, 2),
       (gen_random_uuid(), 'auth_user_esp_002', 'sofia_g88', 'sofia.garcia@example.com', 'Sofía', 'García', '1990-08-11', NULL,
        NOW() - INTERVAL '95 days', 1, 1, 2),
       (gen_random_uuid(), 'auth_user_esp_003', 'carlos_mrtnz', 'carlos.martinez@example.com', 'Carlos', 'Martínez', '1996-03-01',
        'http://example.com/pics/carlos_m.png', NOW() - INTERVAL '90 days', 1, 1, 2),
       (gen_random_uuid(), 'auth_user_esp_004', 'laura_lpz', 'laura.lopez@example.com', 'Laura', 'López', '1993-11-25', NULL,
        NOW() - INTERVAL '85 days', 1, 1, 2),
       (gen_random_uuid(), 'auth_user_esp_005', 'david_sanchez', 'david.sanchez@example.com', 'David', 'Sánchez', '1978-06-14',
        'http://example.com/pics/david_s.jpg', NOW() - INTERVAL '80 days', 1, 1, 2),
       (gen_random_uuid(), 'auth_user_esp_006', 'elena_fdez', 'elena.fernandez@example.com', 'Elena', 'Fernández', '1999-01-03', NULL,
        NOW() - INTERVAL '75 days', 1, 1, 2),
       (gen_random_uuid(), 'auth_user_esp_007', 'javier_perez', 'javier.perez@example.com', 'Javier', 'Pérez', '1965-04-19',
        'http://example.com/pics/javier_p.jpg', NOW() - INTERVAL '70 days', 1, 1, 2),
       (gen_random_uuid(), 'auth_user_esp_008', 'maria_rgz', 'maria.rodriguez@example.com', 'María', 'Rodríguez', '1991-09-07', NULL,
        NOW() - INTERVAL '65 days', 1, 1, 2),
       (gen_random_uuid(), 'auth_user_esp_009', 'antonio_gomez', 'antonio.gomez@example.com', 'Antonio', 'Gómez', '1985-12-30',
        'http://example.com/pics/antonio_g.png', NOW() - INTERVAL '60 days', 1, 1, 2),
       (gen_random_uuid(), 'auth_user_esp_010', 'ana_diaz', 'ana.diaz@example.com', 'Ana', 'Díaz', '1989-02-18', NULL,
        NOW() - INTERVAL '55 days', 1, 1, 2),
       (gen_random_uuid(), 'auth_user_esp_011', 'miguel_moreno', 'miguel.moreno@example.com', 'Miguel', 'Moreno', '1994-07-22',
        'http://example.com/pics/miguel_m.jpg', NOW() - INTERVAL '50 days', 1, 1, 2),
       (gen_random_uuid(), 'auth_user_esp_012', 'isabella_ruiz', 'isabella.ruiz@example.com', 'Isabella', 'Ruiz', '1997-10-05', NULL,
        NOW() - INTERVAL '45 days', 1, 1, 2),
       (gen_random_uuid(), 'auth_user_esp_013', 'ricardo_alvarez', 'ricardo.alvarez@example.com', 'Ricardo', 'Álvarez', '1980-01-28',
        'http://example.com/pics/ricardo_a.jpg', NOW() - INTERVAL '40 days', 1, 1, 2),
       (gen_random_uuid(), 'auth_user_esp_014', 'carmen_jmnz', 'carmen.jimenez@example.com', 'Carmen', 'Jiménez', '1995-04-04', NULL,
        NOW() - INTERVAL '35 days', 1, 1, 2),
       (gen_random_uuid(), 'auth_user_esp_015', 'sergio_navarro', 'sergio.navarro@example.com', 'Sergio', 'Navarro', '1998-08-16',
        'http://example.com/pics/sergio_n.png', NOW() - INTERVAL '30 days', 1, 1, 2)
ON CONFLICT (external_auth_id) DO NOTHING;

-- Artists
INSERT INTO artists (spotify_id, artist_name, picture_url)
VALUES ('spotify:artist:VetustaMorlaId', 'Vetusta Morla', 'http://example.com/artist_pics/vetusta.jpg'),
       ('spotify:artist:RosaliaId', 'Rosalía', 'http://example.com/artist_pics/rosalia.jpg'),
       ('spotify:artist:IzalId', 'Izal', 'http://example.com/artist_pics/izal.jpg'),
       ('spotify:artist:CTanganaId', 'C. Tangana', 'http://example.com/artist_pics/ctangana.jpg'),
       ('spotify:artist:EstopaId', 'Estopa', 'http://example.com/artist_pics/estopa.jpg'),
       ('spotify:artist:MelendiId', 'Melendi', 'http://example.com/artist_pics/melendi.jpg'),
       ('spotify:artist:AlejandroSanzId', 'Alejandro Sanz', 'http://example.com/artist_pics/sanz.jpg'),
       ('spotify:artist:LaOrejaDeVanGoghId', 'La Oreja de Van Gogh', 'http://example.com/artist_pics/lodvg.jpg'),
       ('spotify:artist:LoriMeyersId', 'Lori Meyers', 'http://example.com/artist_pics/lorimeyers.jpg'),
       ('spotify:artist:LoveOfLesbianId', 'Love of Lesbian', 'http://example.com/artist_pics/loveoflesbian.jpg'),
       ('spotify:artist:KaseOId', 'Kase.O', 'http://example.com/artist_pics/kaseo.jpg'),
       ('spotify:artist:SilviaPerezCruzId', 'Sílvia Pérez Cruz', 'http://example.com/artist_pics/silviaperezcruz.jpg')
ON CONFLICT (spotify_id) DO NOTHING;
