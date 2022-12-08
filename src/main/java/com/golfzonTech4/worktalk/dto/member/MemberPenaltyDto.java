package com.golfzonTech4.worktalk.dto.member;

import com.golfzonTech4.worktalk.domain.MemberType;
import com.golfzonTech4.worktalk.domain.PenaltyType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MemberPenaltyDto {

    private Long id;
    private String email;
    private String name;
    private String tel;
    private MemberType memberType;
    private int activated;

    private Long penaltyId;
    private String penaltyReason;
    private PenaltyType penaltyType;
    private LocalDateTime penaltyDate;

    @QueryProjection
    public MemberPenaltyDto(Long id, String email, String name, String tel, MemberType memberType, int activated, Long penaltyId, String penaltyReason, PenaltyType penaltyType, LocalDateTime penaltyDate) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.tel = tel;
        this.memberType = memberType;
        this.activated = activated;
        this.penaltyId = penaltyId;
        this.penaltyReason = penaltyReason;
        this.penaltyType = penaltyType;
        this.penaltyDate = penaltyDate;
    }
}
