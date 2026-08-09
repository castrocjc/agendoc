import type { SelectHTMLAttributes } from "react";
import "./AppSelect.css";

export interface AppSelectOption {
  value: string;
  label: string;
  disabled?: boolean;
}

interface AppSelectProps extends Omit<
  SelectHTMLAttributes<HTMLSelectElement>,
  "children"
> {
  label: string;
  options: AppSelectOption[];
  placeholder?: string;
  error?: string;
  helperText?: string;
}

function AppSelect({
  id,
  label,
  options,
  placeholder = "Selecciona una opción",
  error,
  helperText,
  className = "",
  ...props
}: AppSelectProps) {
  const helperId = helperText ? `${id}-helper` : undefined;
  const errorId = error ? `${id}-error` : undefined;

  return (
    <div className={["app-select", className].filter(Boolean).join(" ")}>
      <label className="app-select__label" htmlFor={id}>
        {label}
      </label>

      <div
        className={[
          "app-select__control",
          error ? "app-select__control--error" : "",
        ]
          .filter(Boolean)
          .join(" ")}
      >
        <select
          id={id}
          aria-invalid={Boolean(error)}
          aria-describedby={
            [helperId, errorId].filter(Boolean).join(" ") || undefined
          }
          {...props}
        >
          <option value="" disabled>
            {placeholder}
          </option>

          {options.map((option) => (
            <option
              key={option.value}
              value={option.value}
              disabled={option.disabled}
            >
              {option.label}
            </option>
          ))}
        </select>

        <span className="app-select__arrow" aria-hidden="true">
          <svg viewBox="0 0 20 20" width="18" height="18" fill="none">
            <path
              d="M6 8L10 12L14 8"
              stroke="currentColor"
              strokeWidth="1.8"
              strokeLinecap="round"
              strokeLinejoin="round"
            />
          </svg>
        </span>
      </div>

      {helperText && !error && (
        <p id={helperId} className="app-select__helper">
          {helperText}
        </p>
      )}

      {error && (
        <p id={errorId} className="app-select__error">
          {error}
        </p>
      )}
    </div>
  );
}

export default AppSelect;
