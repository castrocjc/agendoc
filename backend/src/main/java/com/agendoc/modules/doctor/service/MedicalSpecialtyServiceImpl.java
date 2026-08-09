package com.agendoc.modules.doctor.service;

import com.agendoc.common.entity.RecordStatus;
import com.agendoc.modules.doctor.dto.MedicalSpecialtyResponse;
import com.agendoc.modules.doctor.entity.MedicalSpecialtyEntity;
import com.agendoc.modules.doctor.repository.MedicalSpecialtyRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of medical specialty query use cases.
 */
@Service
@RequiredArgsConstructor
public class MedicalSpecialtyServiceImpl
        implements MedicalSpecialtyService {

    private final MedicalSpecialtyRepository medicalSpecialtyRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MedicalSpecialtyResponse> findActiveSpecialties() {

        return medicalSpecialtyRepository
                .findAllByRecordStatusOrderByNameAsc(RecordStatus.ACTIVE)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private MedicalSpecialtyResponse toResponse(
            MedicalSpecialtyEntity specialty
    ) {
        return new MedicalSpecialtyResponse(
                specialty.getId(),
                specialty.getCode(),
                specialty.getName(),
                specialty.getDescription()
        );
    }
}