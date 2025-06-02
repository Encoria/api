-- V5__Seed_users_and_artists.sql

-- Users
INSERT INTO users (uuid, external_auth_id, username, email, firstname, lastname, birthdate, picture_url, created_at,
                   country_id, role_id, status_id)
VALUES
    (gen_random_uuid(), 'auth_user_esp_001', 'pablo_ruiz', 'pablo.ruiz@example.com', 'Pablo', 'Ruiz', '1992-05-20',
     'https://images.pexels.com/photos/614810/pexels-photo-614810.jpeg?cs=srgb&dl=pexels-simon-robben-55958-614810.jpg&fm=jpg', NOW() - INTERVAL '100 days', 1, 1, 2),
    (gen_random_uuid(), 'auth_user_esp_003', 'carlos_mrtnz', 'carlos.martinez@example.com', 'Carlos', 'Martínez', '1996-03-01',
     'https://images.pexels.com/photos/91227/pexels-photo-91227.jpeg?cs=srgb&dl=pexels-stefanstefancik-91227.jpg&fm=jpg', NOW() - INTERVAL '90 days', 1, 1, 2),
    (gen_random_uuid(), 'auth_user_esp_005', 'david_sanchez', 'david.sanchez@example.com', 'David', 'Sánchez', '1978-06-14',
     'https://plus.unsplash.com/premium_photo-1664536392896-cd1743f9c02c?fm=jpg&q=60&w=3000&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OXx8cGVyc29ufGVufDB8fDB8fHww', NOW() - INTERVAL '80 days', 1, 1, 2),
    (gen_random_uuid(), 'auth_user_esp_007', 'javier_perez', 'javier.perez@example.com', 'Javier', 'Pérez', '1965-04-19',
     'https://images.pexels.com/photos/614810/pexels-photo-614810.jpeg?cs=srgb&dl=pexels-simon-robben-55958-614810.jpg&fm=jpg', NOW() - INTERVAL '70 days', 1, 1, 2),
    (gen_random_uuid(), 'auth_user_esp_009', 'antonio_gomez', 'antonio.gomez@example.com', 'Antonio', 'Gómez', '1985-12-30',
     'https://images.pexels.com/photos/91227/pexels-photo-91227.jpeg?cs=srgb&dl=pexels-stefanstefancik-91227.jpg&fm=jpg', NOW() - INTERVAL '60 days', 1, 1, 2),
    (gen_random_uuid(), 'auth_user_esp_011', 'miguel_moreno', 'miguel.moreno@example.com', 'Miguel', 'Moreno', '1994-07-22',
     'https://plus.unsplash.com/premium_photo-1664536392896-cd1743f9c02c?fm=jpg&q=60&w=3000&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OXx8cGVyc29ufGVufDB8fDB8fHww', NOW() - INTERVAL '50 days', 1, 1, 2),
    (gen_random_uuid(), 'auth_user_esp_013', 'ricardo_alvarez', 'ricardo.alvarez@example.com', 'Ricardo', 'Álvarez', '1980-01-28',
     'https://images.pexels.com/photos/614810/pexels-photo-614810.jpeg?cs=srgb&dl=pexels-simon-robben-55958-614810.jpg&fm=jpg', NOW() - INTERVAL '40 days', 1, 1, 2),
    (gen_random_uuid(), 'auth_user_esp_015', 'sergio_navarro', 'sergio.navarro@example.com', 'Sergio', 'Navarro', '1998-08-16',
     'https://images.pexels.com/photos/91227/pexels-photo-91227.jpeg?cs=srgb&dl=pexels-stefanstefancik-91227.jpg&fm=jpg', NOW() - INTERVAL '30 days', 1, 1, 2),
    (gen_random_uuid(), 'auth_user_esp_002', 'sofia_g88', 'sofia.garcia@example.com', 'Sofía', 'García', '1990-08-11',
     'https://img.freepik.com/free-photo/lifestyle-people-emotions-casual-concept-confident-nice-smiling-asian-woman-cross-arms-chest-confident-ready-help-listening-coworkers-taking-part-conversation_1258-59335.jpg?semt=ais_hybrid&w=740', NOW() - INTERVAL '95 days', 1, 1, 2),
    (gen_random_uuid(), 'auth_user_esp_004', 'laura_lpz', 'laura.lopez@example.com', 'Laura', 'López', '1993-11-25',
     'https://images.pexels.com/photos/733872/pexels-photo-733872.jpeg?cs=srgb&dl=pexels-olly-733872.jpg&fm=jpg', NOW() - INTERVAL '85 days', 1, 1, 2),
    (gen_random_uuid(), 'auth_user_esp_006', 'elena_fdez', 'elena.fernandez@example.com', 'Elena', 'Fernández', '1999-01-03',
     'https://www.stockvault.net/data/2007/03/01/102431/thumb16.jpg', NOW() - INTERVAL '75 days', 1, 1, 2),
    (gen_random_uuid(), 'auth_user_esp_008', 'maria_rgz', 'maria.rodriguez@example.com', 'María', 'Rodríguez', '1991-09-07',
     'https://hips.hearstapps.com/hmg-prod/images/beauty-hair-care-and-face-of-african-happy-woman-in-royalty-free-image-1747524289.pjpeg?crop=0.529xw:1.00xh;0.219xw,0&resize=1120:*', NOW() - INTERVAL '65 days', 1, 1, 2),
    (gen_random_uuid(), 'auth_user_esp_010', 'ana_diaz', 'ana.diaz@example.com', 'Ana', 'Díaz', '1989-02-18',
     'https://img.freepik.com/free-photo/lifestyle-people-emotions-casual-concept-confident-nice-smiling-asian-woman-cross-arms-chest-confident-ready-help-listening-coworkers-taking-part-conversation_1258-59335.jpg?semt=ais_hybrid&w=740', NOW() - INTERVAL '55 days', 1, 1, 2), -- Repite URL mujer 1
    (gen_random_uuid(), 'auth_user_esp_012', 'isabella_ruiz', 'isabella.ruiz@example.com', 'Isabella', 'Ruiz', '1997-10-05',
     'https://images.pexels.com/photos/733872/pexels-photo-733872.jpeg?cs=srgb&dl=pexels-olly-733872.jpg&fm=jpg', NOW() - INTERVAL '45 days', 1, 1, 2),
    (gen_random_uuid(), 'auth_user_esp_014', 'carmen_jmnz', 'carmen.jimenez@example.com', 'Carmen', 'Jiménez', '1995-04-04',
     'https://www.stockvault.net/data/2007/03/01/102431/thumb16.jpg', NOW() - INTERVAL '35 days', 1, 1, 2)
ON CONFLICT (external_auth_id) DO NOTHING;

-- Artists
INSERT INTO artists (spotify_id, artist_name, picture_url)
VALUES
    ('6J6yx1t3nwIDyPXk5xa7O8', 'Vetusta Morla', 'https://e00-elmundo.uecdn.es/assets/multimedia/imagenes/2024/04/25/17140570022020.jpg'),
    ('7ltDVBr6mKbRvohxheJ9h1', 'Rosalía', 'https://elmon.cat/app/uploads/sites/15/2025/04/surten-a-la-llum-les-primeres-imatges-de-rosalia-en-el-rodatge-de-la-serie-euphoria-_-europa-press.jpg'),
    ('2hazSY4Ef3aB9ATXW7F5w3', 'Izal', 'https://imagenes.elpais.com/resizer/v2/https%3A%2F%2Fvdmedia.elpais.com%2Felpaistop%2F20186%2F14%2F2018061418232974_1528992367_asset_still.png?auth=e5fd0ee5629fd9e8e05c916db79d01640f73f1e6e26ea945ef56c277d44ae4a8&width=1200'),
    ('5TYxZTjIPqKM8K8NuP9woO', 'C. Tangana', 'https://media.revistavanityfair.es/photos/60e853a4bb9e71d46fd8d8ba/master/w_3940%2Cc_limit/50464.jpg'),
    ('5ZqnEfVdEGmoPxtELhN7ai', 'Estopa', 'https://img2.rtve.es/i/?w=1600&i=1704812774401.jpg'),
    ('1EXjXQpDx2pROygh8zvHs4', 'Melendi', 'https://cdn-p.smehost.net/sites/5b3bac59eb36401694af3a241173447f/wp-content/uploads/2018/08/foto_de_melendi-2.jpg'),
    ('5sUrlPAHlS9NEirDB8SEbF', 'Alejandro Sanz', 'https://upload.wikimedia.org/wikipedia/commons/thumb/2/21/Goyas_2025_-_Alejandro_Sanz_(cropped).jpg/960px-Goyas_2025_-_Alejandro_Sanz_(cropped).jpg'),
    ('4U7lXyKdSf1JbM1aXvsodC', 'La Oreja de Van Gogh', 'https://yt3.googleusercontent.com/ytc/AIdro_mbGmTXdEsIAv7Yf8QfnpHA1Sa_3z1RslnTSaa8TQ5My7g=s900-c-k-c0x00ffffff-no-rj'),
    ('3mOsjj1MhocRVwOejIZlTi', 'Lori Meyers', 'https://i.scdn.co/image/ab6761610000e5eb1e67179609693ea704b6acd1'),
    ('6VCoG3MG7ZKRxDjaYOvtrF', 'Love of Lesbian', 'https://estaticos-cdn.prensaiberica.es/clip/87eee705-2362-40ac-9e58-c5c7b3f99d63_alta-libre-aspect-ratio_default_0.jpg'),
    ('7GmXwGXJSsmWTkCyk5Twux', 'Kase.O', 'https://static.wikia.nocookie.net/rap/images/3/3d/KaseO.jpg/revision/latest?cb=20180209002754&path-prefix=es'),
    ('7qJXYbBDibZ1Zixi89aUnw', 'Sílvia Pérez Cruz', 'https://img2.rtve.es/i/?w=1600&i=1665399320253.jpg')
ON CONFLICT (spotify_id) DO NOTHING;
