-- =====================================================
-- AgenDoc
-- Medical specialties seed data
-- HU-04 - Register doctor
-- =====================================================

INSERT INTO medical_specialties (
    code,
    name,
    description,
    record_status,
    created_by
)
VALUES
    (
        'GENERAL_MEDICINE',
        'Medicina General',
        'Atención médica integral y de primera consulta.',
        'ACTIVE',
        'FLYWAY'
    ),
    (
        'PEDIATRICS',
        'Pediatría',
        'Atención médica de niños y adolescentes.',
        'ACTIVE',
        'FLYWAY'
    ),
    (
        'CARDIOLOGY',
        'Cardiología',
        'Atención especializada del corazón y sistema cardiovascular.',
        'ACTIVE',
        'FLYWAY'
    ),
    (
        'DERMATOLOGY',
        'Dermatología',
        'Atención especializada de la piel, cabello y uñas.',
        'ACTIVE',
        'FLYWAY'
    ),
    (
        'GYNECOLOGY',
        'Ginecología',
        'Atención especializada de la salud femenina.',
        'ACTIVE',
        'FLYWAY'
    ),
    (
        'TRAUMATOLOGY',
        'Traumatología',
        'Atención especializada del sistema musculoesquelético.',
        'ACTIVE',
        'FLYWAY'
    );