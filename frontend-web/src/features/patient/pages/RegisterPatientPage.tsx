import {
  useRef,
  useState,
} from "react";
import type { FormEvent } from "react";

import {
  ArrowLeft,
  CalendarDays,
  FileText,
  Mail,
  MapPin,
  Phone,
  UserPlus,
  UserRound,
} from "lucide-react";
import { useNavigate } from "react-router-dom";

import AppButton from "../../../components/AppButton";
import AppInput from "../../../components/AppInput";
import AppSelect from "../../../components/AppSelect";
import {
  createPatient,
  PatientServiceError,
} from "../services/patientService";
import type {
  CreatePatientRequest,
} from "../types/patient.types";
import {
  validatePatientForm,
} from "../validation/patientFormValidator";
import type {
  PatientFormErrors,
  PatientFormValues,
} from "../validation/patientFormValidator";

import "./RegisterPatientPage.css";

const INITIAL_FORM_VALUES: PatientFormValues = {
  firstName: "",
  lastName: "",
  documentType: "",
  documentNumber: "",
  birthDate: "",
  phone: "",
  email: "",
  address: "",
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

function RegisterPatientPage() {
  const navigate = useNavigate();

  const [formValues, setFormValues] =
    useState<PatientFormValues>(INITIAL_FORM_VALUES);

  const [formErrors, setFormErrors] =
    useState<PatientFormErrors>({});

  const [isSubmitting, setIsSubmitting] = useState(false);
  const [submitError, setSubmitError] = useState("");
  const [successMessage, setSuccessMessage] = useState("");

  const firstNameInputRef = useRef<HTMLInputElement>(null);

  function updateField(
    field: keyof PatientFormValues,
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

    const errors = validatePatientForm(formValues);

    if (Object.keys(errors).length > 0) {
      setFormErrors(errors);

      const firstInvalidField = Object.keys(
        errors,
      )[0] as keyof PatientFormValues;

      document
        .getElementById(firstInvalidField)
        ?.focus();

      return;
    }

    setFormErrors({});
    setIsSubmitting(true);

    const normalizedEmail = formValues.email.trim();
    const normalizedAddress = formValues.address.trim();

    const request: CreatePatientRequest = {
      firstName: formValues.firstName.trim(),
      lastName: formValues.lastName.trim(),
      documentType: formValues.documentType,
      documentNumber: formValues.documentNumber.trim(),
      birthDate: formValues.birthDate,
      phone: formValues.phone.trim(),
      email: normalizedEmail || null,
      address: normalizedAddress || null,
    };

    try {
      const patient = await createPatient(request);

      setFormValues(INITIAL_FORM_VALUES);

      setSuccessMessage(
        `Se registró correctamente a ${patient.firstName} ${patient.lastName}.`,
      );

      window.setTimeout(() => {
        setSuccessMessage("");
      }, 4000);

      window.scrollTo({
        top: 0,
        behavior: "smooth",
      });

      window.setTimeout(() => {
        firstNameInputRef.current?.focus();
      }, 250);
    } catch (error) {
      if (error instanceof PatientServiceError) {
        setSubmitError(error.message);
      } else {
        setSubmitError(
          "No fue posible registrar al paciente. Inténtalo nuevamente.",
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
    <main className="register-patient-page">
      <header className="register-patient-page__header">
        <img
          src="/branding/agendoc-logo.png"
          alt="AgenDoc"
          className="register-patient-page__logo"
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

      <section className="register-patient-page__content">
        <div className="register-patient-page__heading">
          <div
            className="register-patient-page__icon"
            aria-hidden="true"
          >
            <UserPlus size={24} />
          </div>

          <div>
            <h1>Registrar paciente</h1>

            <p>
              Ingresa los datos personales y de contacto del paciente.
            </p>
          </div>
        </div>

        <form
          className="register-patient-page__card"
          onSubmit={handleSubmit}
          noValidate
        >
          {successMessage && (
            <div
              className="register-patient-page__alert register-patient-page__alert--success"
              role="status"
              aria-live="polite"
            >
              <p>{successMessage}</p>
            </div>
          )}

          {submitError && (
            <div
              className="register-patient-page__alert register-patient-page__alert--error"
              role="alert"
            >
              <p>{submitError}</p>
            </div>
          )}

          <section className="register-patient-page__form-section">
            <div className="register-patient-page__section-heading">
              <div
                className="register-patient-page__section-icon"
                aria-hidden="true"
              >
                <UserRound size={20} />
              </div>

              <div>
                <h2>Información personal</h2>

                <p>
                  Registra los datos de identificación del paciente.
                </p>
              </div>
            </div>

            <div className="register-patient-page__grid">
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
                placeholder="Ej. María Fernanda"
                autoComplete="given-name"
                maxLength={100}
                required
                error={formErrors.firstName}
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
                placeholder="Ej. López Ramírez"
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

              <AppInput
                id="birthDate"
                name="birthDate"
                label="Fecha de nacimiento"
                type="date"
                value={formValues.birthDate}
                onChange={(event) =>
                  updateField("birthDate", event.target.value)
                }
                leftIcon={<CalendarDays size={18} />}
                required
                error={formErrors.birthDate}
              />
            </div>
          </section>

          <section className="register-patient-page__form-section">
            <div className="register-patient-page__section-heading">
              <div
                className="register-patient-page__section-icon"
                aria-hidden="true"
              >
                <Phone size={20} />
              </div>

              <div>
                <h2>Información de contacto</h2>

                <p>
                  Registra los medios de contacto y ubicación del
                  paciente.
                </p>
              </div>
            </div>

            <div className="register-patient-page__grid">
              <AppInput
                id="phone"
                name="phone"
                label="Teléfono"
                type="tel"
                value={formValues.phone}
                onChange={(event) =>
                  updateField("phone", event.target.value)
                }
                placeholder="Ej. 999 999 999"
                autoComplete="tel"
                maxLength={30}
                leftIcon={<Phone size={18} />}
                required
                error={formErrors.phone}
              />

              <AppInput
                id="email"
                name="email"
                label="Correo electrónico"
                type="email"
                value={formValues.email}
                onChange={(event) =>
                  updateField("email", event.target.value)
                }
                placeholder="paciente@correo.com"
                autoComplete="email"
                maxLength={150}
                leftIcon={<Mail size={18} />}
                helperText="Opcional."
                error={formErrors.email}
              />

              <AppInput
                id="address"
                name="address"
                label="Dirección"
                type="text"
                value={formValues.address}
                onChange={(event) =>
                  updateField("address", event.target.value)
                }
                placeholder="Ej. Av. Principal 123"
                autoComplete="street-address"
                maxLength={250}
                leftIcon={<MapPin size={18} />}
                helperText="Opcional."
                error={formErrors.address}
                className="register-patient-page__field--full"
              />
            </div>
          </section>

          <div className="register-patient-page__actions">
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
              leftIcon={<UserPlus size={18} />}
              isLoading={isSubmitting}
              disabled={isSubmitting}
            >
              Registrar paciente
            </AppButton>
          </div>
        </form>
      </section>
    </main>
  );
}

export default RegisterPatientPage;