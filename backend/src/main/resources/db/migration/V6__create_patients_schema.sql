-- =====================================================
-- AgenDoc
-- Patients schema
-- HU-05 - Register patient
-- =====================================================

-- -----------------------------------------------------
-- Patients
-- -----------------------------------------------------

CREATE TABLE patients (
    id BIGSERIAL PRIMARY KEY,

    clinic_id BIGINT NOT NULL,
    user_id BIGINT,

    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,

    document_type VARCHAR(30) NOT NULL,
    document_number VARCHAR(50) NOT NULL,

    birth_date DATE NOT NULL,

    phone VARCHAR(30) NOT NULL,
    email VARCHAR(150),
    address VARCHAR(250),

    record_status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP WITH TIME ZONE,
    updated_by VARCHAR(100),

    CONSTRAINT fk_patients_clinic
        FOREIGN KEY (clinic_id)
        REFERENCES clinics (id),

    CONSTRAINT fk_patients_user
        FOREIGN KEY (user_id)
        REFERENCES users (id),

    CONSTRAINT uk_patients_user
        UNIQUE (user_id),

    CONSTRAINT uk_patients_document_number
        UNIQUE (document_number),

    CONSTRAINT uk_patients_email
        UNIQUE (email),

    CONSTRAINT chk_patients_birth_date
        CHECK (birth_date <= CURRENT_DATE),

    CONSTRAINT chk_patients_record_status
        CHECK (record_status IN ('ACTIVE', 'INACTIVE'))
);

-- -----------------------------------------------------
-- Patients indexes
-- -----------------------------------------------------

CREATE INDEX idx_patients_clinic_id
    ON patients (clinic_id);

CREATE INDEX idx_patients_record_status
    ON patients (record_status);

CREATE INDEX idx_patients_last_name
    ON patients (last_name);