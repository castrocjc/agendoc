import { useState } from "react";
import AppButton from "../../../components/AppButton";
import AppCard from "../../../components/AppCard";
import AppInput from "../../../components/AppInput";
import "./LoginPage.css";
import {
  CalendarDays,
  Users,
  ShieldCheck
} from "lucide-react";

function LoginPage() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const isFormValid = username.trim().length > 0 && password.trim().length > 0;

  function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
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
            <h1>La plataforma que conecta pacientes, médicos y consultorios.</h1>
            <p>La plataforma diseñada para gestionar citas médicas de forma simple, rápida y segura.</p>

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

          <p className="login-security">Cumplimos con estándares de seguridad y privacidad de datos.</p>
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
                label="Correo electrónico"
                type="text"
                placeholder="ejemplo@agendoc.com"
                value={username}
                onChange={(event) => setUsername(event.target.value)}
                autoComplete="username"
                leftIcon="✉"
              />

              <AppInput
                id="password"
                name="password"
                label="Contraseña"
                type="password"
                placeholder="••••••••••••"
                value={password}
                onChange={(event) => setPassword(event.target.value)}
                autoComplete="current-password"
                leftIcon="⌑"
                rightIcon="◉"
              />

              <div className="login-options">
                <label>
                  <input type="checkbox" />
                  Recordarme
                </label>

                <button type="button">¿Olvidaste tu contraseña?</button>
              </div>

              <AppButton type="submit" size="lg" disabled={!isFormValid}>
                Iniciar sesión
              </AppButton>

              <div className="login-divider">
                <span />
                <p>o continúa con</p>
                <span />
              </div>

              <AppButton type="button" variant="outline" size="lg">
                Continuar con Google
              </AppButton>
            </form>

            <p className="login-footer">
              ¿No tienes una cuenta? <button type="button">Contacta al administrador</button>
            </p>
          </AppCard>
        </section>
      </section>
    </main>
  );
}

export default LoginPage;