import type { ButtonHTMLAttributes, ReactNode } from "react";
import "./AppButton.css";

type AppButtonVariant = "primary" | "secondary" | "outline" | "ghost" | "danger";
type AppButtonSize = "sm" | "md" | "lg";

interface AppButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  children: ReactNode;
  variant?: AppButtonVariant;
  size?: AppButtonSize;
  isLoading?: boolean;
  fullWidth?: boolean;
  leftIcon?: ReactNode;
  rightIcon?: ReactNode;
}

function AppButton({
  children,
  variant = "primary",
  size = "md",
  isLoading = false,
  disabled,
  fullWidth = true,
  leftIcon,
  rightIcon,
  className = "",
  ...props
}: AppButtonProps) {
  return (
    <button
      className={[
        "app-button",
        `app-button--${variant}`,
        `app-button--${size}`,
        fullWidth ? "app-button--full" : "",
        isLoading ? "app-button--loading" : "",
        className,
      ]
        .filter(Boolean)
        .join(" ")}
      disabled={disabled || isLoading}
      aria-busy={isLoading}
      {...props}
    >
      {isLoading && <span className="app-button__spinner" aria-hidden="true" />}

      {!isLoading && leftIcon && (
        <span className="app-button__icon" aria-hidden="true">
          {leftIcon}
        </span>
      )}

      <span className="app-button__label">
        {isLoading ? "Procesando..." : children}
      </span>

      {!isLoading && rightIcon && (
        <span className="app-button__icon" aria-hidden="true">
          {rightIcon}
        </span>
      )}
    </button>
  );
}

export default AppButton;