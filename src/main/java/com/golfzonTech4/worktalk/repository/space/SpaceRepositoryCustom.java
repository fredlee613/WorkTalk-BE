package com.golfzonTech4.worktalk.repository.space;

import com.golfzonTech4.worktalk.domain.Space;
import com.golfzonTech4.worktalk.dto.space.SpaceMainDto;
import com.golfzonTech4.worktalk.dto.space.SpaceSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SpaceRepositoryCustom {

    Page<Space> getAdminSpacePage(SpaceSearchDto spaceSearchDto, Pageable pageable);

    List<SpaceMainDto> getMainPage(Integer spaceType, String spaceName, String address);

    Page<SpaceMainDto> getMainSpacePage(Pageable pageable, Integer spaceType, String spaceName, String address);
}
