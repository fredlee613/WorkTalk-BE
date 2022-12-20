package com.golfzonTech4.worktalk.repository.qna;

import com.golfzonTech4.worktalk.dto.qna.QnaDetailDto;
import com.golfzonTech4.worktalk.dto.qna.QnaSearchDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

public interface QnaRepositoryCustom {

    PageImpl<QnaDetailDto> findQnaDtoListbyHostSpace(PageRequest pageRequest, QnaSearchDto dto);

    PageImpl<QnaDetailDto> findQnaDtoListByMember(PageRequest pageRequest, QnaSearchDto dto);

    PageImpl<QnaDetailDto> findQnaDtoListBySpaceId(PageRequest pageRequest, Long spaceId);

}
