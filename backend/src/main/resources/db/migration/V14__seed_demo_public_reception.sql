-- =====================================================
-- AgenDoc
-- Public demo environment seed data
-- Release 1.0 - Online portfolio demo
-- =====================================================
--
-- Provides fictitious doctors, medical agendas and
-- future availability for the public Digital Reception.
--
-- No authenticated doctor users are created by this
-- migration.
-- =====================================================

-- -----------------------------------------------------
-- 1. Demo doctors
-- -----------------------------------------------------

INSERT INTO doctors (
    clinic_id,
    specialty_id,
    first_name,
    last_name,
    document_type,
    document_number,
    medical_license_number,
    phone,
    email,
    record_status,
    created_by
)
SELECT
    clinic.id,
    specialty.id,
    demo.first_name,
    demo.last_name,
    'DEMO',
    demo.document_number,
    demo.license_number,
    demo.phone,
    demo.email,
    'ACTIVE',
    'FLYWAY_DEMO'
FROM clinics clinic
CROSS JOIN (
    VALUES
        (
            'GENERAL_MEDICINE',
            'Ana',
            'Torres',
            'DEMO-DOC-001',
            'DEMO-LIC-001',
            '+52 55 1000 0001',
            'ana.torres@demo.agendoc.local'
        ),
        (
            'CARDIOLOGY',
            'Carlos',
            'Mendoza',
            'DEMO-DOC-002',
            'DEMO-LIC-002',
            '+52 55 1000 0002',
            'carlos.mendoza@demo.agendoc.local'
        ),
        (
            'PEDIATRICS',
            'Lucía',
            'Ramírez',
            'DEMO-DOC-003',
            'DEMO-LIC-003',
            '+52 55 1000 0003',
            'lucia.ramirez@demo.agendoc.local'
        )
) AS demo(
    specialty_code,
    first_name,
    last_name,
    document_number,
    license_number,
    phone,
    email
)
INNER JOIN medical_specialties specialty
    ON specialty.code = demo.specialty_code
WHERE clinic.slug = 'agendoc-development-clinic'
  AND clinic.record_status = 'ACTIVE'
  AND specialty.record_status = 'ACTIVE'
  AND NOT EXISTS (
      SELECT 1
      FROM doctors existing_doctor
      WHERE existing_doctor.document_number = demo.document_number
         OR existing_doctor.medical_license_number = demo.license_number
         OR LOWER(existing_doctor.email) = LOWER(demo.email)
  );

-- -----------------------------------------------------
-- 2. Medical agendas
-- -----------------------------------------------------

INSERT INTO medical_agendas (
    clinic_id,
    doctor_id,
    name,
    active,
    record_status,
    created_by
)
SELECT
    doctor.clinic_id,
    doctor.id,
    'Agenda Demo - ' || doctor.first_name || ' ' || doctor.last_name,
    TRUE,
    'ACTIVE',
    'FLYWAY_DEMO'
FROM doctors doctor
INNER JOIN clinics clinic
    ON clinic.id = doctor.clinic_id
WHERE clinic.slug = 'agendoc-development-clinic'
  AND doctor.document_number IN (
      'DEMO-DOC-001',
      'DEMO-DOC-002',
      'DEMO-DOC-003'
  )
  AND doctor.record_status = 'ACTIVE'
  AND NOT EXISTS (
      SELECT 1
      FROM medical_agendas existing_agenda
      WHERE existing_agenda.doctor_id = doctor.id
  );

-- -----------------------------------------------------
-- 3. Future agenda blocks
-- -----------------------------------------------------
--
-- Creates four 30-minute blocks per weekday for the
-- next 180 days from the migration execution date.
-- -----------------------------------------------------

INSERT INTO agenda_blocks (
    medical_agenda_id,
    appointment_date,
    start_time,
    end_time,
    available,
    record_status,
    created_by
)
SELECT
    agenda.id,
    calendar_day.appointment_date,
    slot.start_time,
    slot.end_time,
    TRUE,
    'ACTIVE',
    'FLYWAY_DEMO'
FROM medical_agendas agenda
INNER JOIN doctors doctor
    ON doctor.id = agenda.doctor_id
INNER JOIN clinics clinic
    ON clinic.id = agenda.clinic_id
CROSS JOIN (
    SELECT generated_day::date AS appointment_date
    FROM generate_series(
        CURRENT_DATE + INTERVAL '1 day',
        CURRENT_DATE + INTERVAL '180 days',
        INTERVAL '1 day'
    ) AS generated_day
    WHERE EXTRACT(ISODOW FROM generated_day) BETWEEN 1 AND 5
) calendar_day
CROSS JOIN (
    VALUES
        (TIME '09:00', TIME '09:30'),
        (TIME '09:30', TIME '10:00'),
        (TIME '10:00', TIME '10:30'),
        (TIME '10:30', TIME '11:00')
) AS slot(start_time, end_time)
WHERE clinic.slug = 'agendoc-development-clinic'
  AND doctor.document_number IN (
      'DEMO-DOC-001',
      'DEMO-DOC-002',
      'DEMO-DOC-003'
  )
  AND agenda.active = TRUE
  AND agenda.record_status = 'ACTIVE'
  AND doctor.record_status = 'ACTIVE'
  AND NOT EXISTS (
      SELECT 1
      FROM agenda_blocks existing_block
      WHERE existing_block.medical_agenda_id = agenda.id
        AND existing_block.appointment_date = calendar_day.appointment_date
        AND existing_block.start_time = slot.start_time
        AND existing_block.end_time = slot.end_time
  );
