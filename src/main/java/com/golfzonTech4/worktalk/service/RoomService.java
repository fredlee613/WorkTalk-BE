package com.golfzonTech4.worktalk.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.golfzonTech4.worktalk.domain.Room;
import com.golfzonTech4.worktalk.domain.RoomImg;
import com.golfzonTech4.worktalk.domain.Space;
import com.golfzonTech4.worktalk.dto.room.RoomDetailDto;
import com.golfzonTech4.worktalk.dto.room.RoomImgDto;
import com.golfzonTech4.worktalk.dto.room.RoomInsertDto;
import com.golfzonTech4.worktalk.dto.room.RoomUpdateDto;
import com.golfzonTech4.worktalk.repository.room.RoomImgRepository;
import com.golfzonTech4.worktalk.repository.room.RoomRepository;
import com.golfzonTech4.worktalk.repository.space.SpaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomImgRepository roomImgRepository;
    private final SpaceRepository spaceRepository;
    private final AwsS3Service awsS3Service;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    //세부공간 등록
    @Transactional
    public Long createRoom(RoomInsertDto dto){
        Optional<Space> space = Optional.ofNullable(spaceRepository.findBySpaceId(dto.getSpaceId()));
        if(!space.isPresent()){
            throw new EntityNotFoundException("Space Not Found");
        }

        List<String> imageurlList = new ArrayList<>();
        if (!dto.getMultipartFileList().isEmpty()) {
            imageurlList.addAll(awsS3Service.upload(dto.getMultipartFileList()));
        } else {
            imageurlList.add(null);
        }

        Room roomToCreate = new Room();
        BeanUtils.copyProperties(dto, roomToCreate);
        roomToCreate.setSpace(space.get()); //spaceId 가져오기
        space.get().setSpaceStatus("waiting");//space테이블의 spaceStatus상태 waiting으로 바꾸기

        roomRepository.save(roomToCreate);

        for (String imageurl : imageurlList) {
            RoomImg roomImgToCreate = new RoomImg();
            BeanUtils.copyProperties(dto, roomImgToCreate);
            roomImgToCreate.setRoom(roomToCreate);
            roomImgToCreate.setRoomImgUrl(imageurl);
            roomImgRepository.save(roomImgToCreate);
        }
        return roomToCreate.getRoomId();
    }

    //세부사무공간 수정
    @Transactional
    public void updateRoom(RoomUpdateDto dto) {
        log.info("updateRoom()....");
        Room room = roomRepository.findByRoomId(dto.getRoomId());

        List<RoomImg> imageurlList = new ArrayList<>();
        if (!dto.getMultipartFileList().isEmpty() || dto.getMultipartFileList() != null) {
            List<String> newImageurlList = new ArrayList<>();
            newImageurlList.addAll(awsS3Service.upload(dto.getMultipartFileList()));
            for (String imageurl : newImageurlList) {
                RoomImg roomImg = new RoomImg();
                roomImg.setRoom(room);
                roomImg.setRoomImgUrl(imageurl);
                roomImgRepository.save(roomImg);
            }
        } else{
            imageurlList = roomImgRepository.findByRoom(room);
        }
        room.setRoomDetail(dto.getRoomDetail());
        room.setRoomPrice(dto.getRoomPrice());
        room.setWorkStart(dto.getWorkStart());
        room.setWorkEnd(dto.getWorkEnd());
        room.setOfferingOption(dto.getOfferingOption());
    }

    //사무공간에 해당하는 세부공간들 가져오기
    public List<RoomDetailDto> getRooms(Long spaceId) {
        log.info("getRooms()....");
        return roomRepository.getRooms(spaceId);
    }

    //세부사무공간 단일 선택
    public Room selectRoom(Long roomId){
        log.info("selectRoom()....");
        Room room = roomRepository.findByRoomId(roomId);
        if(room != null){
            return room;
        }
        throw new EntityNotFoundException("해당 사무공간을 찾지 못했습니다.");
    }

    //세부사무공간 삭제
    @Transactional
    public void deleteRoom(Long roomId){
        log.info("deleteRoom()....");
        roomRepository.deleteById(roomId);
    }

    // 이미지 삭제
    public void deleteRoomImg(RoomImgDto dto){
        Optional<RoomImg> findRoomImg = roomImgRepository.findById(dto.getRoomImgId());
        String roomImgUrl = findRoomImg.get().getRoomImgUrl();
        try {
            log.info("deleteSpaceImg : ", roomImgUrl.substring(roomImgUrl.lastIndexOf("/")+1));
            amazonS3.deleteObject(this.bucket, roomImgUrl.substring(roomImgUrl.lastIndexOf("/")+1)); // s3에서 이미지 삭제
        } catch (AmazonServiceException e){
            log.error(e.getErrorMessage());
        }
        roomImgRepository.deleteById(dto.getRoomImgId()); // DB에서 이미지 삭제
    }


}
