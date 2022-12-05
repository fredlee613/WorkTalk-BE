package com.golfzonTech4.worktalk.dto.penalty;

import com.golfzonTech4.worktalk.domain.PenaltyType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PenaltyDto {

    private Long penaltyId;
    private Long memberId;
    private String memberName;
    private String penaltyReason;
    private PenaltyType penaltyType;
    private LocalDateTime penaltyDate;

    @QueryProjection
    public PenaltyDto(Long penaltyId, Long memberId, String memberName, String penaltyReason, PenaltyType penaltyType, LocalDateTime penaltyDate) {
        this.penaltyId = penaltyId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.penaltyReason = penaltyReason;
        this.penaltyType = penaltyType;
        this.penaltyDate = penaltyDate;
    }

    public PenaltyDto(Long memberId, String penaltyReason, PenaltyType penaltyType) {
        this.memberId = memberId;
        this.penaltyReason = penaltyReason;
        this.penaltyType = penaltyType;
    }
}
