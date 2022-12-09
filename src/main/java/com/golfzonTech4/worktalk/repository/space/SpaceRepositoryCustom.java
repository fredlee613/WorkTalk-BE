package com.golfzonTech4.worktalk.repository.space;

import com.golfzonTech4.worktalk.dto.space.SpaceMainDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

public interface SpaceRepositoryCustom {

//    Page<Space> getAdminSpacePage(SpaceSearchDto spaceSearchDto, Pageable pageable);

//    List<SpaceMainDto> getMainPage(Integer spaceType, String spaceName, String address);

    PageImpl<SpaceMainDto> getMainSpacePage(PageRequest pageRequest, Integer spaceType, String spaceName, String address);
}
