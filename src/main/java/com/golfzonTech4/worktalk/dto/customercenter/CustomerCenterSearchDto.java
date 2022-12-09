package com.golfzonTech4.worktalk.dto.customercenter;

import com.golfzonTech4.worktalk.domain.CcType;
import com.golfzonTech4.worktalk.domain.MemberType;
import lombok.Data;

@Data
public class CustomerCenterSearchDto {
    private MemberType searchMemberType;
    private CcType searchccType;
}
