package com.golfzonTech4.worktalk.dto.qnacomment;

import com.golfzonTech4.worktalk.domain.CcType;
import com.golfzonTech4.worktalk.domain.Member;
import com.golfzonTech4.worktalk.domain.Qna;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QnaCommentInsertDto {

    private Long qnaId;

    private Long memberId;

    private String qnacomment;

}
