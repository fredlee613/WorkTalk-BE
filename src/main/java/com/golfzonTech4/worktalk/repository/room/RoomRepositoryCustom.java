package com.golfzonTech4.worktalk.repository.room;

import com.golfzonTech4.worktalk.dto.room.RoomDetailDto;

import java.util.List;

public interface RoomRepositoryCustom {

    List<RoomDetailDto> getRooms(Long spaceId);

}
