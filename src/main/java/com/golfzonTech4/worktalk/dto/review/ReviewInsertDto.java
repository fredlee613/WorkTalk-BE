package com.golfzonTech4.worktalk.dto.review;

import com.golfzonTech4.worktalk.domain.Reservation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class ReviewInsertDto {
    private Long reviewId;
    private Long reserveId;
    private Long memberId;
    @NotEmpty(message = "후기를 입력해주세요")
    private String content;
    private Double grade;
    private String review_img;
}
