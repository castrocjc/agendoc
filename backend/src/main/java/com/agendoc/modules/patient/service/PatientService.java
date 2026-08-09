package com.agendoc.modules.patient.service;

import com.agendoc.modules.patient.dto.CreatePatientRequest;
import com.agendoc.modules.patient.dto.PatientResponse;
import java.util.List;

/**
 * Defines patient management use cases.
 */
public interface PatientService {

    PatientResponse createPatient(CreatePatientRequest request);

    List<PatientResponse> searchPatients(String query);
}