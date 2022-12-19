package com.golfzonTech4.worktalk.dto.qna;

import com.golfzonTech4.worktalk.domain.QnaType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class QnaSearchDto {

    private QnaType searchQnaType;

    private String searchHost;

    private String searchSpaceName;

    private int pageNum;
}
