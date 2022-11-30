package com.golfzonTech4.worktalk.dto.customercomment;

import com.golfzonTech4.worktalk.domain.CustomerCenter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerCommentInsertDto {

    private Long ccId;

    private String content;
}
