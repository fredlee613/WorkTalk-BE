package com.golfzonTech4.worktalk.repository.mileage;

import com.golfzonTech4.worktalk.domain.Mileage;

import java.util.Optional;

public interface MileageRepositoryCustom {

    Integer getTotalSave(Long memberId);

    Integer getTotalUse(Long memberId);

    Integer getTotalToBeSaved(Long memberId);

    Optional<Mileage> findByReservation(Long reserveId);
}
