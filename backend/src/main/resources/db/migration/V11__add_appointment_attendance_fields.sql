ALTER TABLE appointments
    ADD COLUMN confirmed_at TIMESTAMP WITH TIME ZONE,
    ADD COLUMN confirmed_by VARCHAR(100),
    ADD COLUMN no_show_at TIMESTAMP WITH TIME ZONE,
    ADD COLUMN no_show_by VARCHAR(100),
    ADD COLUMN no_show_comment VARCHAR(500);

COMMENT ON COLUMN appointments.confirmed_at IS
    'Date and time when the patient arrival was confirmed.';

COMMENT ON COLUMN appointments.confirmed_by IS
    'User responsible for confirming the patient arrival.';

COMMENT ON COLUMN appointments.no_show_at IS
    'Date and time when the patient no-show was registered.';

COMMENT ON COLUMN appointments.no_show_by IS
    'User responsible for registering the patient no-show.';

COMMENT ON COLUMN appointments.no_show_comment IS
    'Optional comment recorded when the patient no-show is registered.';