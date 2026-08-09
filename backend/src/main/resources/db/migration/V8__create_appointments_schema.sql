-- =====================================================
-- AgenDoc
-- Appointments schema
-- HU-10 - Create medical appointment
-- =====================================================

-- -----------------------------------------------------
-- Appointment statuses catalog
-- -----------------------------------------------------

CREATE TABLE appointment_statuses (
    id BIGSERIAL PRIMARY KEY,

    code VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(250),

    record_status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP WITH TIME ZONE,
    updated_by VARCHAR(100),

    CONSTRAINT uk_appointment_statuses_code
        UNIQUE (code),

    CONSTRAINT uk_appointment_statuses_name
        UNIQUE (name),

    CONSTRAINT chk_appointment_statuses_record_status
        CHECK (record_status IN ('ACTIVE', 'INACTIVE'))
);

-- -----------------------------------------------------
-- Appointments
-- -----------------------------------------------------

CREATE TABLE appointments (
    id BIGSERIAL PRIMARY KEY,

    clinic_id BIGINT NOT NULL,
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    agenda_block_id BIGINT NOT NULL,
    status_id BIGINT NOT NULL,

    reason VARCHAR(500),
    notes VARCHAR(1000),

    record_status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP WITH TIME ZONE,
    updated_by VARCHAR(100),

    CONSTRAINT fk_appointments_clinic
        FOREIGN KEY (clinic_id)
        REFERENCES clinics (id),

    CONSTRAINT fk_appointments_patient
        FOREIGN KEY (patient_id)
        REFERENCES patients (id),

    CONSTRAINT fk_appointments_doctor
        FOREIGN KEY (doctor_id)
        REFERENCES doctors (id),

    CONSTRAINT fk_appointments_agenda_block
        FOREIGN KEY (agenda_block_id)
        REFERENCES agenda_blocks (id),

    CONSTRAINT fk_appointments_status
        FOREIGN KEY (status_id)
        REFERENCES appointment_statuses (id),

    CONSTRAINT chk_appointments_record_status
        CHECK (record_status IN ('ACTIVE', 'INACTIVE'))
);

-- -----------------------------------------------------
-- Appointment statuses seed data
-- -----------------------------------------------------

INSERT INTO appointment_statuses (
    code,
    name,
    description,
    record_status,
    created_by
)
VALUES
    (
        'PROGRAMADA',
        'Programada',
        'La cita médica ha sido creada y se encuentra pendiente de confirmación.',
        'ACTIVE',
        'SYSTEM'
    ),
    (
        'CONFIRMADA',
        'Confirmada',
        'La asistencia del paciente a la cita médica ha sido confirmada.',
        'ACTIVE',
        'SYSTEM'
    ),
    (
        'ATENDIDA',
        'Atendida',
        'La atención médica correspondiente a la cita ha sido completada.',
        'ACTIVE',
        'SYSTEM'
    ),
    (
        'CANCELADA',
        'Cancelada',
        'La cita médica fue cancelada antes de la atención.',
        'ACTIVE',
        'SYSTEM'
    ),
    (
        'NO_ASISTIO',
        'No asistió',
        'El paciente no asistió a la cita médica programada.',
        'ACTIVE',
        'SYSTEM'
    );

-- -----------------------------------------------------
-- Appointment statuses indexes
-- -----------------------------------------------------

CREATE INDEX idx_appointment_statuses_record_status
    ON appointment_statuses (record_status);

-- -----------------------------------------------------
-- Appointments indexes
-- -----------------------------------------------------

CREATE INDEX idx_appointments_clinic_id
    ON appointments (clinic_id);

CREATE INDEX idx_appointments_patient_id
    ON appointments (patient_id);

CREATE INDEX idx_appointments_doctor_id
    ON appointments (doctor_id);

CREATE INDEX idx_appointments_agenda_block_id
    ON appointments (agenda_block_id);

CREATE INDEX idx_appointments_status_id
    ON appointments (status_id);

CREATE INDEX idx_appointments_record_status
    ON appointments (record_status);

CREATE INDEX idx_appointments_clinic_status
    ON appointments (
        clinic_id,
        status_id
    );

CREATE INDEX idx_appointments_patient_status
    ON appointments (
        patient_id,
        status_id
    );

CREATE INDEX idx_appointments_doctor_status
    ON appointments (
        doctor_id,
        status_id
    );