package com.golfzonTech4.worktalk.repository.customercenter;

import com.golfzonTech4.worktalk.dto.customercenter.CustomerCenterDetailDto;
import com.golfzonTech4.worktalk.dto.customercenter.CustomerCenterSearchDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

public interface CustomerCenterRepositoryCustom {

    PageImpl<CustomerCenterDetailDto> customerManagePage(PageRequest pageRequest, CustomerCenterSearchDto dto); //마스터용 전체 1대1문의 리스트

    PageImpl<CustomerCenterDetailDto> findccDtoListByMember(String name, PageRequest pageRequest, CustomerCenterSearchDto dto); //접속자의 1대1문의 내역 리스트
}
