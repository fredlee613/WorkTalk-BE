package com.golfzonTech4.worktalk.repository;

import com.golfzonTech4.worktalk.domain.Member;
import com.golfzonTech4.worktalk.domain.Room;
import com.golfzonTech4.worktalk.domain.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findAllBySpace(Space space); //해당 사무공간의 세부공간 리스트

    Room findByRoomId(Long roomId); //세부공간 선택

}
