package com.golfzonTech4.worktalk.repository.penalty;

import com.golfzonTech4.worktalk.dto.penalty.PenaltyDto;

import java.util.List;

public interface PenaltyRepositoryCustom {
    public List<PenaltyDto> findPenalties();
}
