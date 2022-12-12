package com.golfzonTech4.worktalk.repository.space;

import com.golfzonTech4.worktalk.dto.space.SpaceDetailDto;
import com.golfzonTech4.worktalk.dto.space.SpaceMainDto;
import com.golfzonTech4.worktalk.dto.space.SpaceSearchDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface SpaceRepositoryCustom {

//    Page<Space> getAdminSpacePage(SpaceSearchDto spaceSearchDto, Pageable pageable);

//    List<SpaceMainDto> getMainPage(Integer spaceType, String spaceName, String address);

    PageImpl<SpaceMainDto> getMainSpacePage(PageRequest pageRequest, SpaceSearchDto dto);

//    PageImpl<SpaceMainDto> getMainSpacePage(PageRequest pageRequest, Integer spaceType, String spaceName, String address);

    List<SpaceDetailDto> getSpaceDetailPage(Long spaceId); //사무공간 상세페이지
}
