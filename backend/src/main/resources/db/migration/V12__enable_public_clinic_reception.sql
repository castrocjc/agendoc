-- =====================================================
-- AgenDoc
-- Enable public clinic reception
-- HU-20 - Explore clinic and book first appointment
-- =====================================================

-- =====================================================
-- 1. Clinics
-- Public identity and Digital Reception
-- =====================================================

ALTER TABLE clinics
    ADD COLUMN slug VARCHAR(100),
    ADD COLUMN public_name VARCHAR(150),
    ADD COLUMN public_description VARCHAR(500),
    ADD COLUMN whatsapp VARCHAR(30),
    ADD COLUMN map_url VARCHAR(500),
    ADD COLUMN logo_url VARCHAR(500),
    ADD COLUMN public_portal_enabled BOOLEAN NOT NULL DEFAULT FALSE;

-- -----------------------------------------------------
-- Initialize the public identity of existing clinics
-- -----------------------------------------------------

UPDATE clinics
SET
    public_name = name,
    updated_at = CURRENT_TIMESTAMP,
    updated_by = 'FLYWAY'
WHERE public_name IS NULL;

-- Configure the development clinic used by the MVP.
UPDATE clinics
SET
    slug = 'agendoc-development-clinic',
    public_name = 'AgenDoc Development Clinic',
    public_portal_enabled = TRUE,
    updated_at = CURRENT_TIMESTAMP,
    updated_by = 'FLYWAY'
WHERE name = 'AgenDoc Development Clinic'
  AND email = 'contacto@agendoc.local';

-- Assign a safe internal public identifier to any other
-- pre-existing clinic. Its Digital Reception remains disabled.
UPDATE clinics
SET
    slug = 'clinic-' || id,
    updated_at = CURRENT_TIMESTAMP,
    updated_by = 'FLYWAY'
WHERE slug IS NULL;

ALTER TABLE clinics
    ALTER COLUMN slug SET NOT NULL;

ALTER TABLE clinics
    ADD CONSTRAINT uk_clinics_slug
        UNIQUE (slug);

ALTER TABLE clinics
    ADD CONSTRAINT chk_clinics_slug_format
        CHECK (
            slug ~ '^[a-z0-9]+(-[a-z0-9]+)*$'
        );

CREATE INDEX idx_clinics_public_portal_enabled
    ON clinics (public_portal_enabled);

COMMENT ON COLUMN clinics.slug IS
    'Unique public identifier used to resolve the clinic from the Digital Reception URL.';

COMMENT ON COLUMN clinics.public_name IS
    'Clinic name displayed publicly in the Digital Reception.';

COMMENT ON COLUMN clinics.public_description IS
    'Optional public description displayed in the Digital Reception.';

COMMENT ON COLUMN clinics.whatsapp IS
    'Optional public WhatsApp contact number.';

COMMENT ON COLUMN clinics.map_url IS
    'Optional public URL used to display or open the clinic location.';

COMMENT ON COLUMN clinics.logo_url IS
    'Optional public URL of the clinic logo.';

COMMENT ON COLUMN clinics.public_portal_enabled IS
    'Indicates whether the clinic Digital Reception is publicly available.';

-- =====================================================
-- 2. Users
-- Contextual email uniqueness and technical username
-- =====================================================

-- -----------------------------------------------------
-- Validate existing data before changing uniqueness
-- -----------------------------------------------------

DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM users
        GROUP BY
            clinic_id,
            LOWER(BTRIM(email))
        HAVING COUNT(*) > 1
    ) THEN
        RAISE EXCEPTION
            'Cannot enable contextual user email uniqueness because duplicated normalized emails exist within a clinic.';
    END IF;
END
$$;

ALTER TABLE users
    DROP CONSTRAINT IF EXISTS uk_users_email;

ALTER TABLE users
    ALTER COLUMN username TYPE VARCHAR(255);

-- Existing emails are normalized to the same rule
-- that will be applied by the Backend.
UPDATE users
SET
    email = LOWER(BTRIM(email)),
    updated_at = CURRENT_TIMESTAMP,
    updated_by = 'FLYWAY'
WHERE email <> LOWER(BTRIM(email));

CREATE UNIQUE INDEX uk_users_clinic_email_normalized
    ON users (
        clinic_id,
        LOWER(BTRIM(email))
    );

COMMENT ON INDEX uk_users_clinic_email_normalized IS
    'Ensures that a normalized email identifies only one user within the same clinic.';

-- =====================================================
-- 3. Patients
-- Progressive patient profile
-- =====================================================

-- -----------------------------------------------------
-- Validate existing document data before normalization
-- -----------------------------------------------------

DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM patients
        WHERE document_type IS NOT NULL
          AND document_number IS NOT NULL
        GROUP BY
            clinic_id,
            UPPER(BTRIM(document_type)),
            UPPER(BTRIM(document_number))
        HAVING COUNT(*) > 1
    ) THEN
        RAISE EXCEPTION
            'Cannot enable contextual patient document uniqueness because duplicated normalized documents exist within a clinic.';
    END IF;
END
$$;

-- -----------------------------------------------------
-- Validate existing patient emails before normalization
-- -----------------------------------------------------

DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM patients
        WHERE email IS NOT NULL
          AND BTRIM(email) <> ''
        GROUP BY
            clinic_id,
            LOWER(BTRIM(email))
        HAVING COUNT(*) > 1
    ) THEN
        RAISE EXCEPTION
            'Cannot enable contextual patient email uniqueness because duplicated normalized emails exist within a clinic.';
    END IF;
END
$$;

ALTER TABLE patients
    DROP CONSTRAINT IF EXISTS uk_patients_document_number;

ALTER TABLE patients
    DROP CONSTRAINT IF EXISTS uk_patients_email;

ALTER TABLE patients
    ALTER COLUMN document_type DROP NOT NULL,
    ALTER COLUMN document_number DROP NOT NULL,
    ALTER COLUMN birth_date DROP NOT NULL;

-- Normalize existing optional values.
-- Empty values become NULL to represent missing profile data.
UPDATE patients
SET
    document_type = NULLIF(
        UPPER(BTRIM(document_type)),
        ''
    ),
    document_number = NULLIF(
        UPPER(BTRIM(document_number)),
        ''
    ),
    email = NULLIF(
        LOWER(BTRIM(email)),
        ''
    ),
    updated_at = CURRENT_TIMESTAMP,
    updated_by = 'FLYWAY';

ALTER TABLE patients
    ADD CONSTRAINT chk_patients_document_complete
        CHECK (
            (
                document_type IS NULL
                AND document_number IS NULL
            )
            OR
            (
                document_type IS NOT NULL
                AND document_number IS NOT NULL
                AND BTRIM(document_type) <> ''
                AND BTRIM(document_number) <> ''
            )
        );

CREATE UNIQUE INDEX uk_patients_clinic_document_normalized
    ON patients (
        clinic_id,
        UPPER(BTRIM(document_type)),
        UPPER(BTRIM(document_number))
    )
    WHERE document_type IS NOT NULL
      AND document_number IS NOT NULL;

CREATE UNIQUE INDEX uk_patients_clinic_email_normalized
    ON patients (
        clinic_id,
        LOWER(BTRIM(email))
    )
    WHERE email IS NOT NULL;

COMMENT ON CONSTRAINT chk_patients_document_complete
    ON patients IS
    'Requires document type and document number to be provided together. Both may remain absent during progressive registration.';

COMMENT ON INDEX uk_patients_clinic_document_normalized IS
    'Ensures document uniqueness within the same clinic when a document has been provided.';

COMMENT ON INDEX uk_patients_clinic_email_normalized IS
    'Ensures patient email uniqueness within the same clinic when an email has been provided.';