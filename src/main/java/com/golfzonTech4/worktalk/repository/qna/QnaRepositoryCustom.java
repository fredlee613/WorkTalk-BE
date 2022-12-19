package com.golfzonTech4.worktalk.repository.qna;

import com.golfzonTech4.worktalk.domain.CcType;
import com.golfzonTech4.worktalk.domain.MemberType;
import com.golfzonTech4.worktalk.domain.QnaType;
import com.golfzonTech4.worktalk.dto.customercenter.CustomerCenterDetailDto;
import com.golfzonTech4.worktalk.dto.qna.QnaDetailDto;
import com.golfzonTech4.worktalk.dto.qna.QnaSearchDto;
import com.golfzonTech4.worktalk.dto.space.SpaceSearchDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface QnaRepositoryCustom {

    PageImpl<QnaDetailDto> findQnaDtoListbyHostSpace(PageRequest pageRequest, QnaSearchDto dto);

}
