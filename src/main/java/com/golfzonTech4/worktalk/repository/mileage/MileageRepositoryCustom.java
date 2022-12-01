package com.golfzonTech4.worktalk.repository.mileage;

public interface MileageRepositoryCustom {

    public int getTotalSave(Long memberId);

    public int getTotalUse(Long memberId);
}
