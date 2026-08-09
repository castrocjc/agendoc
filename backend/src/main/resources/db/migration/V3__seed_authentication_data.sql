-- =====================================================
-- AgenDoc
-- Authentication seed data
-- HU-01 - User login
-- =====================================================

-- -----------------------------------------------------
-- Development clinic
-- -----------------------------------------------------

INSERT INTO clinics (
    name,
    phone,
    email,
    address,
    record_status,
    created_by
)
VALUES (
    'AgenDoc Development Clinic',
    '+52 55 0000 0000',
    'contacto@agendoc.local',
    'Ciudad de México',
    'ACTIVE',
    'FLYWAY'
);

-- -----------------------------------------------------
-- MVP roles
-- -----------------------------------------------------

INSERT INTO roles (
    code,
    name,
    description,
    record_status,
    created_by
)
VALUES
    (
        'PATIENT',
        'Paciente',
        'Usuario que consulta disponibilidad y gestiona sus propias citas.',
        'ACTIVE',
        'FLYWAY'
    ),
    (
        'RECEPTIONIST',
        'Recepcionista',
        'Usuario que gestiona pacientes, agendas y citas del consultorio.',
        'ACTIVE',
        'FLYWAY'
    ),
    (
        'DOCTOR',
        'Médico',
        'Usuario que consulta su agenda y registra la atención médica básica.',
        'ACTIVE',
        'FLYWAY'
    );

-- -----------------------------------------------------
-- Initial development user
-- -----------------------------------------------------

INSERT INTO users (
    clinic_id,
    role_id,
    username,
    email,
    password_hash,
    is_active,
    record_status,
    created_by
)
SELECT
    clinic.id,
    role.id,
    'admin.reception',
    'admin.reception@agendoc.local',
    '$2a$10$J4MGzXlL95BSBX/fM7mGJOKEir7RfUU0HnkuowEwKhiCTTMW2.A/e',
    TRUE,
    'ACTIVE',
    'FLYWAY'
FROM clinics clinic
INNER JOIN roles role
    ON role.code = 'RECEPTIONIST'
WHERE clinic.email = 'contacto@agendoc.local';