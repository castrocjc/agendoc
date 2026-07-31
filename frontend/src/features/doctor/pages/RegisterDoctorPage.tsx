import {
  useEffect,
  useMemo,
  useRef,
  useState,
} from "react";
import type { FormEvent } from "react";

import {
  ArrowLeft,
  BadgeCheck,
  FileText,
  Mail,
  Phone,
  Stethoscope,
  UserRound,
} from "lucide-react";
import { useNavigate } from "react-router-dom";

import AppButton from "../../../components/AppButton";
import AppInput from "../../../components/AppInput";
import AppSelect from "../../../components/AppSelect";
import {
  createDoctor,
  DoctorServiceError,
  getMedicalSpecialties,
} from "../services/doctorService";
import type {
  CreateDoctorRequest,
  MedicalSpecialty,
} from "../types/doctor.types";

import {
  validateDoctorForm,
} from "../validation/doctorFormValidator";
import type {
  DoctorFormErrors,
  DoctorFormValues,
} from "../validation/doctorFormValidator";

import "./RegisterDoctorPage.css";

const INITIAL_FORM_VALUES: DoctorFormValues = {
  firstName: "",
  lastName: "",
  documentType: "",
  documentNumber: "",
  medicalLicenseNumber: "",
  specialtyId: "",
  phone: "",
  email: "",
};

const DOCUMENT_TYPE_OPTIONS = [
  {
    value: "DNI",
    label: "DNI",
  },
  {
    value: "CE",
    label: "Carné de extranjería",
  },
  {
    value: "PASSPORT",
    label: "Pasaporte",
  },
];

function RegisterDoctorPage() {
  const navigate = useNavigate();

  const [formValues, setFormValues] =
    useState<DoctorFormValues>(INITIAL_FORM_VALUES);

  const [formErrors, setFormErrors] =
    useState<DoctorFormErrors>({});

  const firstNameInputRef = useRef<HTMLInputElement>(null);

  const [isSubmitting, setIsSubmitting] = useState(false);
  const [submitError, setSubmitError] = useState("");
  const [successMessage, setSuccessMessage] = useState("");

  const [medicalSpecialties, setMedicalSpecialties] = useState<
    MedicalSpecialty[]
  >([]);

  const [isLoadingSpecialties, setIsLoadingSpecialties] = useState(true);

  const [specialtiesError, setSpecialtiesError] = useState("");

  useEffect(() => {
    let isMounted = true;

    async function loadMedicalSpecialties(): Promise<void> {
      setIsLoadingSpecialties(true);
      setSpecialtiesError("");

      try {
        const specialties = await getMedicalSpecialties();

        if (isMounted) {
          setMedicalSpecialties(specialties);
        }
      } catch (error) {
        if (!isMounted) {
          return;
        }

        if (error instanceof DoctorServiceError) {
          setSpecialtiesError(error.message);
        } else {
          setSpecialtiesError(
            "No fue posible cargar las especialidades médicas.",
          );
        }
      } finally {
        if (isMounted) {
          setIsLoadingSpecialties(false);
        }
      }
    }

    void loadMedicalSpecialties();

    return () => {
      isMounted = false;
    };
  }, []);

  const specialtyOptions = useMemo(
    () =>
      medicalSpecialties.map((specialty) => ({
        value: String(specialty.id),
        label: specialty.name,
      })),
    [medicalSpecialties],
  );

  function updateField(
    field: keyof DoctorFormValues,
    value: string,
  ): void {
    setFormValues((currentValues) => ({
      ...currentValues,
      [field]: value,
    }));

    setFormErrors((currentErrors) => {
      if (!currentErrors[field]) {
        return currentErrors;
      }

      const nextErrors = { ...currentErrors };
      delete nextErrors[field];

      return nextErrors;
    });

    if (submitError) {
      setSubmitError("");
    }

    if (successMessage) {
      setSuccessMessage("");
    }    
  }

  function handleBack(): void {
    navigate("/dashboard");
  }

  function handleCancel(): void {
    navigate("/dashboard");
  }

  async function handleSubmit(
    event: FormEvent<HTMLFormElement>,
  ): Promise<void> {
    event.preventDefault();

    setSubmitError("");
    setSuccessMessage("");

    const errors = validateDoctorForm(formValues);

    if (Object.keys(errors).length > 0) {
      setFormErrors(errors);

      const firstInvalidField = Object.keys(
        errors,
      )[0] as keyof DoctorFormValues;

      document
        .getElementById(firstInvalidField)
        ?.focus();

      return;
    }

    setFormErrors({});
    setIsSubmitting(true);

    const request: CreateDoctorRequest = {
      firstName: formValues.firstName.trim(),
      lastName: formValues.lastName.trim(),
      documentType: formValues.documentType,
      documentNumber: formValues.documentNumber.trim(),
      medicalLicenseNumber:
        formValues.medicalLicenseNumber.trim(),
      specialtyId: Number(formValues.specialtyId),
      phone: formValues.phone.trim(),
      email: formValues.email.trim(),
    };

    try {
      const doctor = await createDoctor(request);

      setFormValues(INITIAL_FORM_VALUES);

      setSuccessMessage(
        `${doctor.firstName} ${doctor.lastName} fue registrado correctamente.`,
      );

      window.scrollTo({
        top: 0,
        behavior: "smooth",
      });

      window.setTimeout(() => {
        firstNameInputRef.current?.focus();
      }, 250);
    } catch (error) {
      if (error instanceof DoctorServiceError) {
        setSubmitError(error.message);
      } else {
        setSubmitError(
          "No fue posible registrar al médico. Inténtalo nuevamente.",
        );
      }

      window.scrollTo({
        top: 0,
        behavior: "smooth",
      });
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <main className="register-doctor-page">
      <header className="register-doctor-page__header">
        <img
          src="/branding/agendoc-logo.png"
          alt="AgenDoc"
          className="register-doctor-page__logo"
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

      <section className="register-doctor-page__content">
        <div className="register-doctor-page__heading">
          <div className="register-doctor-page__icon" aria-hidden="true">
            <Stethoscope size={24} />
          </div>

          <div>
            <h1>Registrar médico</h1>

            <p>
              Ingresa los datos personales, profesionales y de contacto del
              médico.
            </p>
          </div>
        </div>

        <form
          className="register-doctor-page__card"
          onSubmit={handleSubmit}
          noValidate
        >

          {successMessage && (
            <div
              className="register-doctor-page__alert register-doctor-page__alert--success"
              role="status"
              aria-live="polite"
            >
              <p>{successMessage}</p>
            </div>
          )}

          {submitError && (
            <div
              className="register-doctor-page__alert register-doctor-page__alert--error"
              role="alert"
            >
              <p>{submitError}</p>
            </div>
          )}

          {specialtiesError && (
            <div
              className="register-doctor-page__alert register-doctor-page__alert--error"
              role="alert"
            >
              <p>{specialtiesError}</p>

              <AppButton
                type="button"
                variant="outline"
                size="sm"
                fullWidth={false}
                onClick={() => window.location.reload()}
              >
                Reintentar
              </AppButton>
            </div>
          )}

          <section className="register-doctor-page__form-section">
            <div className="register-doctor-page__section-heading">
              <div
                className="register-doctor-page__section-icon"
                aria-hidden="true"
              >
                <UserRound size={20} />
              </div>

              <div>
                <h2>Información personal</h2>

                <p>Registra los datos de identificación del médico.</p>
              </div>
            </div>

            <div className="register-doctor-page__grid">
              <AppInput
                ref={firstNameInputRef}
                id="firstName"
                name="firstName"
                label="Nombres"
                type="text"
                value={formValues.firstName}
                onChange={(event) =>
                  updateField("firstName", event.target.value)
                }
                placeholder="Ej. Juan Carlos"
                autoComplete="given-name"
                maxLength={100}
                error={formErrors.firstName}
                required
              />

              <AppInput
                id="lastName"
                name="lastName"
                label="Apellidos"
                type="text"
                value={formValues.lastName}
                onChange={(event) =>
                  updateField("lastName", event.target.value)
                }
                placeholder="Ej. Pérez Gómez"
                autoComplete="family-name"
                maxLength={100}
                required
                error={formErrors.lastName}
              />

              <AppSelect
                id="documentType"
                name="documentType"
                label="Tipo de documento"
                value={formValues.documentType}
                onChange={(event) =>
                  updateField("documentType", event.target.value)
                }
                options={DOCUMENT_TYPE_OPTIONS}
                placeholder="Selecciona un tipo"
                required
                error={formErrors.documentType}
              />

              <AppInput
                id="documentNumber"
                name="documentNumber"
                label="Número de documento"
                type="text"
                value={formValues.documentNumber}
                onChange={(event) =>
                  updateField("documentNumber", event.target.value)
                }
                placeholder="Ingresa el número"
                maxLength={50}
                leftIcon={<FileText size={18} />}
                required
                error={formErrors.documentNumber}
              />
            </div>
          </section>

          <section className="register-doctor-page__form-section">
            <div className="register-doctor-page__section-heading">
              <div
                className="register-doctor-page__section-icon"
                aria-hidden="true"
              >
                <BadgeCheck size={20} />
              </div>

              <div>
                <h2>Información profesional</h2>

                <p>Selecciona la especialidad y registra la colegiatura.</p>
              </div>
            </div>

            <div className="register-doctor-page__grid">
              <AppSelect
                id="specialtyId"
                name="specialtyId"
                label="Especialidad médica"
                value={formValues.specialtyId}
                onChange={(event) =>
                  updateField("specialtyId", event.target.value)
                }
                options={specialtyOptions}
                placeholder={
                  isLoadingSpecialties
                    ? "Cargando especialidades..."
                    : "Selecciona una especialidad"
                }
                disabled={
                  isLoadingSpecialties ||
                  Boolean(specialtiesError) ||
                  specialtyOptions.length === 0
                }
                helperText={
                  !isLoadingSpecialties &&
                  !specialtiesError &&
                  specialtyOptions.length === 0
                    ? "No existen especialidades disponibles."
                    : undefined
                }
                required
                error={formErrors.specialtyId}
              />

              <AppInput
                id="medicalLicenseNumber"
                name="medicalLicenseNumber"
                label="Número de colegiatura"
                type="text"
                value={formValues.medicalLicenseNumber}
                onChange={(event) =>
                  updateField("medicalLicenseNumber", event.target.value)
                }
                placeholder="Ej. CMP 123456"
                maxLength={50}
                required
                error={formErrors.medicalLicenseNumber}
              />
            </div>
          </section>

          <section className="register-doctor-page__form-section">
            <div className="register-doctor-page__section-heading">
              <div
                className="register-doctor-page__section-icon"
                aria-hidden="true"
              >
                <Mail size={20} />
              </div>

              <div>
                <h2>Información de contacto</h2>

                <p>Registra los medios de contacto principales.</p>
              </div>
            </div>

            <div className="register-doctor-page__grid">
              <AppInput
                id="email"
                name="email"
                label="Correo electrónico"
                type="email"
                value={formValues.email}
                onChange={(event) => updateField("email", event.target.value)}
                placeholder="medico@correo.com"
                autoComplete="email"
                maxLength={150}
                leftIcon={<Mail size={18} />}
                required
                error={formErrors.email}
              />

              <AppInput
                id="phone"
                name="phone"
                label="Teléfono"
                type="tel"
                value={formValues.phone}
                onChange={(event) => updateField("phone", event.target.value)}
                placeholder="Ej. 999 999 999"
                autoComplete="tel"
                maxLength={30}
                leftIcon={<Phone size={18} />}
                error={formErrors.phone}
              />
            </div>
          </section>

          <div className="register-doctor-page__actions">
            <AppButton
              type="button"
              variant="outline"
              fullWidth={false}
              onClick={handleCancel}
              disabled={isSubmitting}
            >
              Cancelar
            </AppButton>

            <AppButton
              type="submit"
              fullWidth={false}
              leftIcon={<Stethoscope size={18} />}
              isLoading={isSubmitting}
              disabled={
                isSubmitting ||
                isLoadingSpecialties ||
                Boolean(specialtiesError)
              }
            >
              Registrar médico
            </AppButton>
          </div>
        </form>
      </section>
    </main>
  );
}

export default RegisterDoctorPage;
