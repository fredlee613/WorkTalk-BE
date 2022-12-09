package com.golfzonTech4.worktalk.repository.customercenter;

import com.golfzonTech4.worktalk.domain.CcType;
import com.golfzonTech4.worktalk.domain.MemberType;
import com.golfzonTech4.worktalk.dto.customercenter.CustomerCenterDetailDto;

import java.util.List;

public interface CustomerCenterRepositoryCustom {

    List<CustomerCenterDetailDto> customerManagePage(MemberType memberType, CcType ccType);

}
