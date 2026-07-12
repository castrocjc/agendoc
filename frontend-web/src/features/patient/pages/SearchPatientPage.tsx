import {
  useRef,
  useState,
} from "react";
import type {
  FormEvent,
} from "react";

import {
  ArrowLeft,
  Search,
  Users,
} from "lucide-react";
import { useNavigate } from "react-router-dom";

import AppButton from "../../../components/AppButton";
import AppInput from "../../../components/AppInput";
import PatientSearchCard from "../components/PatientSearchCard";
import {
  PatientServiceError,
  searchPatients,
} from "../services/patientService";
import type {
  PatientResponse,
} from "../types/patient.types";

import "./SearchPatientPage.css";

const MINIMUM_SEARCH_LENGTH = 2;

function SearchPatientPage() {
  const navigate = useNavigate();

  const searchInputRef = useRef<HTMLInputElement>(null);

  const [searchQuery, setSearchQuery] = useState("");
  const [patients, setPatients] =
    useState<PatientResponse[]>([]);

  const [isSearching, setIsSearching] = useState(false);
  const [hasSearched, setHasSearched] = useState(false);
  const [searchError, setSearchError] = useState("");
  const [fieldError, setFieldError] = useState("");

  function handleBack(): void {
    navigate("/dashboard");
  }

  function handleQueryChange(value: string): void {
    setSearchQuery(value);

    if (fieldError) {
      setFieldError("");
    }

    if (searchError) {
      setSearchError("");
    }
  }

  async function handleSearch(
    event: FormEvent<HTMLFormElement>,
  ): Promise<void> {
    event.preventDefault();

    const normalizedQuery = searchQuery.trim();

    setSearchError("");
    setFieldError("");

    if (normalizedQuery.length < MINIMUM_SEARCH_LENGTH) {
      setFieldError(
        "Ingresa al menos 2 caracteres para buscar pacientes.",
      );

      searchInputRef.current?.focus();

      return;
    }

    setIsSearching(true);
    setHasSearched(true);

    try {
      const results = await searchPatients(normalizedQuery);

      setPatients(results);
    } catch (error) {
      setPatients([]);

      if (error instanceof PatientServiceError) {
        setSearchError(error.message);
      } else {
        setSearchError(
          "No fue posible buscar pacientes. Inténtalo nuevamente.",
        );
      }
    } finally {
      setIsSearching(false);
    }
  }

  return (
    <main className="search-patient-page">
      <header className="search-patient-page__header">
        <img
          src="/branding/agendoc-logo.png"
          alt="AgenDoc"
          className="search-patient-page__logo"
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

      <section className="search-patient-page__content">
        <div className="search-patient-page__heading">
          <div
            className="search-patient-page__icon"
            aria-hidden="true"
          >
            <Users size={24} />
          </div>

          <div>
            <h1>Buscar paciente</h1>

            <p>
              Busca pacientes registrados por nombre, apellido o
              número de documento.
            </p>
          </div>
        </div>

        <section
          className="search-patient-page__card"
          aria-labelledby="patient-search-title"
        >
          <div className="search-patient-page__section-heading">
            <div>
              <h2 id="patient-search-title">
                Criterio de búsqueda
              </h2>

              <p>
                Ingresa al menos 2 caracteres para localizar un
                paciente.
              </p>
            </div>
          </div>

          <form
            className="search-patient-page__form"
            onSubmit={handleSearch}
            noValidate
          >
            <AppInput
              ref={searchInputRef}
              id="patientSearch"
              name="patientSearch"
              label="Nombre o número de documento"
              type="search"
              value={searchQuery}
              onChange={(event) =>
                handleQueryChange(event.target.value)
              }
              placeholder="Ej. Quintana o 12345675"
              leftIcon={<Search size={18} />}
              maxLength={100}
              error={fieldError}
            />

            <AppButton
              type="submit"
              fullWidth={false}
              leftIcon={<Search size={18} />}
              isLoading={isSearching}
              disabled={isSearching}
            >
              Buscar paciente
            </AppButton>
          </form>
        </section>

        <section
          className="search-patient-page__results"
          aria-labelledby="patient-results-title"
          aria-live="polite"
          aria-busy={isSearching}
        >
          <div className="search-patient-page__results-heading">
            <div>
              <h2 id="patient-results-title">
                Resultados
              </h2>

              {hasSearched
                && !isSearching
                && !searchError
                && patients.length > 0 && (
                  <p>
                    {patients.length === 1
                      ? "Se encontró 1 paciente."
                      : `Se encontraron ${patients.length} pacientes.`}
                  </p>
                )}
            </div>
          </div>

          {!hasSearched && (
            <div className="search-patient-page__state">
              <Search
                size={32}
                aria-hidden="true"
              />

              <h3>Busca un paciente</h3>

              <p>
                Ingresa un nombre, apellido o número de documento para
                consultar coincidencias.
              </p>
            </div>
          )}

          {isSearching && (
            <div
              className="search-patient-page__state"
              role="status"
            >
              <div
                className="search-patient-page__spinner"
                aria-hidden="true"
              />

              <h3>Buscando pacientes</h3>

              <p>
                Espera un momento mientras consultamos la información.
              </p>
            </div>
          )}

          {!isSearching && searchError && (
            <div
              className="search-patient-page__state search-patient-page__state--error"
              role="alert"
            >
              <Users
                size={32}
                aria-hidden="true"
              />

              <h3>No pudimos realizar la búsqueda</h3>

              <p>{searchError}</p>
            </div>
          )}

          {!isSearching
            && hasSearched
            && !searchError
            && patients.length === 0 && (
              <div className="search-patient-page__state">
                <Users
                  size={32}
                  aria-hidden="true"
                />

                <h3>No encontramos pacientes</h3>

                <p>
                  Revisa el nombre o número de documento e intenta
                  nuevamente.
                </p>
              </div>
            )}

          {!isSearching
            && !searchError
            && patients.length > 0 && (
              <div className="search-patient-page__result-list">
                {patients.map((patient) => (
                  <PatientSearchCard
                    key={patient.id}
                    patient={patient}
                  />
                ))}
              </div>
            )}
        </section>
      </section>
    </main>
  );
}

export default SearchPatientPage;