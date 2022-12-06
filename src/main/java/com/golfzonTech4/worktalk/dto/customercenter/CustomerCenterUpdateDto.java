package com.golfzonTech4.worktalk.dto.customercenter;

import com.golfzonTech4.worktalk.domain.CcType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class CustomerCenterUpdateDto {

    @NotEmpty(message = "내용을 입력해주세요")
    private String content;

}
