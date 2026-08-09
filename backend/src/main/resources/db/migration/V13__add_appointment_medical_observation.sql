ALTER TABLE appointments
    ADD COLUMN medical_observation VARCHAR(2000),
    ADD COLUMN medical_observation_recorded_at TIMESTAMP WITH TIME ZONE,
    ADD COLUMN medical_observation_recorded_by VARCHAR(100);

COMMENT ON COLUMN appointments.medical_observation IS
    'Basic medical observation recorded by the assigned doctor.';

COMMENT ON COLUMN appointments.medical_observation_recorded_at IS
    'Date and time when the medical observation was last recorded.';

COMMENT ON COLUMN appointments.medical_observation_recorded_by IS
    'User responsible for recording the medical observation.';