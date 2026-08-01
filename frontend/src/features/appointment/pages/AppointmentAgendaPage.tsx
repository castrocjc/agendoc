import { useEffect, useMemo, useState } from "react";
import type { FormEvent } from "react";

import {
  ArrowLeft,
  CalendarClock,
  CalendarDays,
  Clock3,
  Search,
  Stethoscope,
  UserCheck,
  UserRound,
  UserX,
  X,
} from "lucide-react";
import { useNavigate } from "react-router-dom";

import AppButton from "../../../components/AppButton";
import AppInput from "../../../components/AppInput";
import AppSelect from "../../../components/AppSelect";
import {
  DoctorServiceError,
  getDoctors,
} from "../../doctor/services/doctorService";
import type { DoctorResponse } from "../../doctor/types/doctor.types";
import {
  AppointmentServiceError,
  cancelAppointment,
  confirmAppointmentArrival,
  findAppointments,
  registerAppointmentNoShow,
  rescheduleAppointment,
} from "../services/appointmentService";
import {
  AgendaServiceError,
  findAgendaBlocks,
} from "../../agenda/services/agendaService";
import type { AppointmentAgendaResponse } from "../types/appointment.types";
import type { AgendaBlockResponse } from "../../agenda/types/agenda.types";

import "./AppointmentAgendaPage.css";

const STATUS_OPTIONS = [
  {
    value: "PROGRAMADA",
    label: "Programada",
  },
  {
    value: "CONFIRMADA",
    label: "Confirmada",
  },
  {
    value: "ATENDIDA",
    label: "Atendida",
  },
  {
    value: "CANCELADA",
    label: "Cancelada",
  },
  {
    value: "NO_ASISTIO",
    label: "No asistió",
  },
];

function getToday(): string {
  const today = new Date();

  const year = today.getFullYear();
  const month = String(today.getMonth() + 1).padStart(2, "0");
  const day = String(today.getDate()).padStart(2, "0");

  return `${year}-${month}-${day}`;
}

function formatDate(value: string): string {
  const [year, month, day] = value.split("-");

  return `${day}/${month}/${year}`;
}

function formatTime(value: string): string {
  return value.slice(0, 5);
}

function hasAppointmentStarted(
  appointment: AppointmentAgendaResponse,
): boolean {
  const appointmentStart = new Date(
    `${appointment.appointmentDate}T${appointment.startTime}`,
  );

  return appointmentStart.getTime() <= Date.now();
}

function getAppointmentStatusClass(statusCode: string): string {
  return `appointment-agenda-page__status appointment-agenda-page__status--${statusCode
    .toLowerCase()
    .replaceAll("_", "-")}`;
}

function AppointmentAgendaPage() {
  const navigate = useNavigate();

  const [doctors, setDoctors] = useState<DoctorResponse[]>([]);
  const [isLoadingDoctors, setIsLoadingDoctors] = useState(true);
  const [doctorError, setDoctorError] = useState("");

  const [selectedDate, setSelectedDate] = useState(getToday());
  const [selectedDoctorId, setSelectedDoctorId] = useState("");
  const [selectedStatus, setSelectedStatus] = useState("");

  const [appointments, setAppointments] = useState<AppointmentAgendaResponse[]>(
    [],
  );
  const [isLoadingAppointments, setIsLoadingAppointments] = useState(false);
  const [isCancellingAppointment, setIsCancellingAppointment] = useState(false);
  const [isConfirmingArrival, setIsConfirmingArrival] = useState(false);
  const [isRegisteringNoShow, setIsRegisteringNoShow] = useState(false);
  const [appointmentToReschedule, setAppointmentToReschedule] =
    useState<AppointmentAgendaResponse | null>(null);
  const [rescheduleDate, setRescheduleDate] = useState("");
  const [availableAgendaBlocks, setAvailableAgendaBlocks] = useState<
    AgendaBlockResponse[]
  >([]);
  const [selectedAgendaBlockId, setSelectedAgendaBlockId] = useState("");
  const [isLoadingAgendaBlocks, setIsLoadingAgendaBlocks] = useState(false);
  const [isReschedulingAppointment, setIsReschedulingAppointment] =
    useState(false);
  const [agendaBlockSelectionError, setAgendaBlockSelectionError] =
    useState("");
  const [agendaBlockError, setAgendaBlockError] = useState("");
  const [rescheduleDateError, setRescheduleDateError] = useState("");

  const [appointmentError, setAppointmentError] = useState("");
  const [successMessage, setSuccessMessage] = useState("");
  const [searchPerformed, setSearchPerformed] = useState(false);
  const [dateError, setDateError] = useState("");

  const doctorOptions = useMemo(
    () =>
      doctors.map((doctor) => ({
        value: doctor.id.toString(),
        label: `${doctor.firstName} ${doctor.lastName} · ${doctor.specialtyName}`,
      })),
    [doctors],
  );

  const agendaBlockOptions = useMemo(
    () =>
      availableAgendaBlocks.map((agendaBlock) => ({
        value: agendaBlock.id.toString(),
        label: `${formatTime(agendaBlock.startTime)} - ${formatTime(
          agendaBlock.endTime,
        )}`,
      })),
    [availableAgendaBlocks],
  );

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

  function handleBack(): void {
    navigate("/dashboard");
  }

  function clearSearchResult(): void {
    setAppointments([]);
    setAppointmentError("");
    setSuccessMessage("");
    setSearchPerformed(false);
  }

  function handleDateChange(value: string): void {
    setSelectedDate(value);
    setDateError("");
    clearSearchResult();
  }

  function handleDoctorChange(value: string): void {
    setSelectedDoctorId(value);
    clearSearchResult();
  }

  function handleStatusChange(value: string): void {
    setSelectedStatus(value);
    clearSearchResult();
  }

  function handleStartReschedule(appointment: AppointmentAgendaResponse): void {
    setAppointmentToReschedule(appointment);
    setRescheduleDate(appointment.appointmentDate);
    setAvailableAgendaBlocks([]);
    setSelectedAgendaBlockId("");
    setAgendaBlockError("");
    setAgendaBlockSelectionError("");
    setRescheduleDateError("");
    setAppointmentError("");
    setSuccessMessage("");
  }

  function handleCloseReschedule(): void {
    setAppointmentToReschedule(null);
    setRescheduleDate("");
    setAvailableAgendaBlocks([]);
    setSelectedAgendaBlockId("");
    setAgendaBlockError("");
    setAgendaBlockSelectionError("");
    setRescheduleDateError("");
  }

  function handleRescheduleDateChange(value: string): void {
    setRescheduleDate(value);
    setAvailableAgendaBlocks([]);
    setSelectedAgendaBlockId("");
    setAgendaBlockError("");
    setAgendaBlockSelectionError("");
    setRescheduleDateError("");
  }

  async function loadAppointments(): Promise<void> {
    const results = await findAppointments({
      date: selectedDate,
      doctorId: selectedDoctorId ? Number(selectedDoctorId) : undefined,
      status: selectedStatus || undefined,
    });

    setAppointments(
      [...results].sort((firstAppointment, secondAppointment) =>
        firstAppointment.startTime.localeCompare(secondAppointment.startTime),
      ),
    );
  }

  async function handleSearch(
    event: FormEvent<HTMLFormElement>,
  ): Promise<void> {
    event.preventDefault();

    setDateError("");
    setAppointmentError("");
    setSuccessMessage("");
    setAppointments([]);
    setSearchPerformed(true);

    if (!selectedDate) {
      setDateError("Selecciona la fecha que deseas consultar.");
      return;
    }

    setIsLoadingAppointments(true);

    try {
      await loadAppointments();
    } catch (error) {
      if (error instanceof AppointmentServiceError) {
        setAppointmentError(error.message);
      } else {
        setAppointmentError(
          "No fue posible consultar la agenda del consultorio.",
        );
      }
    } finally {
      setIsLoadingAppointments(false);
    }
  }

  async function handleFindAvailableAgendaBlocks(): Promise<void> {
    if (!appointmentToReschedule) {
      return;
    }

    setRescheduleDateError("");
    setAgendaBlockError("");
    setAvailableAgendaBlocks([]);
    setSelectedAgendaBlockId("");

    if (!rescheduleDate) {
      setRescheduleDateError("Selecciona la nueva fecha de la cita.");
      return;
    }

    setIsLoadingAgendaBlocks(true);

    try {
      const results = await findAgendaBlocks(
        appointmentToReschedule.doctorId,
        rescheduleDate,
      );

      const selectableBlocks = results.filter(
        (agendaBlock) =>
          agendaBlock.available &&
          agendaBlock.id !== appointmentToReschedule.agendaBlockId,
      );

      setAvailableAgendaBlocks(selectableBlocks);

      if (selectableBlocks.length === 0) {
        setAgendaBlockError(
          "El médico no tiene otros horarios disponibles para la fecha seleccionada.",
        );
      }
    } catch (error) {
      if (error instanceof AgendaServiceError) {
        setAgendaBlockError(error.message);
      } else {
        setAgendaBlockError(
          "No fue posible consultar los horarios disponibles.",
        );
      }
    } finally {
      setIsLoadingAgendaBlocks(false);
    }
  }

  async function handleConfirmReschedule(): Promise<void> {
    if (!appointmentToReschedule) {
      return;
    }

    setAgendaBlockSelectionError("");
    setAppointmentError("");
    setSuccessMessage("");

    if (!selectedAgendaBlockId) {
      setAgendaBlockSelectionError("Selecciona el nuevo horario de la cita.");
      return;
    }

    const selectedAgendaBlock = availableAgendaBlocks.find(
      (agendaBlock) => agendaBlock.id === Number(selectedAgendaBlockId),
    );

    if (!selectedAgendaBlock) {
      setAgendaBlockSelectionError(
        "El horario seleccionado ya no se encuentra disponible.",
      );
      return;
    }

    const confirmed = window.confirm(
      [
        "¿Deseas reprogramar esta cita?",
        "",
        `Horario actual: ${formatDate(
          appointmentToReschedule.appointmentDate,
        )} · ${formatTime(appointmentToReschedule.startTime)} - ${formatTime(
          appointmentToReschedule.endTime,
        )}`,
        "",
        `Nuevo horario: ${formatDate(
          selectedAgendaBlock.appointmentDate,
        )} · ${formatTime(selectedAgendaBlock.startTime)} - ${formatTime(
          selectedAgendaBlock.endTime,
        )}`,
      ].join("\n"),
    );

    if (!confirmed) {
      return;
    }

    setIsReschedulingAppointment(true);

    try {
      await rescheduleAppointment(appointmentToReschedule.id, {
        agendaBlockId: selectedAgendaBlock.id,
      });

      await loadAppointments();

      handleCloseReschedule();

      setSuccessMessage("La cita fue reprogramada correctamente.");
    } catch (error) {
      if (error instanceof AppointmentServiceError) {
        setAppointmentError(error.message);
      } else {
        setAppointmentError("No fue posible reprogramar la cita.");
      }
    } finally {
      setIsReschedulingAppointment(false);
    }
  }

  async function handleConfirmArrival(
    appointment: AppointmentAgendaResponse,
  ): Promise<void> {
    const confirmed = window.confirm(
      [
        "¿Deseas confirmar la llegada del paciente?",
        "",
        `Paciente: ${appointment.patientFirstName} ${appointment.patientLastName}`,
        `Horario: ${formatTime(appointment.startTime)} - ${formatTime(
          appointment.endTime,
        )}`,
      ].join("\n"),
    );

    if (!confirmed) {
      return;
    }

    setSuccessMessage("");
    setAppointmentError("");
    setIsConfirmingArrival(true);

    try {
      await confirmAppointmentArrival(appointment.id);

      await loadAppointments();

      setSuccessMessage(
        "La llegada del paciente fue confirmada correctamente.",
      );
    } catch (error) {
      if (error instanceof AppointmentServiceError) {
        setAppointmentError(error.message);
      } else {
        setAppointmentError(
          "No fue posible confirmar la llegada del paciente.",
        );
      }
    } finally {
      setIsConfirmingArrival(false);
    }
  }

async function handleRegisterNoShow(
  appointment: AppointmentAgendaResponse,
): Promise<void> {
  const comment = window.prompt(
    [
      "Registrar inasistencia",
      "",
      `Paciente: ${appointment.patientFirstName} ${appointment.patientLastName}`,
      `Horario: ${formatTime(appointment.startTime)} - ${formatTime(
        appointment.endTime,
      )}`,
      "",
      "Puedes ingresar un comentario opcional.",
      "Presiona Cancelar para cerrar sin realizar cambios.",
    ].join("\n"),
    "",
  );

  if (comment === null) {
    return;
  }

  const confirmed = window.confirm(
    [
      "¿Deseas registrar que el paciente no asistió?",
      "",
      "Esta acción dejará la cita en un estado final.",
    ].join("\n"),
  );

  if (!confirmed) {
    return;
  }

  setSuccessMessage("");
  setAppointmentError("");
  setIsRegisteringNoShow(true);

  try {
    await registerAppointmentNoShow(
      appointment.id,
      {
        comment: comment.trim() || null,
      },
    );

    await loadAppointments();

    setSuccessMessage(
      "La inasistencia del paciente fue registrada correctamente.",
    );
  } catch (error) {
    if (error instanceof AppointmentServiceError) {
      setAppointmentError(error.message);
    } else {
      setAppointmentError(
        "No fue posible registrar la inasistencia del paciente.",
      );
    }
  } finally {
    setIsRegisteringNoShow(false);
  }
}

  async function handleCancelAppointment(appointmentId: number): Promise<void> {
    const confirmed = window.confirm(
      "¿Deseas cancelar esta cita?\n\nEl horario quedará disponible nuevamente.",
    );

    if (!confirmed) {
      return;
    }

    setSuccessMessage("");
    setAppointmentError("");
    setIsCancellingAppointment(true);

    try {
      await cancelAppointment(appointmentId, {
        reason: null,
      });

      await loadAppointments();

      setSuccessMessage("La cita fue cancelada correctamente.");
    } catch (error) {
      if (error instanceof AppointmentServiceError) {
        setAppointmentError(error.message);
      } else {
        setAppointmentError("No fue posible cancelar la cita.");
      }
    } finally {
      setIsCancellingAppointment(false);
    }
  }

  return (
    <main className="appointment-agenda-page">
      <header className="appointment-agenda-page__header">
        <img
          src="/branding/agendoc-logo.png"
          alt="AgenDoc"
          className="appointment-agenda-page__logo"
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

      <section className="appointment-agenda-page__content">
        <div className="appointment-agenda-page__heading">
          <div
            className="appointment-agenda-page__heading-icon"
            aria-hidden="true"
          >
            <CalendarDays size={24} />
          </div>

          <div>
            <h1>Agenda del consultorio</h1>

            <p>
              Consulta las citas programadas y filtra la información por fecha,
              médico o estado.
            </p>
          </div>
        </div>

        <section
          className="appointment-agenda-page__card"
          aria-labelledby="appointment-filters-title"
        >
          <div className="appointment-agenda-page__section-heading">
            <div>
              <h2 id="appointment-filters-title">Filtros de consulta</h2>

              <p>La fecha es obligatoria. Los demás filtros son opcionales.</p>
            </div>
          </div>

          <form
            className="appointment-agenda-page__filters"
            onSubmit={(event) => void handleSearch(event)}
            noValidate
          >
            <AppInput
              id="appointmentAgendaDate"
              name="appointmentAgendaDate"
              label="Fecha"
              type="date"
              value={selectedDate}
              error={dateError}
              disabled={
                isLoadingAppointments ||
                isCancellingAppointment ||
                isConfirmingArrival ||
                isRegisteringNoShow ||
                isLoadingAgendaBlocks ||
                isReschedulingAppointment
              }
              onChange={(event) => handleDateChange(event.target.value)}
            />

            <AppSelect
              id="appointmentAgendaDoctor"
              name="appointmentAgendaDoctor"
              label="Médico"
              value={selectedDoctorId}
              options={doctorOptions}
              placeholder="Todos los médicos"
              helperText={doctorError ? doctorError : "Opcional"}
              disabled={
                isLoadingDoctors ||
                isLoadingAppointments ||
                isCancellingAppointment ||
                isConfirmingArrival ||
                isRegisteringNoShow ||
                isLoadingAgendaBlocks ||
                isReschedulingAppointment ||
                doctors.length === 0
              }
              onChange={(event) => handleDoctorChange(event.target.value)}
            />

            <AppSelect
              id="appointmentAgendaStatus"
              name="appointmentAgendaStatus"
              label="Estado"
              value={selectedStatus}
              options={STATUS_OPTIONS}
              placeholder="Todos los estados"
              helperText="Opcional"
              disabled={
                isLoadingAppointments ||
                isCancellingAppointment ||
                isConfirmingArrival ||
                isRegisteringNoShow ||
                isLoadingAgendaBlocks ||
                isReschedulingAppointment
              }
              onChange={(event) => handleStatusChange(event.target.value)}
            />

            <div className="appointment-agenda-page__filter-action">
              <AppButton
                type="submit"
                fullWidth={false}
                leftIcon={<Search size={18} />}
                isLoading={isLoadingAppointments}
                disabled={
                  isLoadingAppointments ||
                  isCancellingAppointment ||
                  isConfirmingArrival ||
                  isRegisteringNoShow ||
                  isLoadingAgendaBlocks ||
                  isReschedulingAppointment
                }
              >
                Consultar agenda
              </AppButton>
            </div>
          </form>
        </section>

        <section
          className="appointment-agenda-page__card"
          aria-labelledby="appointment-results-title"
          aria-live="polite"
          aria-busy={isLoadingAppointments}
        >
          <div className="appointment-agenda-page__section-heading">
            <div>
              <h2 id="appointment-results-title">Citas registradas</h2>

              <p>Las citas se muestran ordenadas por hora de inicio.</p>
            </div>

            {!isLoadingAppointments &&
              searchPerformed &&
              !appointmentError &&
              appointments.length > 0 && (
                <span className="appointment-agenda-page__count">
                  {appointments.length}
                </span>
              )}
          </div>

          {!searchPerformed && !isLoadingAppointments && (
            <div className="appointment-agenda-page__state">
              <CalendarDays size={32} aria-hidden="true" />

              <h3>Consulta la agenda</h3>

              <p>
                Selecciona los filtros y presiona Consultar agenda para
                visualizar las citas.
              </p>
            </div>
          )}

          {isLoadingAppointments && (
            <div className="appointment-agenda-page__state" role="status">
              <div
                className="appointment-agenda-page__spinner"
                aria-hidden="true"
              />

              <h3>Consultando agenda</h3>

              <p>Espera un momento mientras consultamos las citas.</p>
            </div>
          )}

          {!isLoadingAppointments && successMessage && (
            <div
              className="appointment-agenda-page__message appointment-agenda-page__message--success"
              role="status"
            >
              {successMessage}
            </div>
          )}

          {!isLoadingAppointments && appointmentToReschedule && (
            <section
              className="appointment-agenda-page__reschedule-panel"
              aria-labelledby="reschedule-appointment-title"
            >
              <div className="appointment-agenda-page__reschedule-header">
                <div>
                  <h3 id="reschedule-appointment-title">Reprogramar cita</h3>

                  <p>
                    Selecciona una nueva fecha y horario para la cita de{" "}
                    <strong>
                      {appointmentToReschedule.patientFirstName}{" "}
                      {appointmentToReschedule.patientLastName}
                    </strong>
                    .
                  </p>
                </div>

                <button
                  type="button"
                  className="appointment-agenda-page__reschedule-close"
                  aria-label="Cerrar reprogramación"
                  disabled={isLoadingAgendaBlocks || isReschedulingAppointment}
                  onClick={handleCloseReschedule}
                >
                  <X size={20} aria-hidden="true" />
                </button>
              </div>

              <div className="appointment-agenda-page__current-appointment">
                <div>
                  <span>Médico</span>

                  <strong>
                    {appointmentToReschedule.doctorFirstName}{" "}
                    {appointmentToReschedule.doctorLastName}
                  </strong>
                </div>

                <div>
                  <span>Horario actual</span>

                  <strong>
                    {formatDate(appointmentToReschedule.appointmentDate)}
                    {" · "}
                    {formatTime(appointmentToReschedule.startTime)}
                    {" - "}
                    {formatTime(appointmentToReschedule.endTime)}
                  </strong>
                </div>
              </div>

              <div className="appointment-agenda-page__reschedule-fields">
                <AppInput
                  id="rescheduleAppointmentDate"
                  name="rescheduleAppointmentDate"
                  label="Nueva fecha"
                  type="date"
                  value={rescheduleDate}
                  min={getToday()}
                  error={rescheduleDateError}
                  disabled={isLoadingAgendaBlocks || isReschedulingAppointment}
                  onChange={(event) =>
                    handleRescheduleDateChange(event.target.value)
                  }
                />

                <div className="appointment-agenda-page__reschedule-search">
                  <AppButton
                    type="button"
                    fullWidth={false}
                    leftIcon={<Search size={18} />}
                    isLoading={isLoadingAgendaBlocks}
                    disabled={
                      isLoadingAgendaBlocks ||
                      isReschedulingAppointment ||
                      !rescheduleDate
                    }
                    onClick={() => void handleFindAvailableAgendaBlocks()}
                  >
                    Consultar horarios
                  </AppButton>
                </div>

                <AppSelect
                  id="rescheduleAgendaBlock"
                  name="rescheduleAgendaBlock"
                  label="Nuevo horario"
                  value={selectedAgendaBlockId}
                  options={agendaBlockOptions}
                  placeholder="Selecciona un horario"
                  helperText={
                    agendaBlockError ||
                    (availableAgendaBlocks.length > 0
                      ? "Selecciona el nuevo horario de la cita."
                      : "Consulta primero los horarios disponibles.")
                  }
                  error={agendaBlockSelectionError}
                  disabled={
                    isLoadingAgendaBlocks ||
                    isReschedulingAppointment ||
                    availableAgendaBlocks.length === 0
                  }
                  onChange={(event) => {
                    setSelectedAgendaBlockId(event.target.value);
                    setAgendaBlockSelectionError("");
                  }}
                />
              </div>

              <div className="appointment-agenda-page__reschedule-actions">
                <AppButton
                  type="button"
                  variant="ghost"
                  fullWidth={false}
                  disabled={isLoadingAgendaBlocks || isReschedulingAppointment}
                  onClick={handleCloseReschedule}
                >
                  Cancelar
                </AppButton>

                <AppButton
                  type="button"
                  fullWidth={false}
                  leftIcon={<CalendarClock size={18} />}
                  isLoading={isReschedulingAppointment}
                  disabled={
                    isLoadingAgendaBlocks ||
                    isReschedulingAppointment ||
                    !selectedAgendaBlockId
                  }
                  onClick={() => void handleConfirmReschedule()}
                >
                  Confirmar reprogramación
                </AppButton>
              </div>
            </section>
          )}

          {!isLoadingAppointments && appointmentError && (
            <div
              className="appointment-agenda-page__state appointment-agenda-page__state--error"
              role="alert"
            >
              <CalendarDays size={32} aria-hidden="true" />

              <h3>No pudimos consultar la agenda</h3>

              <p>{appointmentError}</p>
            </div>
          )}

          {!isLoadingAppointments &&
            searchPerformed &&
            !appointmentError &&
            appointments.length === 0 && (
              <div className="appointment-agenda-page__state">
                <Clock3 size={32} aria-hidden="true" />

                <h3>No hay citas registradas</h3>

                <p>
                  No encontramos citas que coincidan con los filtros
                  seleccionados.
                </p>
              </div>
            )}

          {!isLoadingAppointments &&
            !appointmentError &&
            appointments.length > 0 && (
              <>
                <div className="appointment-agenda-page__table-wrapper">
                  <table className="appointment-agenda-page__table">
                    <thead>
                      <tr>
                        <th>Hora</th>
                        <th>Paciente</th>
                        <th>Médico</th>
                        <th>Especialidad</th>
                        <th>Estado</th>
                        <th>Motivo</th>
                        <th>Acciones</th>
                      </tr>
                    </thead>

                    <tbody>
                      {appointments.map((appointment) => (
                        <tr key={appointment.id}>
                          <td>
                            <span className="appointment-agenda-page__time">
                              {formatTime(appointment.startTime)}
                              {" - "}
                              {formatTime(appointment.endTime)}
                            </span>
                          </td>

                          <td>
                            <span className="appointment-agenda-page__person">
                              {appointment.patientFirstName}{" "}
                              {appointment.patientLastName}
                            </span>
                          </td>

                          <td>
                            <span className="appointment-agenda-page__person">
                              {appointment.doctorFirstName}{" "}
                              {appointment.doctorLastName}
                            </span>
                          </td>

                          <td>{appointment.specialtyName}</td>

                          <td>
                            <span
                              className={getAppointmentStatusClass(
                                appointment.statusCode,
                              )}
                            >
                              {appointment.statusName}
                            </span>
                          </td>

                          <td>
                            {appointment.reason?.trim() ||
                              "Sin motivo registrado"}
                          </td>
                          <td>
                            {appointment.statusCode === "PROGRAMADA" ? (
                              <div className="appointment-agenda-page__table-actions">
                                <AppButton
                                  type="button"
                                  fullWidth={false}
                                  leftIcon={<UserCheck size={16} />}
                                  isLoading={isConfirmingArrival}
                                  disabled={
                                    isCancellingAppointment ||
                                    isConfirmingArrival ||
                                    isRegisteringNoShow ||
                                    isLoadingAgendaBlocks ||
                                    isReschedulingAppointment
                                  }
                                  onClick={() =>
                                    void handleConfirmArrival(appointment)
                                  }
                                >
                                  Confirmar llegada
                                </AppButton>
                                {hasAppointmentStarted(appointment) && (
                                  <AppButton
                                    type="button"
                                    variant="outline"
                                    fullWidth={false}
                                    leftIcon={<UserX size={16} />}
                                    isLoading={isRegisteringNoShow}
                                    disabled={
                                      isCancellingAppointment ||
                                      isConfirmingArrival ||
                                      isRegisteringNoShow ||
                                      isLoadingAgendaBlocks ||
                                      isReschedulingAppointment
                                    }
                                    onClick={() =>
                                      void handleRegisterNoShow(appointment)
                                    }
                                  >
                                    No asistió
                                  </AppButton>
                                )}
                                <AppButton
                                  type="button"
                                  variant="outline"
                                  fullWidth={false}
                                  leftIcon={<CalendarClock size={16} />}
                                  disabled={
                                    isCancellingAppointment ||
                                    isConfirmingArrival ||
                                    isRegisteringNoShow ||
                                    isLoadingAgendaBlocks ||
                                    isReschedulingAppointment
                                  }
                                  onClick={() =>
                                    handleStartReschedule(appointment)
                                  }
                                >
                                  Reprogramar
                                </AppButton>
                                <AppButton
                                  type="button"
                                  variant="ghost"
                                  fullWidth={false}
                                  disabled={
                                    isCancellingAppointment ||
                                    isConfirmingArrival ||
                                    isRegisteringNoShow ||
                                    isLoadingAgendaBlocks ||
                                    isReschedulingAppointment
                                  }
                                  onClick={() =>
                                    void handleCancelAppointment(appointment.id)
                                  }
                                >
                                  Cancelar
                                </AppButton>
                              </div>
                            ) : (
                              <span aria-hidden="true">—</span>
                            )}
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>

                <div className="appointment-agenda-page__cards">
                  {appointments.map((appointment) => (
                    <article
                      key={appointment.id}
                      className="appointment-agenda-page__appointment-card"
                    >
                      <div className="appointment-agenda-page__appointment-card-header">
                        <div>
                          <p className="appointment-agenda-page__appointment-date">
                            {formatDate(appointment.appointmentDate)}
                          </p>

                          <p className="appointment-agenda-page__appointment-time">
                            {formatTime(appointment.startTime)}
                            {" - "}
                            {formatTime(appointment.endTime)}
                          </p>
                        </div>

                        <span
                          className={getAppointmentStatusClass(
                            appointment.statusCode,
                          )}
                        >
                          {appointment.statusName}
                        </span>
                      </div>

                      <div className="appointment-agenda-page__appointment-detail">
                        <UserRound size={18} aria-hidden="true" />

                        <div>
                          <span>Paciente</span>

                          <strong>
                            {appointment.patientFirstName}{" "}
                            {appointment.patientLastName}
                          </strong>
                        </div>
                      </div>

                      <div className="appointment-agenda-page__appointment-detail">
                        <Stethoscope size={18} aria-hidden="true" />

                        <div>
                          <span>Médico</span>

                          <strong>
                            {appointment.doctorFirstName}{" "}
                            {appointment.doctorLastName}
                          </strong>

                          <small>{appointment.specialtyName}</small>
                        </div>
                      </div>

                      <div className="appointment-agenda-page__appointment-reason">
                        <span>Motivo</span>

                        <p>
                          {appointment.reason?.trim() ||
                            "Sin motivo registrado"}
                        </p>
                      </div>

                      {appointment.statusCode === "PROGRAMADA" && (
                        <div className="appointment-agenda-page__appointment-actions">
                          <AppButton
                            type="button"
                            fullWidth
                            leftIcon={<UserCheck size={18} />}
                            isLoading={isConfirmingArrival}
                            disabled={
                              isCancellingAppointment ||
                              isConfirmingArrival ||
                              isRegisteringNoShow ||
                              isLoadingAgendaBlocks ||
                              isReschedulingAppointment
                            }
                            onClick={() =>
                              void handleConfirmArrival(appointment)
                            }
                          >
                            Confirmar llegada
                          </AppButton>
                          {hasAppointmentStarted(appointment) && (
                            <AppButton
                              type="button"
                              variant="outline"
                              fullWidth
                              leftIcon={<UserX size={18} />}
                              isLoading={isRegisteringNoShow}
                              disabled={
                                isCancellingAppointment ||
                                isConfirmingArrival ||
                                isRegisteringNoShow ||
                                isLoadingAgendaBlocks ||
                                isReschedulingAppointment
                              }
                              onClick={() =>
                                void handleRegisterNoShow(appointment)
                              }
                            >
                              Registrar inasistencia
                            </AppButton>
                          )}
                          <AppButton
                            type="button"
                            variant="outline"
                            fullWidth
                            leftIcon={<CalendarClock size={18} />}
                            disabled={
                              isCancellingAppointment ||
                              isConfirmingArrival ||
                              isRegisteringNoShow ||
                              isLoadingAgendaBlocks ||
                              isReschedulingAppointment
                            }
                            onClick={() => handleStartReschedule(appointment)}
                          >
                            Reprogramar cita
                          </AppButton>
                          <AppButton
                            type="button"
                            variant="ghost"
                            fullWidth
                            disabled={
                              isCancellingAppointment ||
                              isConfirmingArrival ||
                              isRegisteringNoShow ||
                              isLoadingAgendaBlocks ||
                              isReschedulingAppointment
                            }
                            onClick={() =>
                              void handleCancelAppointment(appointment.id)
                            }
                          >
                            Cancelar cita
                          </AppButton>
                        </div>
                      )}
                    </article>
                  ))}
                </div>
              </>
            )}
        </section>
      </section>
    </main>
  );
}

export default AppointmentAgendaPage;
