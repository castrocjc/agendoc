package com.agendoc.modules.appointment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agendoc.common.entity.RecordStatus;
import com.agendoc.modules.agenda.entity.AgendaBlockEntity;
import com.agendoc.modules.agenda.entity.MedicalAgendaEntity;
import com.agendoc.modules.agenda.repository.AgendaBlockRepository;
import com.agendoc.modules.appointment.dto.AppointmentResponse;
import com.agendoc.modules.appointment.dto.CreateAppointmentRequest;
import com.agendoc.modules.appointment.entity.AppointmentEntity;
import com.agendoc.modules.appointment.entity.AppointmentStatusCode;
import com.agendoc.modules.appointment.entity.AppointmentStatusEntity;
import com.agendoc.modules.appointment.repository.AppointmentRepository;
import com.agendoc.modules.appointment.repository.AppointmentStatusRepository;
import com.agendoc.modules.clinic.entity.ClinicEntity;
import com.agendoc.modules.clinic.repository.ClinicRepository;
import com.agendoc.modules.doctor.entity.DoctorEntity;
import com.agendoc.modules.doctor.repository.DoctorRepository;
import com.agendoc.modules.patient.entity.PatientEntity;
import com.agendoc.modules.patient.repository.PatientRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private AppointmentStatusRepository appointmentStatusRepository;

    @Mock
    private AgendaBlockRepository agendaBlockRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private ClinicRepository clinicRepository;

    private AppointmentServiceImpl appointmentService;

    @BeforeEach
    void setUp() {
        appointmentService = new AppointmentServiceImpl(
                appointmentRepository,
                appointmentStatusRepository,
                agendaBlockRepository,
                patientRepository,
                doctorRepository,
                clinicRepository
        );
    }

    @Test
    void shouldCreateAppointmentWhenRequestIsValid() {

        CreateAppointmentRequest request =
                new CreateAppointmentRequest(
                        1L,
                        2L,
                        12L,
                        "Consulta médica general",
                        "Primera cita del paciente"
                );

        ClinicEntity clinic = createClinic();
        PatientEntity patient = createPatient(clinic);
        DoctorEntity doctor = createDoctor(clinic);
        AgendaBlockEntity agendaBlock =
                createAgendaBlock(clinic, doctor);
        AppointmentStatusEntity appointmentStatus =
                createAppointmentStatus();

        when(clinicRepository
                .findFirstByRecordStatusOrderByIdAsc(
                        RecordStatus.ACTIVE
                ))
                .thenReturn(Optional.of(clinic));

        when(patientRepository.findByIdAndRecordStatus(
                1L,
                RecordStatus.ACTIVE
        ))
                .thenReturn(Optional.of(patient));

        when(doctorRepository.findByIdAndRecordStatus(
                2L,
                RecordStatus.ACTIVE
        ))
                .thenReturn(Optional.of(doctor));

        when(agendaBlockRepository
                .findByIdAndRecordStatusForUpdate(
                        12L,
                        RecordStatus.ACTIVE
                ))
                .thenReturn(Optional.of(agendaBlock));

        when(appointmentStatusRepository
                .findByCodeAndRecordStatus(
                        AppointmentStatusCode.PROGRAMADA.name(),
                        RecordStatus.ACTIVE
                ))
                .thenReturn(Optional.of(appointmentStatus));

        when(appointmentRepository.save(
                any(AppointmentEntity.class)
        ))
                .thenAnswer(invocation -> {
                    AppointmentEntity appointment =
                            invocation.getArgument(0);

                    appointment.setId(20L);

                    return appointment;
                });

        AppointmentResponse response =
                appointmentService.createAppointment(request);

        assertThat(response.id()).isEqualTo(20L);
        assertThat(response.clinicId()).isEqualTo(1L);

        assertThat(response.patientId()).isEqualTo(1L);
        assertThat(response.patientFirstName()).isEqualTo("María");
        assertThat(response.patientLastName()).isEqualTo("González");

        assertThat(response.doctorId()).isEqualTo(2L);
        assertThat(response.doctorFirstName()).isEqualTo("Ana");
        assertThat(response.doctorLastName()).isEqualTo("Torres");

        assertThat(response.agendaBlockId()).isEqualTo(12L);
        assertThat(response.appointmentDate())
                .isEqualTo(LocalDate.now().plusDays(1));
        assertThat(response.startTime())
                .isEqualTo(LocalTime.of(9, 0));
        assertThat(response.endTime())
                .isEqualTo(LocalTime.of(9, 30));

        assertThat(response.statusCode())
                .isEqualTo("PROGRAMADA");
        assertThat(response.statusName())
                .isEqualTo("Programada");

        assertThat(response.reason())
                .isEqualTo("Consulta médica general");
        assertThat(response.notes())
                .isEqualTo("Primera cita del paciente");
        assertThat(response.recordStatus())
                .isEqualTo("ACTIVE");

        ArgumentCaptor<AppointmentEntity> appointmentCaptor =
                ArgumentCaptor.forClass(AppointmentEntity.class);

        verify(appointmentRepository)
                .save(appointmentCaptor.capture());

        AppointmentEntity savedAppointment =
                appointmentCaptor.getValue();

        assertThat(savedAppointment.getClinic())
                .isSameAs(clinic);
        assertThat(savedAppointment.getPatient())
                .isSameAs(patient);
        assertThat(savedAppointment.getDoctor())
                .isSameAs(doctor);
        assertThat(savedAppointment.getAgendaBlock())
                .isSameAs(agendaBlock);
        assertThat(savedAppointment.getStatus())
                .isSameAs(appointmentStatus);

        assertThat(savedAppointment.getReason())
                .isEqualTo("Consulta médica general");
        assertThat(savedAppointment.getNotes())
                .isEqualTo("Primera cita del paciente");

        assertThat(agendaBlock.getAvailable()).isFalse();

        verify(clinicRepository)
                .findFirstByRecordStatusOrderByIdAsc(
                        RecordStatus.ACTIVE
                );

        verify(patientRepository)
                .findByIdAndRecordStatus(
                        1L,
                        RecordStatus.ACTIVE
                );

        verify(doctorRepository)
                .findByIdAndRecordStatus(
                        2L,
                        RecordStatus.ACTIVE
                );

        verify(agendaBlockRepository)
                .findByIdAndRecordStatusForUpdate(
                        12L,
                        RecordStatus.ACTIVE
                );

        verify(appointmentStatusRepository)
                .findByCodeAndRecordStatus(
                        AppointmentStatusCode.PROGRAMADA.name(),
                        RecordStatus.ACTIVE
                );
    }

    private ClinicEntity createClinic() {
        ClinicEntity clinic = new ClinicEntity();

        clinic.setId(1L);

        return clinic;
    }

    private PatientEntity createPatient(
            ClinicEntity clinic
    ) {
        PatientEntity patient = new PatientEntity();

        patient.setId(1L);
        patient.setClinic(clinic);
        patient.setFirstName("María");
        patient.setLastName("González");

        return patient;
    }

    private DoctorEntity createDoctor(
            ClinicEntity clinic
    ) {
        DoctorEntity doctor = new DoctorEntity();

        doctor.setId(2L);
        doctor.setClinic(clinic);
        doctor.setFirstName("Ana");
        doctor.setLastName("Torres");

        return doctor;
    }

    private AgendaBlockEntity createAgendaBlock(
            ClinicEntity clinic,
            DoctorEntity doctor
    ) {
        MedicalAgendaEntity medicalAgenda =
                new MedicalAgendaEntity();

        medicalAgenda.setId(5L);
        medicalAgenda.setClinic(clinic);
        medicalAgenda.setDoctor(doctor);
        medicalAgenda.setName("Agenda de Ana Torres");
        medicalAgenda.setActive(true);

        AgendaBlockEntity agendaBlock =
                new AgendaBlockEntity();

        agendaBlock.setId(12L);
        agendaBlock.setMedicalAgenda(medicalAgenda);
        agendaBlock.setAppointmentDate(
                LocalDate.now().plusDays(1)
        );
        agendaBlock.setStartTime(LocalTime.of(9, 0));
        agendaBlock.setEndTime(LocalTime.of(9, 30));
        agendaBlock.setAvailable(true);

        return agendaBlock;
    }

    private AppointmentStatusEntity createAppointmentStatus() {
        AppointmentStatusEntity appointmentStatus =
                new AppointmentStatusEntity();

        appointmentStatus.setId(1L);
        appointmentStatus.setCode(
                AppointmentStatusCode.PROGRAMADA.name()
        );
        appointmentStatus.setName("Programada");
        appointmentStatus.setDescription(
                "Cita médica registrada y pendiente de confirmación."
        );

        return appointmentStatus;
    }
}