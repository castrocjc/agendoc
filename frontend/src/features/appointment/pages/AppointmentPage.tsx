import {
  useEffect,
  useMemo,
  useState,
} from "react";
import type {
  FormEvent,
} from "react";

import {
  ArrowLeft,
  CalendarDays,
  Check,
  Clock3,
  Search,
  Stethoscope,
  UserRound,
} from "lucide-react";
import { useNavigate } from "react-router-dom";

import AppButton from "../../../components/AppButton";
import AppInput from "../../../components/AppInput";
import AppSelect from "../../../components/AppSelect";
import PatientSearchCard from "../../patient/components/PatientSearchCard";
import {
  PatientServiceError,
  searchPatients,
} from "../../patient/services/patientService";
import type {
  PatientResponse,
} from "../../patient/types/patient.types";
import {
  DoctorServiceError,
  getDoctors,
} from "../../doctor/services/doctorService";
import type {
  DoctorResponse,
} from "../../doctor/types/doctor.types";
import {
  AgendaServiceError,
  findAgendaBlocks,
} from "../../agenda/services/agendaService";
import type {
  AgendaBlockResponse,
} from "../../agenda/types/agenda.types";
import {
  formatAgendaDate,
  getToday,
} from "../../agenda/validation/agendaValidation";
import {
  AppointmentServiceError,
  createAppointment,
} from "../services/appointmentService";
import "./AppointmentPage.css";

function formatAgendaTime(value: string): string {
  return value.slice(0, 5);
}

function AppointmentPage() {
  const navigate = useNavigate();

  const [patientQuery, setPatientQuery] = useState("");
  const [patients, setPatients] = useState<PatientResponse[]>([]);
  const [selectedPatient, setSelectedPatient] =
    useState<PatientResponse | null>(null);
  const [isSearchingPatients, setIsSearchingPatients] =
    useState(false);
  const [patientSearchPerformed, setPatientSearchPerformed] =
    useState(false);
  const [patientSearchError, setPatientSearchError] =
    useState("");

  const [doctors, setDoctors] = useState<DoctorResponse[]>([]);
  const [selectedDoctorId, setSelectedDoctorId] = useState("");
  const [isLoadingDoctors, setIsLoadingDoctors] = useState(true);
  const [doctorError, setDoctorError] = useState("");

  const [appointmentDate, setAppointmentDate] = useState("");
  const [agendaBlocks, setAgendaBlocks] =
    useState<AgendaBlockResponse[]>([]);
  const [selectedAgendaBlockId, setSelectedAgendaBlockId] =
    useState("");
  const [isLoadingAgendaBlocks, setIsLoadingAgendaBlocks] =
    useState(false);
  const [agendaBlocksError, setAgendaBlocksError] =
    useState("");

  const [reason, setReason] = useState("");
  const [notes, setNotes] = useState("");

  const [isCreatingAppointment, setIsCreatingAppointment] =
    useState(false);

  const [appointmentError, setAppointmentError] =
    useState("");

  const [appointmentSuccess, setAppointmentSuccess] =
    useState("");

  const doctorOptions = useMemo(
    () =>
      doctors.map((doctor) => ({
        value: doctor.id.toString(),
        label: `${doctor.firstName} ${doctor.lastName} · ${doctor.specialtyName}`,
      })),
    [doctors],
  );

  const selectedDoctor = useMemo(
    () =>
      doctors.find(
        (doctor) =>
          doctor.id.toString() === selectedDoctorId,
      ) ?? null,
    [doctors, selectedDoctorId],
  );

  const selectedAgendaBlock = useMemo(
    () =>
      agendaBlocks.find(
        (block) =>
          block.id.toString() === selectedAgendaBlockId,
      ) ?? null,
    [agendaBlocks, selectedAgendaBlockId],
  );

  const hasAppointmentSelection =
    selectedPatient !== null
    && selectedDoctorId.length > 0
    && selectedAgendaBlockId.length > 0;

  const canCreateAppointment =
    hasAppointmentSelection
    && !isCreatingAppointment;

  useEffect(() => {
    let isMounted = true;

    async function loadDoctors(): Promise<void> {
      setIsLoadingDoctors(true);
      setDoctorError("");

      try {
        const results = await getDoctors();

        if (isMounted) {
          setDoctors(results);
        }
      } catch (error) {
        if (!isMounted) {
          return;
        }

        setDoctors([]);

        if (error instanceof DoctorServiceError) {
          setDoctorError(error.message);
        } else {
          setDoctorError(
            "No fue posible cargar los médicos. Inténtalo nuevamente.",
          );
        }
      } finally {
        if (isMounted) {
          setIsLoadingDoctors(false);
        }
      }
    }

    void loadDoctors();

    return () => {
      isMounted = false;
    };
  }, []);

  useEffect(() => {
    let isMounted = true;

    async function loadAgendaBlocks(): Promise<void> {
      if (!selectedDoctorId || !appointmentDate) {
        setAgendaBlocks([]);
        setSelectedAgendaBlockId("");
        setAgendaBlocksError("");
        setIsLoadingAgendaBlocks(false);
        return;
      }

      setIsLoadingAgendaBlocks(true);
      setAgendaBlocksError("");
      setSelectedAgendaBlockId("");

      try {
        const results = await findAgendaBlocks(
          Number(selectedDoctorId),
          appointmentDate,
        );

        if (isMounted) {
          setAgendaBlocks(
            [...results].sort((firstBlock, secondBlock) =>
              firstBlock.startTime.localeCompare(
                secondBlock.startTime,
              ),
            ),
          );
        }
      } catch (error) {
        if (!isMounted) {
          return;
        }

        setAgendaBlocks([]);

        if (error instanceof AgendaServiceError) {
          setAgendaBlocksError(error.message);
        } else {
          setAgendaBlocksError(
            "No fue posible consultar la disponibilidad.",
          );
        }
      } finally {
        if (isMounted) {
          setIsLoadingAgendaBlocks(false);
        }
      }
    }

    void loadAgendaBlocks();

    return () => {
      isMounted = false;
    };
  }, [selectedDoctorId, appointmentDate]);

  function handleBack(): void {
    navigate("/dashboard");
  }

  function clearPatientSearch(): void {
    setPatients([]);
    setPatientSearchPerformed(false);
    setPatientSearchError("");
  }

  function handlePatientQueryChange(value: string): void {
    setAppointmentSuccess("");
    setAppointmentError("");

    setPatientQuery(value);
    clearPatientSearch();
  }

  async function handleSearchPatients(
    event: FormEvent<HTMLFormElement>,
  ): Promise<void> {
    event.preventDefault();

    const normalizedQuery = patientQuery.trim();

    setPatientSearchPerformed(true);
    setPatientSearchError("");
    setPatients([]);

    if (!normalizedQuery) {
      setPatientSearchError(
        "Ingresa el nombre o documento del paciente.",
      );
      return;
    }

    setIsSearchingPatients(true);

    try {
      const results = await searchPatients(normalizedQuery);

      setPatients(results);
    } catch (error) {
      if (error instanceof PatientServiceError) {
        setPatientSearchError(error.message);
      } else {
        setPatientSearchError(
          "No fue posible buscar pacientes. Inténtalo nuevamente.",
        );
      }
    } finally {
      setIsSearchingPatients(false);
    }
  }

  function handleSelectPatient(
    patient: PatientResponse,
  ): void {
    setSelectedPatient(patient);
    setPatients([]);
    setPatientSearchPerformed(false);
    setPatientSearchError("");
  }

  function handleChangePatient(): void {
    setSelectedPatient(null);
    setPatientQuery("");
    setPatients([]);
    setPatientSearchPerformed(false);
    setPatientSearchError("");
  }

  function handleDoctorChange(value: string): void {
    setAppointmentSuccess("");
    setAppointmentError("");

    setSelectedDoctorId(value);
    setAppointmentDate("");
    setAgendaBlocks([]);
    setSelectedAgendaBlockId("");
    setAgendaBlocksError("");
  }

  function handleAppointmentDateChange(value: string): void {
    setAppointmentDate(value);
    setAgendaBlocks([]);
    setSelectedAgendaBlockId("");
    setAgendaBlocksError("");
  }

  function handleSelectAgendaBlock(
    block: AgendaBlockResponse,
  ): void {
    if (!block.available) {
      return;
    }

    setSelectedAgendaBlockId(block.id.toString());
  }

  async function handleCreateAppointment(): Promise<void> {
    if (
      !selectedPatient ||
      !selectedAgendaBlockId ||
      !selectedDoctorId
    ) {
      return;
    }

    setAppointmentError("");
    setAppointmentSuccess("");
    setIsCreatingAppointment(true);

    try {
      await createAppointment({
        patientId: selectedPatient.id,
        doctorId: Number(selectedDoctorId),
        agendaBlockId: Number(selectedAgendaBlockId),
        reason: reason.trim() || null,
        notes: notes.trim() || null,
      });

      setAppointmentSuccess(
        "La cita médica fue registrada correctamente.",
      );

      setSelectedPatient(null);
      setPatientQuery("");
      setPatients([]);
      setPatientSearchPerformed(false);
      setPatientSearchError("");

      setSelectedDoctorId("");
      setAppointmentDate("");
      setAgendaBlocks([]);
      setSelectedAgendaBlockId("");
      setAgendaBlocksError("");

      setReason("");
      setNotes("");
    } catch (error) {
      if (error instanceof AppointmentServiceError) {
        setAppointmentError(error.message);
      } else {
        setAppointmentError(
          "No fue posible registrar la cita médica.",
        );
      }
    } finally {
      setIsCreatingAppointment(false);
    }
  }  

  return (
    <main className="appointment-page">
      <header className="appointment-page__header">
        <img
          src="/branding/agendoc-logo.png"
          alt="AgenDoc"
          className="appointment-page__logo"
        />

        <AppButton
          type="button"
          variant="ghost"
          fullWidth={false}
          leftIcon={<ArrowLeft size={18} />}
          onClick={handleBack}
        >
          Volver
        </AppButton>
      </header>

      <section className="appointment-page__content">
        <div className="appointment-page__heading">
          <div
            className="appointment-page__heading-icon"
            aria-hidden="true"
          >
            <CalendarDays size={24} />
          </div>

          <div>
            <h1>Agendar cita médica</h1>

            <p>
              Selecciona al paciente, médico y horario para preparar
              una nueva cita.
            </p>
          </div>
        </div>

        <section
          className="appointment-page__card"
          aria-labelledby="patient-selection-title"
        >
          <div className="appointment-page__section-heading">
            <div>
              <h2 id="patient-selection-title">
                1. Seleccionar paciente
              </h2>

              <p>
                Busca al paciente por nombre o número de documento.
              </p>
            </div>

            {selectedPatient && (
              <span className="appointment-page__completed">
                <Check size={15} aria-hidden="true" />
                Seleccionado
              </span>
            )}
          </div>

          {!selectedPatient && (
            <>
              <form
                className="appointment-page__patient-search"
                onSubmit={(event) =>
                  void handleSearchPatients(event)
                }
                noValidate
              >
                <AppInput
                  id="patientQuery"
                  name="patientQuery"
                  label="Paciente"
                  type="search"
                  value={patientQuery}
                  placeholder="Nombre o número de documento"
                  helperText="Ingresa al menos un dato para realizar la búsqueda."
                  disabled={isSearchingPatients}
                  onChange={(event) =>
                    handlePatientQueryChange(event.target.value)
                  }
                />

                <AppButton
                  type="submit"
                  fullWidth={false}
                  leftIcon={<Search size={18} />}
                  isLoading={isSearchingPatients}
                  disabled={isSearchingPatients}
                >
                  Buscar paciente
                </AppButton>
              </form>

              {patientSearchError && (
                <div
                  className="appointment-page__message appointment-page__message--error"
                  role="alert"
                >
                  {patientSearchError}
                </div>
              )}

              {isSearchingPatients && (
                <div
                  className="appointment-page__state"
                  role="status"
                >
                  <div
                    className="appointment-page__spinner"
                    aria-hidden="true"
                  />

                  <h3>Buscando pacientes</h3>

                  <p>
                    Espera un momento mientras consultamos la
                    información.
                  </p>
                </div>
              )}

              {!isSearchingPatients
                && patientSearchPerformed
                && !patientSearchError
                && patients.length === 0 && (
                  <div className="appointment-page__state">
                    <UserRound size={32} aria-hidden="true" />

                    <h3>No encontramos pacientes</h3>

                    <p>
                      Verifica el nombre o documento ingresado e
                      inténtalo nuevamente.
                    </p>
                  </div>
                )}

              {!isSearchingPatients && patients.length > 0 && (
                <div className="appointment-page__patient-results">
                  {patients.map((patient) => (
                    <div
                      key={patient.id}
                      className="appointment-page__patient-result"
                    >
                      <PatientSearchCard patient={patient} />

                      <div className="appointment-page__patient-action">
                        <AppButton
                          type="button"
                          fullWidth={false}
                          onClick={() =>
                            handleSelectPatient(patient)
                          }
                        >
                          Seleccionar paciente
                        </AppButton>
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </>
          )}

          {selectedPatient && (
            <div className="appointment-page__selected-patient">
              <PatientSearchCard patient={selectedPatient} />

              <AppButton
                type="button"
                variant="ghost"
                fullWidth={false}
                onClick={handleChangePatient}
              >
                Cambiar paciente
              </AppButton>
            </div>
          )}
        </section>

        <section
          className="appointment-page__card"
          aria-labelledby="doctor-selection-title"
          aria-live="polite"
          aria-busy={isLoadingDoctors}
        >
          <div className="appointment-page__section-heading">
            <div>
              <h2 id="doctor-selection-title">
                2. Seleccionar médico y fecha
              </h2>

              <p>
                Selecciona el profesional y la fecha de atención.
              </p>
            </div>

            {selectedDoctorId && appointmentDate && (
              <span className="appointment-page__completed">
                <Check size={15} aria-hidden="true" />
                Completado
              </span>
            )}
          </div>

          {isLoadingDoctors && (
            <div
              className="appointment-page__state"
              role="status"
            >
              <div
                className="appointment-page__spinner"
                aria-hidden="true"
              />

              <h3>Cargando médicos</h3>

              <p>
                Espera un momento mientras consultamos la información.
              </p>
            </div>
          )}

          {!isLoadingDoctors && doctorError && (
            <div
              className="appointment-page__state appointment-page__state--error"
              role="alert"
            >
              <Stethoscope size={32} aria-hidden="true" />

              <h3>No pudimos cargar los médicos</h3>

              <p>{doctorError}</p>
            </div>
          )}

          {!isLoadingDoctors
            && !doctorError
            && doctors.length === 0 && (
              <div className="appointment-page__state">
                <Stethoscope size={32} aria-hidden="true" />

                <h3>No hay médicos disponibles</h3>

                <p>
                  Registra al menos un médico antes de agendar una
                  cita.
                </p>
              </div>
            )}

          {!isLoadingDoctors
            && !doctorError
            && doctors.length > 0 && (
              <div className="appointment-page__doctor-fields">
                <AppSelect
                  id="doctorId"
                  name="doctorId"
                  label="Médico"
                  value={selectedDoctorId}
                  options={doctorOptions}
                  placeholder="Selecciona un médico"
                  helperText="La disponibilidad depende del médico seleccionado."
                  onChange={(event) =>
                    handleDoctorChange(event.target.value)
                  }
                />

                <AppInput
                  id="appointmentDate"
                  name="appointmentDate"
                  label="Fecha de la cita"
                  type="date"
                  min={getToday()}
                  value={appointmentDate}
                  disabled={!selectedDoctorId}
                  helperText={
                    selectedDoctorId
                      ? "Selecciona la fecha que deseas consultar."
                      : "Selecciona primero un médico."
                  }
                  onChange={(event) =>
                    handleAppointmentDateChange(
                      event.target.value,
                    )
                  }
                />
              </div>
            )}
        </section>

        <section
          className="appointment-page__card"
          aria-labelledby="schedule-selection-title"
          aria-live="polite"
          aria-busy={isLoadingAgendaBlocks}
        >
          <div className="appointment-page__section-heading">
            <div>
              <h2 id="schedule-selection-title">
                3. Seleccionar horario
              </h2>

              <p>
                Elige uno de los bloques disponibles para la cita.
              </p>
            </div>

            {selectedAgendaBlock && (
              <span className="appointment-page__completed">
                <Check size={15} aria-hidden="true" />
                Seleccionado
              </span>
            )}
          </div>

          {!selectedDoctorId || !appointmentDate ? (
            <div className="appointment-page__state">
              <CalendarDays size={32} aria-hidden="true" />

              <h3>Selecciona médico y fecha</h3>

              <p>
                Los horarios registrados aparecerán en esta sección.
              </p>
            </div>
          ) : null}

          {selectedDoctorId
            && appointmentDate
            && isLoadingAgendaBlocks && (
              <div
                className="appointment-page__state"
                role="status"
              >
                <div
                  className="appointment-page__spinner"
                  aria-hidden="true"
                />

                <h3>Consultando disponibilidad</h3>

                <p>
                  Espera mientras consultamos los horarios del médico.
                </p>
              </div>
            )}

          {selectedDoctorId
            && appointmentDate
            && !isLoadingAgendaBlocks
            && agendaBlocksError && (
              <div
                className="appointment-page__state appointment-page__state--error"
                role="alert"
              >
                <CalendarDays size={32} aria-hidden="true" />

                <h3>No pudimos consultar la disponibilidad</h3>

                <p>{agendaBlocksError}</p>
              </div>
            )}

          {selectedDoctorId
            && appointmentDate
            && !isLoadingAgendaBlocks
            && !agendaBlocksError
            && agendaBlocks.length === 0 && (
              <div className="appointment-page__state">
                <Clock3 size={32} aria-hidden="true" />

                <h3>No hay horarios registrados</h3>

                <p>
                  El médico no tiene bloques de agenda para la fecha
                  seleccionada.
                </p>
              </div>
            )}

          {!isLoadingAgendaBlocks
            && !agendaBlocksError
            && agendaBlocks.length > 0 && (
              <div className="appointment-page__schedule-list">
                {agendaBlocks.map((block) => {
                  const isSelected =
                    block.id.toString()
                    === selectedAgendaBlockId;

                  return (
                    <button
                      key={block.id}
                      type="button"
                      className={[
                        "appointment-page__schedule",
                        block.available
                          ? "appointment-page__schedule--available"
                          : "appointment-page__schedule--unavailable",
                        isSelected
                          ? "appointment-page__schedule--selected"
                          : "",
                      ]
                        .filter(Boolean)
                        .join(" ")}
                      disabled={!block.available}
                      aria-pressed={isSelected}
                      onClick={() =>
                        handleSelectAgendaBlock(block)
                      }
                    >
                      <div>
                        <p className="appointment-page__schedule-date">
                          {formatAgendaDate(
                            block.appointmentDate,
                          )}
                        </p>

                        <p className="appointment-page__schedule-time">
                          {formatAgendaTime(block.startTime)}
                          {" - "}
                          {formatAgendaTime(block.endTime)}
                        </p>
                      </div>

                      <span className="appointment-page__schedule-status">
                        {isSelected
                          ? "Seleccionado"
                          : block.available
                            ? "Disponible"
                            : "No disponible"}
                      </span>
                    </button>
                  );
                })}
              </div>
            )}
        </section>

        <section
          className="appointment-page__card"
          aria-labelledby="appointment-details-title"
        >
          <div className="appointment-page__section-heading">
            <div>
              <h2 id="appointment-details-title">
                4. Detalles de la cita
              </h2>

              <p>
                Registra información complementaria para la atención.
              </p>
            </div>
          </div>

          <div className="appointment-page__details-fields">
            <div className="appointment-page__textarea-field">
              <label htmlFor="reason">
                Motivo de la cita
              </label>

              <textarea
                id="reason"
                name="reason"
                value={reason}
                maxLength={500}
                placeholder="Describe brevemente el motivo de la consulta"
                onChange={(event) =>
                  setReason(event.target.value)
                }
              />

              <div className="appointment-page__field-footer">
                <span>Opcional</span>
                <span>{reason.length}/500</span>
              </div>
            </div>

            <div className="appointment-page__textarea-field">
              <label htmlFor="notes">
                Observaciones
              </label>

              <textarea
                id="notes"
                name="notes"
                value={notes}
                maxLength={1000}
                placeholder="Agrega observaciones relevantes para la cita"
                onChange={(event) =>
                  setNotes(event.target.value)
                }
              />

              <div className="appointment-page__field-footer">
                <span>Opcional</span>
                <span>{notes.length}/1000</span>
              </div>
            </div>
          </div>
        </section>

        {appointmentSuccess && (
          <div
            className="appointment-page__message appointment-page__message--success"
          >
            {appointmentSuccess}
          </div>
        )}

        {appointmentError && (
          <div
            className="appointment-page__message appointment-page__message--error"
          >
            {appointmentError}
          </div>
        )}

        <section
          className="appointment-page__card appointment-page__summary"
          aria-labelledby="appointment-summary-title"
        >
          <div className="appointment-page__section-heading">
            <div>
              <h2 id="appointment-summary-title">
                Resumen de la cita
              </h2>

              <p>
                Revisa la información antes de registrar la cita.
              </p>
            </div>
          </div>

          {!hasAppointmentSelection && (
            <div className="appointment-page__state">
              <CalendarDays size={32} aria-hidden="true" />

              <h3>Completa la información requerida</h3>

              <p>
                Selecciona un paciente, un médico y un horario
                disponible.
              </p>
            </div>
          )}

          {hasAppointmentSelection
            && selectedPatient
            && selectedDoctor
            && selectedAgendaBlock && (
              <div className="appointment-page__summary-content">
                <dl className="appointment-page__summary-list">
                  <div>
                    <dt>Paciente</dt>

                    <dd>
                      {selectedPatient.firstName}
                      {" "}
                      {selectedPatient.lastName}
                    </dd>
                  </div>

                  <div>
                    <dt>Médico</dt>

                    <dd>
                      {selectedDoctor.firstName}
                      {" "}
                      {selectedDoctor.lastName}
                    </dd>
                  </div>

                  <div>
                    <dt>Especialidad</dt>

                    <dd>{selectedDoctor.specialtyName}</dd>
                  </div>

                  <div>
                    <dt>Fecha</dt>

                    <dd>
                      {formatAgendaDate(
                        selectedAgendaBlock.appointmentDate,
                      )}
                    </dd>
                  </div>

                  <div>
                    <dt>Horario</dt>

                    <dd>
                      {formatAgendaTime(
                        selectedAgendaBlock.startTime,
                      )}
                      {" - "}
                      {formatAgendaTime(
                        selectedAgendaBlock.endTime,
                      )}
                    </dd>
                  </div>
                </dl>

                <div className="appointment-page__actions">
                  <AppButton
                    type="button"
                    fullWidth={false}
                    disabled={!canCreateAppointment}
                    isLoading={isCreatingAppointment}
                    onClick={() => void handleCreateAppointment()}
                  >
                    Agendar cita
                  </AppButton>
                </div>
              </div>
            )}
        </section>
      </section>
    </main>
  );
}

export default AppointmentPage;