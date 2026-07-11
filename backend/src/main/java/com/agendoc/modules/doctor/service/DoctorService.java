package com.agendoc.modules.doctor.service;

import com.agendoc.modules.doctor.dto.CreateDoctorRequest;
import com.agendoc.modules.doctor.dto.DoctorResponse;

/**
 * Defines doctor management use cases.
 */
public interface DoctorService {

    DoctorResponse createDoctor(CreateDoctorRequest request);
}