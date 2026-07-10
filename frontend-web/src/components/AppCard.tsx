import type { HTMLAttributes, ReactNode } from "react";
import "./AppCard.css";

type AppCardElevation = "none" | "low" | "medium" | "high";

interface AppCardProps extends HTMLAttributes<HTMLElement> {
  children: ReactNode;
  elevation?: AppCardElevation;
}

function AppCard({
  children,
  className = "",
  elevation = "low",
  ...props
}: AppCardProps) {
  return (
    <section
      className={["app-card", `app-card--${elevation}`, className]
        .filter(Boolean)
        .join(" ")}
      {...props}
    >
      {children}
    </section>
  );
}

export default AppCard;