ALTER TABLE appointments
    ADD COLUMN cancellation_reason VARCHAR(500) NULL,
    ADD COLUMN cancelled_at TIMESTAMP NULL;
