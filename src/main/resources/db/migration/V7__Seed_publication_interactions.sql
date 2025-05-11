-- V7__Seed_publication_interactions.sql

-- Publication Comments
INSERT INTO publication_comments (uuid, content, created_at, user_id, publication_id, status_id)
VALUES (gen_random_uuid(), '¡Yo también voy! ¡Nos vemos allí!', NOW() - INTERVAL '57 days', 3, 1, 2),
       (gen_random_uuid(), '¡Qué envidia! Disfruta mucho.', NOW() - INTERVAL '56 days', 5, 1, 2),
       (gen_random_uuid(), 'El Palau Sant Jordi es increíble para conciertos.', NOW() - INTERVAL '67 days', 1, 2, 2),
       (gen_random_uuid(), 'Su despedida será épica.', NOW() - INTERVAL '47 days', 13, 3, 2),
       (gen_random_uuid(), '¡El cartel del FIB es brutal este año!', NOW() - INTERVAL '95 days', 15, 4, 2),
       (gen_random_uuid(), '¿Ya tienes tu entrada?', NOW() - INTERVAL '94 days', 8, 4, 2),
       (gen_random_uuid(), 'El Madrileño en directo es una pasada.', NOW() - INTERVAL '37 days', 1, 5, 2),
       (gen_random_uuid(), '25 años y siguen en forma.', NOW() - INTERVAL '75 days', 9, 6, 2),
       (gen_random_uuid(), 'Soñando con el Primavera Sound...', NOW() - INTERVAL '140 days', 6, 7, 2),
       (gen_random_uuid(), 'Melendi nunca defrauda.', NOW() - INTERVAL '50 days', 10, 8, 2),
       (gen_random_uuid(), 'Su voz es única.', NOW() - INTERVAL '60 days', 14, 9, 2),
       (gen_random_uuid(), '¡Qué recuerdos con LODVG!', NOW() - INTERVAL '40 days', 3, 10, 2),
       (gen_random_uuid(), 'El arte del flamenco en estado puro.', NOW() - INTERVAL '30 days', 7, 11, 2),
       (gen_random_uuid(), 'Razzmatazz es siempre un acierto para la electrónica.', NOW() - INTERVAL '15 days', 12, 12, 2),
       (gen_random_uuid(), 'El Café Central es un clásico.', NOW() - INTERVAL '20 days', 1, 13, 2),
       (gen_random_uuid(), 'Apoyando el rock nacional!', NOW() - INTERVAL '35 days', 4, 14, 2),
       (gen_random_uuid(), 'El BBK Live es mi festival favorito.', NOW() - INTERVAL '85 days', 15, 15, 2),
       (gen_random_uuid(), 'Mad Cool 2023 fue increíble, a pesar del calor.', NOW() - INTERVAL '340 days', 11, 16, 2),
       (gen_random_uuid(), 'Este comentario está pendiente de moderación.', NOW() - INTERVAL '2 days', 2, 1, 1),
       (gen_random_uuid(), 'Este comentario fue rechazado.', NOW() - INTERVAL '3 days', 3, 2, 3)
ON CONFLICT (uuid) DO NOTHING;

-- Publication Likes
INSERT INTO publication_likes (user_id, publication_id, created_at)
VALUES (2, 1, NOW() - INTERVAL '58 days'),
       (3, 1, NOW() - INTERVAL '57 days'),
       (1, 2, NOW() - INTERVAL '68 days'),
       (4, 2, NOW() - INTERVAL '66 days'),
       (1, 3, NOW() - INTERVAL '48 days'),
       (2, 3, NOW() - INTERVAL '47 days'),
       (5, 3, NOW() - INTERVAL '46 days'),
       (1, 4, NOW() - INTERVAL '98 days'),
       (2, 4, NOW() - INTERVAL '97 days'),
       (3, 4, NOW() - INTERVAL '96 days'),
       (4, 4, NOW() - INTERVAL '95 days'),
       (11, 5, NOW() - INTERVAL '38 days'),
       (13, 5, NOW() - INTERVAL '37 days'),
       (1, 6, NOW() - INTERVAL '78 days'),
       (3, 6, NOW() - INTERVAL '77 days'),
       (5, 8, NOW() - INTERVAL '53 days'),
       (10, 8, NOW() - INTERVAL '52 days'),
       (14, 9, NOW() - INTERVAL '63 days'),
       (3, 10, NOW() - INTERVAL '43 days'),
       (1, 13, NOW() - INTERVAL '23 days'),
       (4, 14, NOW() - INTERVAL '38 days'),
       (15, 15, NOW() - INTERVAL '88 days'),
       (4, 16, NOW() - INTERVAL '345 days'),
       (11, 16, NOW() - INTERVAL '340 days')
ON CONFLICT (user_id, publication_id) DO NOTHING;
