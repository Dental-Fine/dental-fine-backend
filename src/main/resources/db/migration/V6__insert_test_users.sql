-- Corregir el rol del admin insertado en V4 (admin@dentalfine.com)
UPDATE usuarios SET rol = 'ROLE_ADMIN' WHERE rol = 'ROLE_PERSONAL_CLINICA' OR login = 'admin@dentalfine.com';

-- Crear perfil de personal_clinica para el admin (si no existe)
INSERT INTO personal_clinica (nombre, telefono, correo, usuario_id)
SELECT 'Admin Chuy', '0000000000', 'admin@dentalfine.com', id 
FROM usuarios 
WHERE login = 'admin@dentalfine.com'
  AND NOT EXISTS (SELECT 1 FROM personal_clinica WHERE correo = 'admin@dentalfine.com');

-- Insertar un paciente de prueba (Richy) con contraseña 'richy123'
INSERT INTO usuarios (login, clave, rol)
SELECT 'richy@dentalfine.com', '$2a$10$5ccijiaa9g.VglFjwKH9auASecacmC7iSS2ZLgJuJ1MnMHX0FWZa.', 'ROLE_PACIENTE'
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE login = 'richy@dentalfine.com');

-- Crear el perfil de paciente para Richy
INSERT INTO paciente (nombre, apellidos, telefono, correo, usuario_id)
SELECT 'Richy', 'Paciente', '1234567890', 'richy@dentalfine.com', id
FROM usuarios
WHERE login = 'richy@dentalfine.com'
  AND NOT EXISTS (SELECT 1 FROM paciente WHERE correo = 'richy@dentalfine.com');

-- Insertar usuario de prueba para la Dentista Anel (misma clave que el admin: admin123456)
INSERT INTO usuarios (login, clave, rol)
SELECT 'anel@dentalfine.com', '$2a$10$gyhq1KqWf96HNTgjaoPH9OjxD7DfZczuvgevEJ10EC/KbChJnqrNW', 'ROLE_DENTISTA'
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE login = 'anel@dentalfine.com');

-- Crear el perfil de dentista para Anel
INSERT INTO dentista (nombre, usuario_id)
SELECT 'Dra. Anel', id
FROM usuarios
WHERE login = 'anel@dentalfine.com'
  AND NOT EXISTS (SELECT 1 FROM dentista WHERE usuario_id = (SELECT id FROM usuarios WHERE login = 'anel@dentalfine.com'));
