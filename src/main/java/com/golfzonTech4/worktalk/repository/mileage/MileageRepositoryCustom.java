package com.golfzonTech4.worktalk.repository.mileage;

import com.golfzonTech4.worktalk.domain.Mileage;

import java.util.Optional;

public interface MileageRepositoryCustom {

    public Integer getTotalSave(Long memberId);

    public Integer getTotalUse(Long memberId);

    public Optional<Mileage> findByReservation(Long reserveId);
}
