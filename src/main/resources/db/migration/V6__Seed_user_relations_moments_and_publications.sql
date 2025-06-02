-- V6__Seed_user_relations_moments_and_publications.sql

-- User Settings
INSERT INTO user_settings (user_id)
VALUES (1), (2), (3), (4), (5), (6), (7), (8), (9), (10), (11), (12), (13), (14), (15)
ON CONFLICT (user_id) DO NOTHING;

-- User Followers
INSERT INTO user_followers (user_id, follower_id, follows_since, approved)
VALUES (1, 2, NOW() - INTERVAL '80 days', TRUE),
       (2, 1, NOW() - INTERVAL '78 days', TRUE),
       (3, 1, NOW() - INTERVAL '70 days', TRUE),
       (4, 1, NOW() - INTERVAL '65 days', FALSE),
       (1, 5, NOW() - INTERVAL '60 days', TRUE),
       (5, 2, NOW() - INTERVAL '58 days', TRUE),
       (6, 8, NOW() - INTERVAL '50 days', TRUE),
       (8, 6, NOW() - INTERVAL '48 days', TRUE),
       (11, 1, NOW() - INTERVAL '40 days', TRUE),
       (11, 2, NOW() - INTERVAL '38 days', TRUE),
       (15, 4, NOW() - INTERVAL '30 days', TRUE),
       (4, 15, NOW() - INTERVAL '28 days', TRUE)
ON CONFLICT (user_id, follower_id) DO NOTHING;

-- Moments
INSERT INTO moments (uuid, title, description, latitude, longitude, date, created_at, user_id, status_id, artist_id)
VALUES (gen_random_uuid(), 'Vetusta Morla en Madrid', 'Concierto de presentación de su nuevo álbum en el WiZink Center.', 40.4336, -3.6635,
        '2024-11-15', NOW() - INTERVAL '60 days', 2, 2, 1),
       (gen_random_uuid(), 'Rosalía - Motomami Tour Barcelona', 'El espectacular show de Rosalía en el Palau Sant Jordi.', 41.3648, 2.1525,
        '2024-12-01', NOW() - INTERVAL '70 days', 1, 2, 2),
       (gen_random_uuid(), 'Izal - Gira Despedida Sevilla', 'Últimos conciertos de Izal en el Auditorio Rocío Jurado.', 37.4008, -6.0088,
        '2024-10-25', NOW() - INTERVAL '50 days', 3, 2, 3),
       (gen_random_uuid(), 'FIB Benicàssim 2024', 'Festival Internacional de Benicàssim. Varios días de música.', 40.0614, 0.1238,
        '2024-07-18', NOW() - INTERVAL '100 days', 11, 2, 4),
       (gen_random_uuid(), 'C. Tangana en Bilbao', 'La gira de El Madrileño en el Bizkaia Arena BEC!', 43.2658, -2.9966, '2024-11-05',
        NOW() - INTERVAL '40 days', 4, 2, 5),
       (gen_random_uuid(), 'Estopa - 25 Aniversario Madrid', 'Celebrando 25 años en el Estadio Metropolitano.', 40.4361, -3.5996,
        '2024-12-10', NOW() - INTERVAL '80 days', 2, 2, 6),
       (gen_random_uuid(), 'Primavera Sound Barcelona 2025', 'Uno de los festivales más importantes de Europa en el Parc del Fòrum.',
        41.4131, 2.2205, '2025-05-29', NOW() - INTERVAL '150 days', 1, 2, 7),
       (gen_random_uuid(), 'Melendi en Valencia', 'Concierto en la Ciudad de las Artes y las Ciencias.', 39.4631, -0.3539, '2024-11-20',
        NOW() - INTERVAL '55 days', 5, 2, 8),
       (gen_random_uuid(), 'Alejandro Sanz en Málaga', 'Tour 2024 en el Palacio de Deportes Martín Carpena.', 36.7055, -4.4578,
        '2024-12-05', NOW() - INTERVAL '65 days', 7, 2, 9),
       (gen_random_uuid(), 'La Oreja de Van Gogh Oviedo', 'Concierto en el Auditorio Príncipe Felipe.', 43.3603, -5.8447, '2024-10-30',
        NOW() - INTERVAL '45 days', 10, 2, 10),
       (gen_random_uuid(), 'Concierto Flamenco Cádiz', 'Una noche de arte flamenco en el Teatro Falla.', 36.5323, -6.2963, '2024-11-08',
        NOW() - INTERVAL '35 days', 9, 2, 11),
       (gen_random_uuid(), 'Noche Electrónica Razzmatazz', 'Sesión con DJ internacional en Sala Razzmatazz.', 41.4010, 2.1968,
        '2024-10-18', NOW() - INTERVAL '20 days', 6, 2, 12),
       (gen_random_uuid(), 'Jazz en Café Central Madrid', 'Jazz íntimo en un club clásico.', 40.4156, -3.7000, '2024-11-01',
        NOW() - INTERVAL '25 days', 5, 2, 1),
       (gen_random_uuid(), 'Gira Rock Nacional La Riviera', 'Varias bandas de rock español en Sala La Riviera.', 40.4120, -3.7180,
        '2024-11-22', NOW() - INTERVAL '40 days', 13, 2, 3),
       (gen_random_uuid(), 'Festival BBK Live 2024', 'Festival en Kobetamendi, Bilbao.', 43.2560, -2.9660, '2024-07-11',
        NOW() - INTERVAL '90 days', 4, 2, 5),
       (gen_random_uuid(), 'Mad Cool Festival 2023', 'Festival de verano en Madrid.', 40.4050, -3.5800, '2023-07-06',
        NOW() - INTERVAL '400 days', 2, 2, 7),
       (gen_random_uuid(), 'Sonar Barcelona 2023', 'Festival de música avanzada y arte multimedia.', 41.3740, 2.1490, '2023-06-15',
        NOW() - INTERVAL '420 days', 6, 2, 9),
       (gen_random_uuid(), 'Concierto Local Pendiente', 'Descripción del concierto local.', 40.0, -3.0, '2025-01-20',
        NOW() - INTERVAL '10 days', 1, 1, 11)
ON CONFLICT (uuid) DO NOTHING;

-- Moment Media
INSERT INTO moment_media (media_url, media_type, position, created_at, moment_id)
VALUES  ('https://static.vecteezy.com/system/resources/thumbnails/027/104/127/small_2x/cheering-crowd-illuminated-by-vibrant-stage-lights-at-concert-photo.jpg', 'IMAGE', 0, NOW() - INTERVAL '58 days', 1),
        ('http://example.com/media/vetusta_madrid_2.mp4', 'VIDEO', 1, NOW() - INTERVAL '57 days', 1),
        ('https://www.rollingstone.com/wp-content/uploads/2022/10/Post-Pandemic-and-Beyond-Looking-Ahead-to-the-Future-of-Live-Concerts.jpg', 'IMAGE', 0, NOW() - INTERVAL '68 days', 2),
        ('https://www.shutterstock.com/image-photo/crowd-partying-stage-lights-live-600nw-2297236461.jpg', 'IMAGE', 0, NOW() - INTERVAL '48 days', 3),
        ('https://d117kfg112vbe4.cloudfront.net/public/Royal-Albert-Hall-DAMS/Events/Auditorium/2022/22-05-16-Bonobo/073.jpg?type=image&id=154&token=4ec5257f&mode=fill&top=85&width=1200&height=630&format=webp', 'IMAGE', 0, NOW() - INTERVAL '98 days', 4),
        ('https://i.guim.co.uk/img/media/dd2344a2c6897c9751eff292f01810a020d05257/0_339_5568_3341/master/5568.jpg?width=1200&quality=85&auto=format&fit=max&s=b56dc9ca6cc142881261a29af5a15628', 'IMAGE', 1, NOW() - INTERVAL '97 days', 4),
        ('https://static.vecteezy.com/system/resources/thumbnails/027/104/127/small_2x/cheering-crowd-illuminated-by-vibrant-stage-lights-at-concert-photo.jpg', 'IMAGE', 0, NOW() - INTERVAL '38 days', 5),
        ('https://www.rollingstone.com/wp-content/uploads/2022/10/Post-Pandemic-and-Beyond-Looking-Ahead-to-the-Future-of-Live-Concerts.jpg', 'IMAGE', 0, NOW() - INTERVAL '78 days', 6),
        ('https://www.shutterstock.com/image-photo/crowd-partying-stage-lights-live-600nw-2297236461.jpg', 'IMAGE', 0, NOW() - INTERVAL '148 days', 7),
        ('https://d117kfg112vbe4.cloudfront.net/public/Royal-Albert-Hall-DAMS/Events/Auditorium/2022/22-05-16-Bonobo/073.jpg?type=image&id=154&token=4ec5257f&mode=fill&top=85&width=1200&height=630&format=webp', 'IMAGE', 0, NOW() - INTERVAL '53 days', 8),
        ('https://i.guim.co.uk/img/media/dd2344a2c6897c9751eff292f01810a020d05257/0_339_5568_3341/master/5568.jpg?width=1200&quality=85&auto=format&fit=max&s=b56dc9ca6cc142881261a29af5a15628', 'IMAGE', 0, NOW() - INTERVAL '63 days', 9),
        ('https://static.vecteezy.com/system/resources/thumbnails/027/104/127/small_2x/cheering-crowd-illuminated-by-vibrant-stage-lights-at-concert-photo.jpg', 'IMAGE', 0, NOW() - INTERVAL '43 days', 10),
        ('https://www.rollingstone.com/wp-content/uploads/2022/10/Post-Pandemic-and-Beyond-Looking-Ahead-to-the-Future-of-Live-Concerts.jpg', 'IMAGE', 0, NOW() - INTERVAL '33 days', 11),
        ('https://www.shutterstock.com/image-photo/crowd-partying-stage-lights-live-600nw-2297236461.jpg', 'IMAGE', 0, NOW() - INTERVAL '18 days', 12),
        ('https://d117kfg112vbe4.cloudfront.net/public/Royal-Albert-Hall-DAMS/Events/Auditorium/2022/22-05-16-Bonobo/073.jpg?type=image&id=154&token=4ec5257f&mode=fill&top=85&width=1200&height=630&format=webp', 'IMAGE', 0, NOW() - INTERVAL '23 days', 13),
        ('https://i.guim.co.uk/img/media/dd2344a2c6897c9751eff292f01810a020d05257/0_339_5568_3341/master/5568.jpg?width=1200&quality=85&auto=format&fit=max&s=b56dc9ca6cc142881261a29af5a15628', 'IMAGE', 0, NOW() - INTERVAL '38 days', 14),
        ('https://static.vecteezy.com/system/resources/thumbnails/027/104/127/small_2x/cheering-crowd-illuminated-by-vibrant-stage-lights-at-concert-photo.jpg', 'IMAGE', 0, NOW() - INTERVAL '88 days', 15),
        ('http://example.com/media/madcool_2023_recap.mp4', 'VIDEO', 0, NOW() - INTERVAL '350 days', 16),
        ('https://www.rollingstone.com/wp-content/uploads/2022/10/Post-Pandemic-and-Beyond-Looking-Ahead-to-the-Future-of-Live-Concerts.jpg', 'IMAGE', 0, NOW() - INTERVAL '340 days', 17)
ON CONFLICT DO NOTHING;

-- Publications
INSERT INTO publications (uuid, created_at, moment_id, user_id, status_id)
VALUES (gen_random_uuid(), NOW() - INTERVAL '59 days', 1, 1, 2),
       (gen_random_uuid(), NOW() - INTERVAL '69 days', 2, 2, 2),
       (gen_random_uuid(), NOW() - INTERVAL '49 days', 3, 3, 2),
       (gen_random_uuid(), NOW() - INTERVAL '99 days', 4, 11, 2),
       (gen_random_uuid(), NOW() - INTERVAL '39 days', 5, 4, 2),
       (gen_random_uuid(), NOW() - INTERVAL '79 days', 6, 2, 2),
       (gen_random_uuid(), NOW() - INTERVAL '149 days', 7, 1, 2),
       (gen_random_uuid(), NOW() - INTERVAL '54 days', 8, 5, 2),
       (gen_random_uuid(), NOW() - INTERVAL '64 days', 9, 7, 2),
       (gen_random_uuid(), NOW() - INTERVAL '44 days', 10, 10, 2),
       (gen_random_uuid(), NOW() - INTERVAL '34 days', 11, 9, 2),
       (gen_random_uuid(), NOW() - INTERVAL '19 days', 12, 6, 2),
       (gen_random_uuid(), NOW() - INTERVAL '24 days', 13, 5, 2),
       (gen_random_uuid(), NOW() - INTERVAL '39 days', 14, 13, 2),
       (gen_random_uuid(), NOW() - INTERVAL '89 days', 15, 4, 2),
       (gen_random_uuid(), NOW() - INTERVAL '360 days', 16, 4, 2),
       (gen_random_uuid(), NOW() - INTERVAL '5 days', 18, 1, 1)
ON CONFLICT (moment_id) DO NOTHING;
