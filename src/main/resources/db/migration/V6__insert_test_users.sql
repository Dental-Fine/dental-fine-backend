-- Corregir el rol del admin insertado en V4 (admin@dentalfine.com)
UPDATE usuarios SET rol = 'ROLE_ADMIN' WHERE rol = 'ROLE_PERSONAL_CLINICA' OR login = 'admin@dentalfine.com';

-- Crear perfil de personal_clinica para el admin (si no existe)
INSERT INTO personal_clinica (nombre, telefono, correo, usuario_id)
SELECT 'Admin Chuy', '0000000000', 'admin@dentalfine.com', id 
FROM usuarios 
WHERE login = 'admin@dentalfine.com'
  AND NOT EXISTS (SELECT 1 FROM personal_clinica WHERE correo = 'admin@dentalfine.com');

-- Insertar un paciente de prueba (Richy) con contraseña '123456'
INSERT INTO usuarios (login, clave, rol)
SELECT 'richy@dentalfine.com', '$2a$10$XURPShQNCsLjp1ESc2laoObo9QZDhxz73hJPaEv7/cBha4pk0AgP.', 'ROLE_PACIENTE'
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE login = 'richy@dentalfine.com');

-- Crear el perfil de paciente para Richy
INSERT INTO paciente (nombre, apellidos, telefono, correo, usuario_id)
SELECT 'Richy', 'Paciente', '1234567890', 'richy@dentalfine.com', id
FROM usuarios
WHERE login = 'richy@dentalfine.com'
  AND NOT EXISTS (SELECT 1 FROM paciente WHERE correo = 'richy@dentalfine.com');
