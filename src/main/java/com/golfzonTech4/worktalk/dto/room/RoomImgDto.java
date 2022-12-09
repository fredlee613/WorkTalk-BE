package com.golfzonTech4.worktalk.dto.room;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoomImgDto {

    private Long roomImgId;

    private String roomImgUrl;

    private Long roomId;

    @QueryProjection
    public RoomImgDto(Long roomImgId, String roomImgUrl, Long roomId) {
        this.roomImgId = roomImgId;
        this.roomImgUrl = roomImgUrl;
        this.roomId = roomId;
    }
}
