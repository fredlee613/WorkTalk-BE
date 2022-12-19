package com.golfzonTech4.worktalk.repository.customercenter;

import com.golfzonTech4.worktalk.domain.CcType;
import com.golfzonTech4.worktalk.dto.customercenter.CustomerCenterDetailDto;
import com.golfzonTech4.worktalk.dto.customercenter.CustomerCenterSearchDto;

import java.util.List;

public interface CustomerCenterRepositoryCustom {

    List<CustomerCenterDetailDto> customerManagePage(CustomerCenterSearchDto dto); //마스터용 전체 1대1문의 리스트

    List<CustomerCenterDetailDto> findccDtoListByMember(String name, CcType ccType); //접속자의 1대1문의 내역 리스트
}
