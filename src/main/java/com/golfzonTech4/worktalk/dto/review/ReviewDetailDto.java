package com.golfzonTech4.worktalk.dto.review;

import com.golfzonTech4.worktalk.domain.QnaType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.eclipse.jdt.internal.compiler.ast.DoubleLiteral;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ReviewDetailDto {

    private Long reviewId;

    private Long reservationId;

    private Long memberId;

    private String content;

    private LocalDateTime lastModifiedDate;

    private Double grade;

    private String spaceName;

    private String roomName;

    public ReviewDetailDto(Long reviewId, Long reservationId, Long memberId, String content, LocalDateTime lastModifiedDate, Double grade, String spaceName, String roomName) {
        this.reviewId = reviewId;
        this.reservationId = reservationId;
        this.memberId = memberId;
        this.content = content;
        this.lastModifiedDate = lastModifiedDate;
        this.grade = grade;
        this.spaceName = spaceName;
        this.roomName = roomName;
    }

    public ReviewDetailDto(Long reviewId, Long reservationId, Long memberId, String content, LocalDateTime lastModifiedDate, Double grade) {
        this.reviewId = reviewId;
        this.reservationId = reservationId;
        this.memberId = memberId;
        this.content = content;
        this.lastModifiedDate = lastModifiedDate;
        this.grade = grade;
    }
}
