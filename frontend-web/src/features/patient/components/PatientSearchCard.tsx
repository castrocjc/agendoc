import { FileText, Mail, Phone, UserRound } from "lucide-react";

import type { PatientResponse } from "../types/patient.types";

import "./PatientSearchCard.css";

interface PatientSearchCardProps {
  patient: PatientResponse;
}

function PatientSearchCard({ patient }: PatientSearchCardProps) {
  const fullName = `${patient.firstName} ${patient.lastName}`;

  return (
    <article className="patient-search-card">
      <div className="patient-search-card__avatar" aria-hidden="true">
        <UserRound size={24} />
      </div>

      <div className="patient-search-card__content">
        <header className="patient-search-card__header">
          <div>
            <h3>{fullName}</h3>

            <p className="patient-search-card__document">
              {patient.documentType} · {patient.documentNumber}
            </p>
          </div>
        </header>

        <div className="patient-search-card__details">
          <div className="patient-search-card__detail">
            <Phone size={16} aria-hidden="true" />

            <span>{patient.phone}</span>
          </div>

          {patient.email && (
            <div className="patient-search-card__detail">
              <Mail size={16} aria-hidden="true" />

              <span>{patient.email}</span>
            </div>
          )}

          <div className="patient-search-card__detail">
            <FileText size={16} aria-hidden="true" />

            <span>Nacimiento: {patient.birthDate}</span>
          </div>
        </div>
      </div>
    </article>
  );
}

export default PatientSearchCard;
