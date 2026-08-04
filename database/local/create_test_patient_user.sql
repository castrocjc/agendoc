USE agendoc;

-- =====================================================
-- AgenDoc
-- Local development data
-- Patient user for HU-09 testing
-- =====================================================
--
-- This script is not managed by Flyway.
-- It must be executed manually only in the local database.
--
-- Test user:
--   username: patient.maria
--   email: patient.maria@agendoc.local
--
-- The password hash is intentionally the same as the
-- admin.reception development user, so both accounts use
-- the same local development password.
-- =====================================================

BEGIN;

-- -----------------------------------------------------
-- Create the PATIENT user when it does not already exist
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
    'patient.maria',
    'patient.maria@agendoc.local',
    '$2a$10$J4MGzXlL95BSBX/fM7mGJOKEir7RfUU0HnkuowEwKhiCTTMW2.A/e',
    TRUE,
    'ACTIVE',
    'LOCAL_SCRIPT'
FROM clinics clinic
INNER JOIN roles role
    ON role.code = 'PATIENT'
WHERE clinic.email = 'contacto@agendoc.local'
  AND clinic.record_status = 'ACTIVE'
  AND role.record_status = 'ACTIVE'
  AND NOT EXISTS (
      SELECT 1
      FROM users existing_user
      WHERE existing_user.username = 'patient.maria'
         OR existing_user.email = 'patient.maria@agendoc.local'
  );

-- -----------------------------------------------------
-- Associate María González with the PATIENT user
-- -----------------------------------------------------

UPDATE patients patient
SET
    user_id = patient_user.id,
    updated_at = CURRENT_TIMESTAMP,
    updated_by = 'LOCAL_SCRIPT'
FROM users patient_user
WHERE patient.document_number = '87654321'
  AND patient.clinic_id = patient_user.clinic_id
  AND patient_user.username = 'patient.maria'
  AND patient.record_status = 'ACTIVE'
  AND patient.user_id IS NULL;

COMMIT;