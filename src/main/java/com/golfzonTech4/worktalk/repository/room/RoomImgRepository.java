package com.golfzonTech4.worktalk.repository.room;

import com.golfzonTech4.worktalk.domain.Room;
import com.golfzonTech4.worktalk.domain.RoomImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomImgRepository extends JpaRepository<RoomImg, Long> {
    List<RoomImg> findByRoom(Room room);
}
