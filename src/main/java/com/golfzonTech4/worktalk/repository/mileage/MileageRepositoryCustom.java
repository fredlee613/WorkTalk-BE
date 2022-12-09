package com.golfzonTech4.worktalk.repository.mileage;

public interface MileageRepositoryCustom {

    public Integer getTotalSave(Long memberId);

    public Integer getTotalUse(Long memberId);
}
