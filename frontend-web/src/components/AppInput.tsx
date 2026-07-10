import type { InputHTMLAttributes, ReactNode } from "react";
import "./AppInput.css";

interface AppInputProps extends InputHTMLAttributes<HTMLInputElement> {
  label: string;
  error?: string;
  helperText?: string;
  leftIcon?: ReactNode;
  rightIcon?: ReactNode;
}

function AppInput({
  id,
  label,
  error,
  helperText,
  leftIcon,
  rightIcon,
  className = "",
  ...props
}: AppInputProps) {
  const helperId = helperText ? `${id}-helper` : undefined;
  const errorId = error ? `${id}-error` : undefined;

  return (
    <div className={["app-input", className].filter(Boolean).join(" ")}>
      <label className="app-input__label" htmlFor={id}>
        {label}
      </label>

      <div
        className={[
          "app-input__control",
          leftIcon ? "app-input__control--with-left-icon" : "",
          rightIcon ? "app-input__control--with-right-icon" : "",
          error ? "app-input__control--error" : "",
        ]
          .filter(Boolean)
          .join(" ")}
      >
        {leftIcon && (
          <span className="app-input__icon app-input__icon--left" aria-hidden="true">
            {leftIcon}
          </span>
        )}

        <input
          id={id}
          aria-invalid={Boolean(error)}
          aria-describedby={[helperId, errorId].filter(Boolean).join(" ") || undefined}
          {...props}
        />

        {rightIcon && (
          <span className="app-input__icon app-input__icon--right" aria-hidden="true">
            {rightIcon}
          </span>
        )}
      </div>

      {helperText && !error && (
        <p id={helperId} className="app-input__helper">
          {helperText}
        </p>
      )}

      {error && (
        <p id={errorId} className="app-input__error">
          {error}
        </p>
      )}
    </div>
  );
}

export default AppInput;