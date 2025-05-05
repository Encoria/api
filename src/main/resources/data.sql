INSERT INTO user_roles (code, name) SELECT 'ADMIN', 'Administrator' WHERE NOT EXISTS (SELECT 1 FROM user_roles WHERE code = 'ADMIN');
INSERT INTO user_roles (code, name) SELECT 'USER', 'Normal user' WHERE NOT EXISTS (SELECT 1 FROM user_roles WHERE code = 'USER');
