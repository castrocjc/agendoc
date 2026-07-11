package com.agendoc.modules.doctor.service;

import com.agendoc.modules.doctor.dto.MedicalSpecialtyResponse;
import java.util.List;

/**
 * Defines medical specialty query use cases.
 */
public interface MedicalSpecialtyService {

    List<MedicalSpecialtyResponse> findActiveSpecialties();
}