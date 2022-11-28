package com.golfzonTech4.worktalk.service;

import com.golfzonTech4.worktalk.domain.Room;
import com.golfzonTech4.worktalk.domain.Space;
import com.golfzonTech4.worktalk.repository.RoomRepository;
import com.golfzonTech4.worktalk.repository.SpaceRepository;
import com.golfzonTech4.worktalk.dto.room.RoomInsertDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final SpaceRepository spaceRepository;


    //세부공간 등록
    @Transactional
    public Room createRoom(RoomInsertDto dto){
        Optional<Space> space = Optional.ofNullable(spaceRepository.findBySpaceId(dto.getSpaceId()));
        if(!space.isPresent()){
            throw new EntityNotFoundException("Space Not Found");
        }
        Room roomToCreate = new Room();
        BeanUtils.copyProperties(dto, roomToCreate);
        roomToCreate.setSpace(space.get()); //spaceId 가져오기
        space.get().setSpaceStatus("waiting");//space테이블의 spaceStatus상태 waiting으로 바꾸기

        return roomRepository.save(roomToCreate);
    }

    public List<Room> getRooms(Space space) {
        log.info("getRooms()....");
        return roomRepository.findAllBySpace(space);
    }

    public Room selectRoom(Long roomId){
        log.info("selectRoom()....");
        Room room = roomRepository.findByRoomId(roomId);
        if(room != null){
            return room;
        }

        throw new EntityNotFoundException("해당 사무공간을 찾지 못했습니다.");
    }

    @Transactional
    public void deleteRoom(Long roomId){
        log.info("deleteRoom()....");
        roomRepository.deleteById(roomId);
    }


}
