-- =====================================================
-- AgenDoc
-- Appointment reschedule history
-- HU-14 - Reschedule medical appointment
-- =====================================================

-- -----------------------------------------------------
-- Appointment reschedule history
-- -----------------------------------------------------

CREATE TABLE appointment_reschedule_history (
    id BIGSERIAL PRIMARY KEY,

    appointment_id BIGINT NOT NULL,
    previous_agenda_block_id BIGINT NOT NULL,
    new_agenda_block_id BIGINT NOT NULL,

    previous_appointment_date DATE NOT NULL,
    previous_start_time TIME NOT NULL,
    previous_end_time TIME NOT NULL,

    new_appointment_date DATE NOT NULL,
    new_start_time TIME NOT NULL,
    new_end_time TIME NOT NULL,

    record_status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP WITH TIME ZONE,
    updated_by VARCHAR(100),

    CONSTRAINT fk_appointment_reschedule_history_appointment
        FOREIGN KEY (appointment_id)
        REFERENCES appointments (id),

    CONSTRAINT fk_appointment_reschedule_history_previous_block
        FOREIGN KEY (previous_agenda_block_id)
        REFERENCES agenda_blocks (id),

    CONSTRAINT fk_appointment_reschedule_history_new_block
        FOREIGN KEY (new_agenda_block_id)
        REFERENCES agenda_blocks (id),

    CONSTRAINT chk_appointment_reschedule_history_different_blocks
        CHECK (previous_agenda_block_id <> new_agenda_block_id),

    CONSTRAINT chk_appointment_reschedule_history_previous_time
        CHECK (previous_start_time < previous_end_time),

    CONSTRAINT chk_appointment_reschedule_history_new_time
        CHECK (new_start_time < new_end_time),

    CONSTRAINT chk_appointment_reschedule_history_record_status
        CHECK (record_status IN ('ACTIVE', 'INACTIVE'))
);

-- -----------------------------------------------------
-- Appointment reschedule history indexes
-- -----------------------------------------------------

CREATE INDEX idx_appointment_reschedule_history_appointment_id
    ON appointment_reschedule_history (appointment_id);

CREATE INDEX idx_appointment_reschedule_history_previous_block_id
    ON appointment_reschedule_history (previous_agenda_block_id);

CREATE INDEX idx_appointment_reschedule_history_new_block_id
    ON appointment_reschedule_history (new_agenda_block_id);

CREATE INDEX idx_appointment_reschedule_history_created_at
    ON appointment_reschedule_history (created_at);

CREATE INDEX idx_appointment_reschedule_history_record_status
    ON appointment_reschedule_history (record_status);