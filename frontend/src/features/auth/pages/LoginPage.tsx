import {
  useState,
} from "react";
import type {
  ChangeEvent,
  FormEvent,
} from "react";
import {
  useNavigate,
} from "react-router-dom";
import {
  CalendarDays,
  LockKeyhole,
  Mail,
  ShieldCheck,
  Users,
} from "lucide-react";

import AppButton from "../../../components/AppButton";
import AppCard from "../../../components/AppCard";
import AppInput from "../../../components/AppInput";
import {
  AuthenticationError,
  login,
} from "../services/authService";
import {
  getDefaultRouteForRole,
  saveSession,
} from "../services/sessionService";

import "./LoginPage.css";

function LoginPage() {
  const navigate = useNavigate();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [submitError, setSubmitError] =
    useState<string | null>(null);

  const isFormValid =
    username.trim().length > 0 &&
    password.trim().length > 0;

  async function handleSubmit(
    event: FormEvent<HTMLFormElement>,
  ): Promise<void> {
    event.preventDefault();

    if (!isFormValid || isSubmitting) {
      return;
    }

    setSubmitError(null);
    setIsSubmitting(true);

    try {
      const response = await login({
        identifier: username.trim(),
        password,
      });

      saveSession(response);

      navigate(
        getDefaultRouteForRole(response.user.role),
        {
          replace: true,
        },
      );
    } catch (error: unknown) {
      if (error instanceof AuthenticationError) {
        setSubmitError(error.message);
      } else {
        setSubmitError(
          "Ocurrió un error inesperado. Inténtalo nuevamente.",
        );
      }
    } finally {
      setIsSubmitting(false);
    }
  }

  function handleUsernameChange(
    event: ChangeEvent<HTMLInputElement>,
  ): void {
    setUsername(event.target.value);

    if (submitError) {
      setSubmitError(null);
    }
  }

  function handlePasswordChange(
    event: ChangeEvent<HTMLInputElement>,
  ): void {
    setPassword(event.target.value);

    if (submitError) {
      setSubmitError(null);
    }
  }

  return (
    <main className="login-page">
      <section className="login-shell">
        <section className="login-hero">
          <div className="login-hero__overlay" />

          <div className="login-brand">
            <img
              src="/branding/agendoc-logo.png"
              alt="AgenDoc"
              className="login-brand__logo"
            />
          </div>

          <div className="login-hero__content">
            <h1>
              La plataforma que conecta pacientes, médicos y consultorios.
            </h1>

            <p>
              La plataforma diseñada para gestionar citas médicas de forma simple, rápida y segura.
            </p>

            <div className="login-benefits">
              <div>
                <span>
                  <CalendarDays size={24} strokeWidth={2.2} />
                </span>

                <strong>Agenda inteligente</strong>
                <p>Gestiona tu tiempo de manera eficiente.</p>
              </div>

              <div>
                <span>
                  <Users size={24} strokeWidth={2.2} />
                </span>

                <strong>Atención centrada en el paciente</strong>
                <p>Mejora la experiencia en cada cita.</p>
              </div>

              <div>
                <span>
                  <ShieldCheck size={24} strokeWidth={2.2} />
                </span>

                <strong>Seguridad y privacidad</strong>
                <p>Protegemos tu información siempre.</p>
              </div>
            </div>
          </div>

          <p className="login-security">
            Cumplimos con estándares de seguridad y privacidad de datos.
          </p>
        </section>

        <section className="login-panel">
          <AppCard className="login-card" elevation="high">
            <div className="login-card__header">
              <h2>Iniciar sesión</h2>
              <p>Bienvenido nuevamente</p>
              <span>Ingresa tus credenciales para continuar</span>
            </div>

            <form className="login-form" onSubmit={handleSubmit}>
              <AppInput
                id="username"
                name="username"
                label="Usuario o correo electrónico"
                type="text"
                placeholder="usuario o ejemplo@agendoc.com"
                value={username}
                onChange={handleUsernameChange}
                autoComplete="username"
                leftIcon={<Mail size={18} />}
                disabled={isSubmitting}
              />

              <AppInput
                id="password"
                name="password"
                label="Contraseña"
                type="password"
                placeholder="••••••••••••"
                value={password}
                onChange={handlePasswordChange}
                autoComplete="current-password"
                leftIcon={<LockKeyhole size={18} />}
                disabled={isSubmitting}
              />

              <div className="login-options login-options--end">
                <button
                  type="button"
                  disabled={isSubmitting}
                >
                  ¿Olvidaste tu contraseña?
                </button>
              </div>

              {submitError && (
                <div
                  className="login-error"
                  role="alert"
                  aria-live="assertive"
                >
                  <span
                    className="login-error__icon"
                    aria-hidden="true"
                  >
                    !
                  </span>

                  <p>{submitError}</p>
                </div>
              )}

              <AppButton
                type="submit"
                size="lg"
                disabled={!isFormValid || isSubmitting}
                isLoading={isSubmitting}
              >
                Iniciar sesión
              </AppButton>
            </form>

            <p className="login-footer">
              ¿No tienes una cuenta?{" "}
              <button
                type="button"
                disabled={isSubmitting}
              >
                Contacta al administrador
              </button>
            </p>
          </AppCard>
        </section>
      </section>
    </main>
  );
}

export default LoginPage;
