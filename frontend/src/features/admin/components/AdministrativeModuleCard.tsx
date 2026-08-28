import type {
  LucideIcon,
} from "lucide-react";
import {
  ArrowRight,
} from "lucide-react";

import AppButton from "../../../components/AppButton";
import AppCard from "../../../components/AppCard";
import "./AdministrativeModuleCard.css";

type AdministrativeModuleStatus =
  | "available"
  | "coming-soon";

interface AdministrativeModuleCardProps {
  title: string;
  description: string;
  icon: LucideIcon;
  status: AdministrativeModuleStatus;
  actionLabel?: string;
  onAction?: () => void;
}

function AdministrativeModuleCard({
  title,
  description,
  icon: Icon,
  status,
  actionLabel,
  onAction,
}: AdministrativeModuleCardProps) {
  const isAvailable =
    status === "available" &&
    actionLabel &&
    onAction;

  return (
    <AppCard
      className="administrative-module-card"
      elevation="low"
    >
      <div className="administrative-module-card__icon">
        <Icon size={26} />
      </div>

      <div className="administrative-module-card__content">
        <div className="administrative-module-card__heading">
          <h2>{title}</h2>

          <span
            className={[
              "administrative-module-card__status",
              `administrative-module-card__status--${status}`,
            ].join(" ")}
          >
            {status === "available"
              ? "Disponible"
              : "Próximamente"}
          </span>
        </div>

        <p>{description}</p>

        {isAvailable && (
          <div className="administrative-module-card__action">
            <AppButton
              type="button"
              variant="outline"
              fullWidth={false}
              rightIcon={<ArrowRight size={18} />}
              onClick={onAction}
            >
              {actionLabel}
            </AppButton>
          </div>
        )}
      </div>
    </AppCard>
  );
}

export default AdministrativeModuleCard;
