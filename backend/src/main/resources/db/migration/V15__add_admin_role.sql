-- =====================================================
-- AgenDoc
-- Release 1.1 - Administration
-- TS-07 - ADMIN role
-- =====================================================

-- -----------------------------------------------------
-- ADMIN role
-- -----------------------------------------------------

INSERT INTO roles (
    code,
    name,
    description,
    record_status,
    created_by
)
VALUES (
    'ADMIN',
    'Administrador',
    'Usuario con acceso administrativo general dentro de su consultorio.',
    'ACTIVE',
    'FLYWAY'
);
