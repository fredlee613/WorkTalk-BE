package com.golfzonTech4.worktalk.dto.review;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class ReviewUpdateDto {

    @NotEmpty(message = "후기를 입력해주세요")
    private String content;
    private Double grade;
    private String review_img;
}
