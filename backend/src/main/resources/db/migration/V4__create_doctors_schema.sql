-- =====================================================
-- AgenDoc
-- Doctors schema
-- HU-04 - Register doctor
-- =====================================================

-- -----------------------------------------------------
-- Medical specialties catalog
-- -----------------------------------------------------

CREATE TABLE medical_specialties (
    id BIGSERIAL PRIMARY KEY,

    code VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(250),

    record_status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP WITH TIME ZONE,
    updated_by VARCHAR(100),

    CONSTRAINT uk_medical_specialties_code
        UNIQUE (code),

    CONSTRAINT uk_medical_specialties_name
        UNIQUE (name),

    CONSTRAINT chk_medical_specialties_record_status
        CHECK (record_status IN ('ACTIVE', 'INACTIVE'))
);

-- -----------------------------------------------------
-- Doctors
-- -----------------------------------------------------

CREATE TABLE doctors (
    id BIGSERIAL PRIMARY KEY,

    clinic_id BIGINT NOT NULL,
    user_id BIGINT,
    specialty_id BIGINT NOT NULL,

    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,

    document_type VARCHAR(30) NOT NULL,
    document_number VARCHAR(50) NOT NULL,

    medical_license_number VARCHAR(50) NOT NULL,

    phone VARCHAR(30),
    email VARCHAR(150) NOT NULL,

    record_status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP WITH TIME ZONE,
    updated_by VARCHAR(100),

    CONSTRAINT fk_doctors_clinic
        FOREIGN KEY (clinic_id)
        REFERENCES clinics (id),

    CONSTRAINT fk_doctors_user
        FOREIGN KEY (user_id)
        REFERENCES users (id),

    CONSTRAINT fk_doctors_specialty
        FOREIGN KEY (specialty_id)
        REFERENCES medical_specialties (id),

    CONSTRAINT uk_doctors_user
        UNIQUE (user_id),

    CONSTRAINT uk_doctors_document_number
        UNIQUE (document_number),

    CONSTRAINT uk_doctors_medical_license_number
        UNIQUE (medical_license_number),

    CONSTRAINT uk_doctors_email
        UNIQUE (email),

    CONSTRAINT chk_doctors_record_status
        CHECK (record_status IN ('ACTIVE', 'INACTIVE'))
);

-- -----------------------------------------------------
-- Medical specialties indexes
-- -----------------------------------------------------

CREATE INDEX idx_medical_specialties_record_status
    ON medical_specialties (record_status);

-- -----------------------------------------------------
-- Doctors indexes
-- -----------------------------------------------------

CREATE INDEX idx_doctors_clinic_id
    ON doctors (clinic_id);

CREATE INDEX idx_doctors_specialty_id
    ON doctors (specialty_id);

CREATE INDEX idx_doctors_record_status
    ON doctors (record_status);

CREATE INDEX idx_doctors_last_name
    ON doctors (last_name);