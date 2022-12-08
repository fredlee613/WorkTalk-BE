package com.golfzonTech4.worktalk.dto.penalty;

import com.golfzonTech4.worktalk.domain.MemberType;
import com.golfzonTech4.worktalk.domain.PenaltyType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PenaltySearchDto {

    private Long penaltyId;
    private String penaltyReason;
    private PenaltyType penaltyType;
    private LocalDateTime penaltyDate;

    private Long id;
    private String email;
    private String name;
    private String tel;
    private MemberType memberType;
    private int activated;

    @QueryProjection
    public PenaltySearchDto(Long penaltyId, String penaltyReason, PenaltyType penaltyType, LocalDateTime penaltyDate, Long id, String email, String name, String tel, MemberType memberType, int activated) {
        this.penaltyId = penaltyId;
        this.penaltyReason = penaltyReason;
        this.penaltyType = penaltyType;
        this.penaltyDate = penaltyDate;
        this.id = id;
        this.email = email;
        this.name = name;
        this.tel = tel;
        this.memberType = memberType;
        this.activated = activated;
    }
}
