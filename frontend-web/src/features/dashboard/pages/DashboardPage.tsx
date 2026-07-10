function DashboardPage() {
  return (
    <main
      style={{
        minHeight: "100vh",
        display: "grid",
        placeItems: "center",
        padding: "24px",
        background: "var(--color-neutral-50, #f8fafc)",
      }}
    >
      <section
        style={{
          width: "min(560px, 100%)",
          padding: "32px",
          border: "1px solid var(--color-neutral-200, #e2e8f0)",
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
    </main>
  );
}

export default DashboardPage;