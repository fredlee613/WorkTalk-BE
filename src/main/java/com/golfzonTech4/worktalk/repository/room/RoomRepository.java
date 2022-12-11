package com.golfzonTech4.worktalk.repository.room;

import com.golfzonTech4.worktalk.domain.Room;
import com.golfzonTech4.worktalk.domain.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>,
        QuerydslPredicateExecutor<Room>, RoomRepositoryCustom {

    List<Room> findAllBySpace(Space space); //해당 사무공간의 세부공간 리스트

    Room findByRoomId(Long roomId); //세부공간 선택

}
