package com.golfzonTech4.worktalk.dto.qna;

import com.golfzonTech4.worktalk.domain.QnaType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class QnaInsertDto {

    private Long qnaId;

    private Long spaceId;

    private Long memberId;

    private QnaType type;

    @NotEmpty(message = "내용을 입력해주세요")
    private String content;

}
