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
  Plus,
  Save,
  Stethoscope,
  Trash2,
} from "lucide-react";
import { useNavigate } from "react-router-dom";

import AppButton from "../../../components/AppButton";
import AppInput from "../../../components/AppInput";
import AppSelect from "../../../components/AppSelect";
import {
  DoctorServiceError,
  getDoctors,
} from "../../doctor/services/doctorService";
import type {
  DoctorResponse,
} from "../../doctor/types/doctor.types";
import {
  AgendaServiceError,
  createAgendaBlocks,
  findAgendaBlocks,
} from "../services/agendaService";
import type {
  AgendaBlockResponse,
} from "../types/agenda.types";
import {
  formatAgendaDate,
  getToday,
  validateAgendaBlock,
} from "../validation/agendaValidation";

import "./MedicalAgendaPage.css";

interface PreparedAgendaBlock {
  id: string;
  appointmentDate: string;
  startTime: string;
  endTime: string;
}

function formatAgendaTime(value: string): string {
  return value.slice(0, 5);
}

function MedicalAgendaPage() {
  const navigate = useNavigate();

  const [doctors, setDoctors] = useState<DoctorResponse[]>([]);
  const [selectedDoctorId, setSelectedDoctorId] = useState("");
  const [isLoadingDoctors, setIsLoadingDoctors] = useState(true);
  const [doctorError, setDoctorError] = useState("");

  const [appointmentDate, setAppointmentDate] = useState("");
  const [startTime, setStartTime] = useState("");
  const [endTime, setEndTime] = useState("");

  const [selectedDoctorError, setSelectedDoctorError] =
    useState("");
  const [dateError, setDateError] = useState("");
  const [startTimeError, setStartTimeError] = useState("");
  const [endTimeError, setEndTimeError] = useState("");

  const [preparedBlocks, setPreparedBlocks] =
    useState<PreparedAgendaBlock[]>([]);

  const [existingBlocks, setExistingBlocks] =
    useState<AgendaBlockResponse[]>([]);
  const [isLoadingExistingBlocks, setIsLoadingExistingBlocks] =
    useState(false);
  const [existingBlocksError, setExistingBlocksError] =
    useState("");

  const [isSaving, setIsSaving] = useState(false);
  const [saveError, setSaveError] = useState("");
  const [successMessage, setSuccessMessage] = useState("");

  const doctorOptions = useMemo(
    () =>
      doctors.map((doctor) => ({
        value: doctor.id.toString(),
        label: `${doctor.firstName} ${doctor.lastName} · ${doctor.specialtyName}`,
      })),
    [doctors],
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

  useEffect(() => {
    let isMounted = true;

    async function loadExistingBlocks(): Promise<void> {
      if (!selectedDoctorId || !appointmentDate) {
        setExistingBlocks([]);
        setExistingBlocksError("");
        setIsLoadingExistingBlocks(false);
        return;
      }

      setIsLoadingExistingBlocks(true);
      setExistingBlocksError("");

      try {
        const results = await findAgendaBlocks(
          Number(selectedDoctorId),
          appointmentDate,
        );

        if (isMounted) {
          setExistingBlocks(
            [...results].sort((firstBlock, secondBlock) =>
              firstBlock.startTime.localeCompare(secondBlock.startTime),
            ),
          );
        }
      } catch (error) {
        if (!isMounted) {
          return;
        }

        setExistingBlocks([]);

        if (error instanceof AgendaServiceError) {
          setExistingBlocksError(error.message);
        } else {
          setExistingBlocksError(
            "No fue posible consultar la agenda existente.",
          );
        }
      } finally {
        if (isMounted) {
          setIsLoadingExistingBlocks(false);
        }
      }
    }

    void loadExistingBlocks();

    return () => {
      isMounted = false;
    };
  }, [selectedDoctorId, appointmentDate]);

  useEffect(() => {
    if (!successMessage) {
      return;
    }

    const timeoutId = window.setTimeout(() => {
      setSuccessMessage("");
    }, 5000);

    return () => {
      window.clearTimeout(timeoutId);
    };
  }, [successMessage]);

  function handleBack(): void {
    navigate("/dashboard");
  }

  function clearOperationMessages(): void {
    setSaveError("");
    setSuccessMessage("");
  }

  function handleDoctorChange(value: string): void {
    setSelectedDoctorId(value);
    setSelectedDoctorError("");
    setPreparedBlocks([]);
    setExistingBlocks([]);
    setExistingBlocksError("");
    clearOperationMessages();
  }

  function handleDateChange(value: string): void {
    setAppointmentDate(value);
    setDateError("");
    setPreparedBlocks([]);
    setExistingBlocks([]);
    setExistingBlocksError("");
    clearOperationMessages();
  }

  function handleStartTimeChange(value: string): void {
    setStartTime(value);
    setStartTimeError("");
    setEndTimeError("");
    clearOperationMessages();
  }

  function handleEndTimeChange(value: string): void {
    setEndTime(value);
    setEndTimeError("");
    clearOperationMessages();
  }

  function validateBlock(): boolean {
    const validation = validateAgendaBlock({
      doctorId: selectedDoctorId,
      appointmentDate,
      startTime,
      endTime,
    });

    setSelectedDoctorError(validation.errors.doctorId);
    setDateError(validation.errors.appointmentDate);
    setStartTimeError(validation.errors.startTime);
    setEndTimeError(validation.errors.endTime);

    return validation.isValid;
  }

  function blockAlreadyPrepared(): boolean {
    return preparedBlocks.some(
      (block) =>
        block.appointmentDate === appointmentDate
        && block.startTime === startTime
        && block.endTime === endTime,
    );
  }

  function handleAddBlock(
    event: FormEvent<HTMLFormElement>,
  ): void {
    event.preventDefault();

    clearOperationMessages();

    if (!validateBlock()) {
      return;
    }

    if (blockAlreadyPrepared()) {
      setEndTimeError(
        "Este bloque ya fue agregado a la lista preparada.",
      );
      return;
    }

    const newBlock: PreparedAgendaBlock = {
      id: crypto.randomUUID(),
      appointmentDate,
      startTime,
      endTime,
    };

    setPreparedBlocks((currentBlocks) => [
      ...currentBlocks,
      newBlock,
    ]);

    setStartTime("");
    setEndTime("");
  }

  function handleRemoveBlock(blockId: string): void {
    clearOperationMessages();

    setPreparedBlocks((currentBlocks) =>
      currentBlocks.filter((block) => block.id !== blockId),
    );
  }

  async function refreshExistingBlocks(): Promise<void> {
    if (!selectedDoctorId || !appointmentDate) {
      return;
    }

    setIsLoadingExistingBlocks(true);
    setExistingBlocksError("");

    try {
      const results = await findAgendaBlocks(
        Number(selectedDoctorId),
        appointmentDate,
      );

      setExistingBlocks(
        [...results].sort((firstBlock, secondBlock) =>
          firstBlock.startTime.localeCompare(secondBlock.startTime),
        ),
      );
    } catch (error) {
      setExistingBlocks([]);

      if (error instanceof AgendaServiceError) {
        setExistingBlocksError(error.message);
      } else {
        setExistingBlocksError(
          "No fue posible actualizar la agenda existente.",
        );
      }
    } finally {
      setIsLoadingExistingBlocks(false);
    }
  }

  async function handleSaveBlocks(): Promise<void> {
    if (
      !selectedDoctorId
      || preparedBlocks.length === 0
      || isSaving
    ) {
      return;
    }

    setIsSaving(true);
    setSaveError("");
    setSuccessMessage("");

    try {
      const response = await createAgendaBlocks(
        Number(selectedDoctorId),
        {
          blocks: preparedBlocks.map((block) => ({
            appointmentDate: block.appointmentDate,
            startTime: block.startTime,
            endTime: block.endTime,
          })),
        },
      );

      const createdCount = response.createdBlocks.length;

      setPreparedBlocks([]);
      setStartTime("");
      setEndTime("");

      setSuccessMessage(
        createdCount === 1
          ? "Se creó 1 bloque de agenda correctamente."
          : `Se crearon ${createdCount} bloques de agenda correctamente.`,
      );

      await refreshExistingBlocks();
    } catch (error) {
      if (error instanceof AgendaServiceError) {
        setSaveError(error.message);
      } else {
        setSaveError(
          "No fue posible guardar los bloques. Inténtalo nuevamente.",
        );
      }
    } finally {
      setIsSaving(false);
    }
  }

  return (
    <main className="medical-agenda-page">
      <header className="medical-agenda-page__header">
        <img
          src="/branding/agendoc-logo.png"
          alt="AgenDoc"
          className="medical-agenda-page__logo"
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

      <section className="medical-agenda-page__content">
        <div className="medical-agenda-page__heading">
          <div
            className="medical-agenda-page__icon"
            aria-hidden="true"
          >
            <CalendarDays size={24} />
          </div>

          <div>
            <h1>Agenda médica</h1>

            <p>
              Configura los bloques de disponibilidad de los médicos.
            </p>
          </div>
        </div>

        <section
          className="medical-agenda-page__card medical-agenda-page__doctor-card"
          aria-labelledby="doctor-selection-title"
          aria-live="polite"
          aria-busy={isLoadingDoctors}
        >
          <div className="medical-agenda-page__section-heading">
            <div>
              <h2 id="doctor-selection-title">
                Seleccionar médico
              </h2>

              <p>
                Selecciona el médico cuya disponibilidad deseas
                configurar.
              </p>
            </div>
          </div>

          {isLoadingDoctors && (
            <div
              className="medical-agenda-page__state"
              role="status"
            >
              <div
                className="medical-agenda-page__spinner"
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
              className="medical-agenda-page__state medical-agenda-page__state--error"
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
              <div className="medical-agenda-page__state">
                <Stethoscope size={32} aria-hidden="true" />

                <h3>No hay médicos disponibles</h3>

                <p>
                  Registra al menos un médico antes de configurar su
                  agenda.
                </p>
              </div>
            )}

          {!isLoadingDoctors
            && !doctorError
            && doctors.length > 0 && (
              <div className="medical-agenda-page__doctor-selection">
                <AppSelect
                  id="doctorId"
                  name="doctorId"
                  label="Médico"
                  value={selectedDoctorId}
                  options={doctorOptions}
                  placeholder="Selecciona un médico"
                  helperText="Los bloques quedarán asociados al médico seleccionado."
                  error={selectedDoctorError}
                  disabled={isSaving}
                  onChange={(event) =>
                    handleDoctorChange(event.target.value)
                  }
                />
              </div>
            )}
        </section>

        {!isLoadingDoctors
          && !doctorError
          && doctors.length > 0 && (
            <>
              <div className="medical-agenda-page__workspace">
                <section
                  className="medical-agenda-page__card"
                  aria-labelledby="create-block-title"
                >
                  <div className="medical-agenda-page__section-heading">
                    <div>
                      <h2 id="create-block-title">
                        Crear bloque
                      </h2>

                      <p>
                        Define la fecha y el horario de atención.
                      </p>
                    </div>
                  </div>

                  <form
                    className="medical-agenda-page__form"
                    onSubmit={handleAddBlock}
                    noValidate
                  >
                    <AppInput
                      id="appointmentDate"
                      name="appointmentDate"
                      label="Fecha"
                      type="date"
                      min={getToday()}
                      value={appointmentDate}
                      error={dateError}
                      disabled={isSaving}
                      onChange={(event) =>
                        handleDateChange(event.target.value)
                      }
                    />

                    <div className="medical-agenda-page__time-fields">
                      <AppInput
                        id="startTime"
                        name="startTime"
                        label="Hora de inicio"
                        type="time"
                        value={startTime}
                        error={startTimeError}
                        disabled={isSaving}
                        onChange={(event) =>
                          handleStartTimeChange(event.target.value)
                        }
                      />

                      <AppInput
                        id="endTime"
                        name="endTime"
                        label="Hora de fin"
                        type="time"
                        value={endTime}
                        error={endTimeError}
                        disabled={isSaving}
                        onChange={(event) =>
                          handleEndTimeChange(event.target.value)
                        }
                      />
                    </div>

                    <div className="medical-agenda-page__form-actions">
                      <AppButton
                        type="submit"
                        fullWidth={false}
                        leftIcon={<Plus size={18} />}
                        disabled={isSaving}
                      >
                        Agregar bloque
                      </AppButton>
                    </div>
                  </form>
                </section>

                <section
                  className="medical-agenda-page__card"
                  aria-labelledby="prepared-blocks-title"
                  aria-live="polite"
                >
                  <div className="medical-agenda-page__section-heading">
                    <div>
                      <h2 id="prepared-blocks-title">
                        Bloques preparados
                      </h2>

                      <p>
                        Revisa los bloques antes de guardarlos.
                      </p>
                    </div>

                    {preparedBlocks.length > 0 && (
                      <span className="medical-agenda-page__count">
                        {preparedBlocks.length}
                      </span>
                    )}
                  </div>

                  {preparedBlocks.length === 0 && (
                    <div className="medical-agenda-page__state">
                      <CalendarDays size={32} aria-hidden="true" />

                      <h3>No hay bloques preparados</h3>

                      <p>
                        Completa el formulario para agregar el primer
                        bloque.
                      </p>
                    </div>
                  )}

                  {preparedBlocks.length > 0 && (
                    <>
                      <div className="medical-agenda-page__block-list">
                        {preparedBlocks.map((block) => (
                          <article
                            key={block.id}
                            className="medical-agenda-page__block"
                          >
                            <div>
                              <p className="medical-agenda-page__block-date">
                                {formatAgendaDate(
                                  block.appointmentDate,
                                )}
                              </p>

                              <p className="medical-agenda-page__block-time">
                                {block.startTime} - {block.endTime}
                              </p>
                            </div>

                            <AppButton
                              type="button"
                              variant="ghost"
                              fullWidth={false}
                              leftIcon={<Trash2 size={16} />}
                              disabled={isSaving}
                              onClick={() =>
                                handleRemoveBlock(block.id)
                              }
                            >
                              Eliminar
                            </AppButton>
                          </article>
                        ))}
                      </div>

                      {saveError && (
                        <div
                          className="medical-agenda-page__message medical-agenda-page__message--error"
                          role="alert"
                        >
                          {saveError}
                        </div>
                      )}

                      <div className="medical-agenda-page__save-actions">
                        <AppButton
                          type="button"
                          fullWidth={false}
                          leftIcon={<Save size={18} />}
                          isLoading={isSaving}
                          disabled={isSaving}
                          onClick={() => void handleSaveBlocks()}
                        >
                          Guardar bloques
                        </AppButton>
                      </div>
                    </>
                  )}

                  {successMessage && (
                    <div
                      className="medical-agenda-page__message medical-agenda-page__message--success"
                      role="status"
                    >
                      {successMessage}
                    </div>
                  )}
                </section>
              </div>

              <section
                className="medical-agenda-page__card medical-agenda-page__existing-agenda"
                aria-labelledby="existing-agenda-title"
                aria-live="polite"
                aria-busy={isLoadingExistingBlocks}
              >
                <div className="medical-agenda-page__section-heading">
                  <div>
                    <h2 id="existing-agenda-title">
                      Agenda existente
                    </h2>

                    <p>
                      Consulta los bloques registrados para el médico
                      y la fecha seleccionados.
                    </p>
                  </div>

                  {existingBlocks.length > 0 && (
                    <span className="medical-agenda-page__count">
                      {existingBlocks.length}
                    </span>
                  )}
                </div>

                {!selectedDoctorId || !appointmentDate ? (
                  <div className="medical-agenda-page__state">
                    <CalendarDays size={32} aria-hidden="true" />

                    <h3>Selecciona médico y fecha</h3>

                    <p>
                      Los bloques ya registrados aparecerán en esta
                      sección.
                    </p>
                  </div>
                ) : null}

                {selectedDoctorId
                  && appointmentDate
                  && isLoadingExistingBlocks && (
                    <div
                      className="medical-agenda-page__state"
                      role="status"
                    >
                      <div
                        className="medical-agenda-page__spinner"
                        aria-hidden="true"
                      />

                      <h3>Consultando agenda</h3>

                      <p>
                        Espera mientras consultamos los bloques
                        registrados.
                      </p>
                    </div>
                  )}

                {selectedDoctorId
                  && appointmentDate
                  && !isLoadingExistingBlocks
                  && existingBlocksError && (
                    <div
                      className="medical-agenda-page__state medical-agenda-page__state--error"
                      role="alert"
                    >
                      <CalendarDays
                        size={32}
                        aria-hidden="true"
                      />

                      <h3>No pudimos consultar la agenda</h3>

                      <p>{existingBlocksError}</p>
                    </div>
                  )}

                {selectedDoctorId
                  && appointmentDate
                  && !isLoadingExistingBlocks
                  && !existingBlocksError
                  && existingBlocks.length === 0 && (
                    <div className="medical-agenda-page__state">
                      <CalendarDays
                        size={32}
                        aria-hidden="true"
                      />

                      <h3>No hay bloques registrados</h3>

                      <p>
                        No existen bloques para el médico y la fecha
                        seleccionados.
                      </p>
                    </div>
                  )}

                {!isLoadingExistingBlocks
                  && !existingBlocksError
                  && existingBlocks.length > 0 && (
                    <div className="medical-agenda-page__existing-list">
                      {existingBlocks.map((block) => (
                        <article
                          key={block.id}
                          className="medical-agenda-page__existing-block"
                        >
                          <div>
                            <p className="medical-agenda-page__block-date">
                              {formatAgendaDate(
                                block.appointmentDate,
                              )}
                            </p>

                            <p className="medical-agenda-page__block-time">
                              {formatAgendaTime(block.startTime)}
                              {" - "}
                              {formatAgendaTime(block.endTime)}
                            </p>
                          </div>

                          <span
                            className={[
                              "medical-agenda-page__availability",
                              block.available
                                ? "medical-agenda-page__availability--available"
                                : "medical-agenda-page__availability--unavailable",
                            ].join(" ")}
                          >
                            {block.available
                              ? "Disponible"
                              : "No disponible"}
                          </span>
                        </article>
                      ))}
                    </div>
                  )}
              </section>
            </>
          )}
      </section>
    </main>
  );
}

export default MedicalAgendaPage;