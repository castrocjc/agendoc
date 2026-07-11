import { useNavigate } from "react-router-dom";
import { LogOut } from "lucide-react";

import AppButton from "../../../components/AppButton";
import { clearSession } from "../../auth/services/sessionService";

function DashboardPage() {
  const navigate = useNavigate();

  function handleLogout(): void {
    const confirmed = window.confirm(
      "¿Deseas cerrar tu sesión en AgenDoc?",
    );

    if (!confirmed) {
      return;
    }

    clearSession();

    navigate("/login", {
      replace: true,
    });
  }

  return (
    <main
      style={{
        minHeight: "100vh",
        background: "var(--color-neutral-50, #f8fafc)",
      }}
    >
      <header
        style={{
          minHeight: "72px",
          display: "flex",
          alignItems: "center",
          justifyContent: "space-between",
          padding: "12px 24px",
          borderBottom:
            "1px solid var(--color-neutral-200, #e2e8f0)",
          background: "#ffffff",
        }}
      >
        <img
          src="/branding/agendoc-logo.png"
          alt="AgenDoc"
          style={{
            width: "140px",
            height: "auto",
            display: "block",
          }}
        />

        <AppButton
          type="button"
          onClick={handleLogout}
        >
          <LogOut
            size={18}
            aria-hidden="true"
          />
          <span>Cerrar sesión</span>
        </AppButton>
      </header>

      <section
        style={{
          minHeight: "calc(100vh - 72px)",
          display: "grid",
          placeItems: "center",
          padding: "24px",
        }}
      >
        <section
          style={{
            width: "min(560px, 100%)",
            padding: "32px",
            border:
              "1px solid var(--color-neutral-200, #e2e8f0)",
            borderRadius: "16px",
            background: "#ffffff",
            textAlign: "center",
          }}
        >
          <h1
            style={{
              margin: "0 0 12px",
              color: "var(--color-neutral-900, #0f172a)",
            }}
          >
            Dashboard de AgenDoc
          </h1>

          <p
            style={{
              margin: 0,
              color: "var(--color-neutral-600, #475569)",
            }}
          >
            Acceso realizado correctamente.
          </p>
        </section>
      </section>
    </main>
  );
}

export default DashboardPage;