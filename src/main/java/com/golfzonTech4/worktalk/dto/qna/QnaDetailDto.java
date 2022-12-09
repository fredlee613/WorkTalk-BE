package com.golfzonTech4.worktalk.dto.qna;

import com.golfzonTech4.worktalk.domain.QnaType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class QnaDetailDto {
    private Long qnaId;

    private Long spaceId;

    private Long memberId;

    private QnaType type;

    private String content;

    private LocalDateTime lastModifiedDate;

    private String qnacomment;

    private LocalDateTime qclastModifiedDate;

    private String spaceName;

    public QnaDetailDto(Long qnaId, Long spaceId, Long memberId, QnaType type, String content, LocalDateTime lastModifiedDate,
                        String qnacomment, LocalDateTime qclastModifiedDate) {
        this.qnaId = qnaId;
        this.spaceId = spaceId;
        this.memberId = memberId;
        this.type = type;
        this.content = content;
        this.lastModifiedDate = lastModifiedDate;
        this.qnacomment = qnacomment;
        this.qclastModifiedDate = qclastModifiedDate;
    }

    public QnaDetailDto(Long qnaId, Long spaceId, Long memberId, QnaType type, String content, LocalDateTime lastModifiedDate,
                        String qnacomment, LocalDateTime qclastModifiedDate, String spaceName) {
        this.qnaId = qnaId;
        this.spaceId = spaceId;
        this.memberId = memberId;
        this.type = type;
        this.content = content;
        this.lastModifiedDate = lastModifiedDate;
        this.qnacomment = qnacomment;
        this.qclastModifiedDate = qclastModifiedDate;
        this.spaceName = spaceName;
    }
}
