package com.golfzonTech4.worktalk.dto.customercenter;

import com.golfzonTech4.worktalk.domain.CcType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CustomerCenterDetailDto {

    private Long ccId;

    private Long memberId;

    private String title;

    private String content;

    private CcType type;

    private LocalDateTime lastModifiedDate;

    private String ccContent;

    private LocalDateTime ccLastModifiedDate;

    @QueryProjection
    public CustomerCenterDetailDto(Long ccId, Long memberId, String title, String content, CcType type, LocalDateTime lastModifiedDate, String ccContent, LocalDateTime ccLastModifiedDate) {
        this.ccId = ccId;
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.type = type;
        this.lastModifiedDate = lastModifiedDate;
        this.ccContent = ccContent;
        this.ccLastModifiedDate = ccLastModifiedDate;
    }
}
