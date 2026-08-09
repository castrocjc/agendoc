-- =====================================================
-- AgenDoc
-- Medical agenda schema
-- HU-07 - Create medical agenda blocks
-- =====================================================

-- -----------------------------------------------------
-- Medical agendas
-- -----------------------------------------------------

CREATE TABLE medical_agendas (
    id BIGSERIAL PRIMARY KEY,

    clinic_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,

    name VARCHAR(150) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,

    record_status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP WITH TIME ZONE,
    updated_by VARCHAR(100),

    CONSTRAINT fk_medical_agendas_clinic
        FOREIGN KEY (clinic_id)
        REFERENCES clinics (id),

    CONSTRAINT fk_medical_agendas_doctor
        FOREIGN KEY (doctor_id)
        REFERENCES doctors (id),

    CONSTRAINT uk_medical_agendas_doctor
        UNIQUE (doctor_id),

    CONSTRAINT chk_medical_agendas_record_status
        CHECK (record_status IN ('ACTIVE', 'INACTIVE'))
);

-- -----------------------------------------------------
-- Agenda blocks
-- -----------------------------------------------------

CREATE TABLE agenda_blocks (
    id BIGSERIAL PRIMARY KEY,

    medical_agenda_id BIGINT NOT NULL,

    appointment_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,

    available BOOLEAN NOT NULL DEFAULT TRUE,

    record_status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP WITH TIME ZONE,
    updated_by VARCHAR(100),

    CONSTRAINT fk_agenda_blocks_medical_agenda
        FOREIGN KEY (medical_agenda_id)
        REFERENCES medical_agendas (id),

    CONSTRAINT uk_agenda_blocks_schedule
        UNIQUE (
            medical_agenda_id,
            appointment_date,
            start_time,
            end_time
        ),

    CONSTRAINT chk_agenda_blocks_time_range
        CHECK (start_time < end_time),

    CONSTRAINT chk_agenda_blocks_record_status
        CHECK (record_status IN ('ACTIVE', 'INACTIVE'))
);

-- -----------------------------------------------------
-- Medical agendas indexes
-- -----------------------------------------------------

CREATE INDEX idx_medical_agendas_clinic_id
    ON medical_agendas (clinic_id);

CREATE INDEX idx_medical_agendas_record_status
    ON medical_agendas (record_status);

-- -----------------------------------------------------
-- Agenda blocks indexes
-- -----------------------------------------------------

CREATE INDEX idx_agenda_blocks_medical_agenda_id
    ON agenda_blocks (medical_agenda_id);

CREATE INDEX idx_agenda_blocks_appointment_date
    ON agenda_blocks (appointment_date);

CREATE INDEX idx_agenda_blocks_available
    ON agenda_blocks (available);

CREATE INDEX idx_agenda_blocks_record_status
    ON agenda_blocks (record_status);

CREATE INDEX idx_agenda_blocks_agenda_date_available
    ON agenda_blocks (
        medical_agenda_id,
        appointment_date,
        available
    );