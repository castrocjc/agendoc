-- =====================================================
-- AgenDoc
-- Authentication schema
-- HU-01 - User login
-- =====================================================

CREATE TABLE clinics (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    phone VARCHAR(30),
    email VARCHAR(150),
    address VARCHAR(250),

    record_status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP WITH TIME ZONE,
    updated_by VARCHAR(100),

    CONSTRAINT chk_clinics_record_status
        CHECK (record_status IN ('ACTIVE', 'INACTIVE'))
);

CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(250),

    record_status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP WITH TIME ZONE,
    updated_by VARCHAR(100),

    CONSTRAINT uk_roles_code UNIQUE (code),

    CONSTRAINT chk_roles_record_status
        CHECK (record_status IN ('ACTIVE', 'INACTIVE'))
);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    clinic_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,

    username VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    last_access_at TIMESTAMP WITH TIME ZONE,

    record_status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP WITH TIME ZONE,
    updated_by VARCHAR(100),

    CONSTRAINT fk_users_clinic
        FOREIGN KEY (clinic_id)
        REFERENCES clinics (id),

    CONSTRAINT fk_users_role
        FOREIGN KEY (role_id)
        REFERENCES roles (id),

    CONSTRAINT uk_users_username UNIQUE (username),
    CONSTRAINT uk_users_email UNIQUE (email),

    CONSTRAINT chk_users_record_status
        CHECK (record_status IN ('ACTIVE', 'INACTIVE'))
);

CREATE INDEX idx_users_clinic_id
    ON users (clinic_id);

CREATE INDEX idx_users_role_id
    ON users (role_id);

CREATE INDEX idx_users_is_active
    ON users (is_active);

CREATE INDEX idx_users_record_status
    ON users (record_status);