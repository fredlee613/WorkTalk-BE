package com.golfzonTech4.worktalk.service;

import com.golfzonTech4.worktalk.domain.Member;
import com.golfzonTech4.worktalk.domain.Review;
import com.golfzonTech4.worktalk.domain.Space;
import com.golfzonTech4.worktalk.domain.SpaceImg;
import com.golfzonTech4.worktalk.dto.space.*;
import com.golfzonTech4.worktalk.repository.ListResult;
import com.golfzonTech4.worktalk.repository.ReviewRepository;
import com.golfzonTech4.worktalk.repository.member.MemberRepository;
import com.golfzonTech4.worktalk.repository.space.SpaceImgRepository;
import com.golfzonTech4.worktalk.repository.space.SpaceRepository;
import com.golfzonTech4.worktalk.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
public class SpaceService {

    private final MemberRepository memberRepository;
    private final SpaceRepository spaceRepository;
    private final SpaceImgRepository spaceImgRepository;
    private final ReviewRepository reviewRepository;
    private final AwsS3Service awsS3Service;

    //사무공간 등록
    @Transactional
    public Long createSpace(SpaceInsertDto dto) {
        log.info("createSpace : {}", dto);
        Optional<String> currentUsername = SecurityUtil.getCurrentUsername();

        Optional<Member> member = memberRepository.findByName(currentUsername.get());

        if (member.get().getActivated() == 0) {
            throw new EntityNotFoundException("계정 승인이 완료되면 이용할 수 있어요");
        }

        List<String> imageurlList = new ArrayList<>();
        if (!dto.getMultipartFileList().isEmpty()) {
            imageurlList.addAll(awsS3Service.upload(dto.getMultipartFileList()));
        } else {
            imageurlList.add(null);
        }

        Space spaceToCreate = new Space();
        BeanUtils.copyProperties(dto, spaceToCreate);
        spaceToCreate.setMember(member.get());
        spaceRepository.save(spaceToCreate);

        for (String imageurl : imageurlList) {
            SpaceImg spaceImgToCreate = new SpaceImg();
            BeanUtils.copyProperties(dto, spaceImgToCreate);
            spaceImgToCreate.setSpace(spaceToCreate);
            spaceImgToCreate.setSpaceImgUrl(imageurl);
            spaceImgRepository.save(spaceImgToCreate);
        }
        return spaceToCreate.getSpaceId();
    }

    //사무공간 다중이미지 등록
//    @Transactional
//    public Long uploadSpaceImage(SpaceImgDto dto, List<MultipartFile> multipartFileList) {
//
//        Space findSpace = spaceRepository.findBySpaceId(dto.getSpaceId());
//        List<String> imageurlList = new ArrayList<>();
//        if (multipartFileList.size() > 0) {
//            imageurlList.addAll(awsS3Service.upload(multipartFileList));
//        } else {
//            imageurlList.add(null);
//        }
//
//        for (String imageurl : imageurlList) {
//            SpaceImg spaceImgToCreate = new SpaceImg();
//            BeanUtils.copyProperties(dto, spaceImgToCreate);
//            spaceImgToCreate.setSpace(findSpace);
//            spaceImgToCreate.setSpaceImgUrl(imageurl);
//            spaceImgRepository.save(spaceImgToCreate);
//        }
//
//        return findSpace.getSpaceId();
//
//    }

    //사무공간 수정
    @Transactional
    public Space updateSpace(SpaceUpdateDto dto) {
        log.info("updateSpace()....");
//        Long result = 0L;
//        Optional<Member> optionalMember = memberRepository.findById(dto.getMemberId());
//        if(!optionalMember.isPresent()){
//            throw new EntityNotFoundException("Member Not Found");
//        }
//        Member member = optionalMember.get();

        Space findSpace = spaceRepository.findBySpaceId(dto.getSpaceId());
        findSpace.setSpaceDetail(dto.getSpaceDetail());
//        findSpace.setSpaceImg(dto.getSpaceImg());
        //변경감지 사용
//        return "수정완료";
        return spaceRepository.save(findSpace);
    }

    //사무공간 상세페이지
    public Space selectSpace(Long spaceId) {
        log.info("selectSpace()....");
        Space space = spaceRepository.findBySpaceId(spaceId);
        if (space != null) {
            return space;
        }

        throw new EntityNotFoundException("해당 사무공간을 찾지 못했습니다.");
    }

    public List<SpaceDetailDto> getSpaceDetailPage(Long spaceId) {
        log.info("selectSpace()....");
        List<SpaceDetailDto> space = spaceRepository.getSpaceDetailPage(spaceId);
        if (space != null) {
            return space;
        }

        throw new EntityNotFoundException("해당 사무공간을 찾지 못했습니다.");
    }

    //호스트가 등록한 사무공간 리스트 조회
    public List<SpaceMainDto> selectSpaceByHost() {
        log.info("selectSpaceByHost()....");
        Optional<String> currentUsername = SecurityUtil.getCurrentUsername();

        List<SpaceMainDto> result =  spaceRepository.getHostSpacePage(currentUsername.get());
        return getSpaceList(result);
    }

    //유저-사무공간 리스트 조회
    public ListResult getMainSpacePage(PageRequest pageRequest, SpaceSearchDto dto) {
        PageImpl<SpaceMainDto> result = spaceRepository.getMainSpacePage(pageRequest, dto);
        return new ListResult(result.getTotalElements(), getSpaceList(result.getContent()));
    }

    public List<SpaceMainDto> getSpaceList(List<SpaceMainDto> spaceList) {
        List<SpaceMainDto> spaceMainDtoList = new ArrayList<>();
        for (SpaceMainDto space : spaceList) {
            SpaceMainDto dto = space;

                List<Review> reviewList = reviewRepository.findAllBySpaceId(space.getSpaceId());
            double sum = 0;
            for (Review review : reviewList) {
                sum += review.getGrade();
            }
            double avg = (sum == 0) ? 0 : Math.floor((sum / reviewList.size()) * 10) / 10;
            log.info("avg= "+ avg);
            dto.setCount(reviewList.size());
            dto.setGradeAvg(avg);
            dto.setSpaceId(space.getSpaceId());
            dto.setSpaceType(space.getSpaceType());
            dto.setSpaceName(space.getSpaceName());
            dto.setAddress(space.getAddress());
            spaceMainDtoList.add(dto);
        }
        return spaceMainDtoList;

    }


//    //유저-사무공간 리스트 조회
//    public List<SpaceMainDto> getMainPage(SpaceSearchDto dto){
//        return spaceRepository.getMainPage(dto.getSearchSpaceType(), dto.getSearchSpaceName(), dto.getSearchAddress());
//    }
//
//    //유저-사무공간 리스트 조회
//    public List<Space> findAllBySpaceStatus(){
//        log.info("findAllBySpaceStatus()....");
//        return spaceRepository.findAllBySpaceStatus();
//    }

    @Transactional
    public void deleteSpace(Long spaceId) {
        log.info("deleteSpace()....");
        spaceRepository.deleteById(spaceId);
    }

    public ListResult getSpaceMasterPage(PageRequest pageRequest, SpaceManageSortingDto dto) {
        log.info("getSpaceMasterPage()....");
        PageImpl<SpaceMasterDto> result = spaceRepository.getSpaceMasterPage(pageRequest, dto);
        return new ListResult(result.getTotalElements(), result.getContent());
    }

    @Transactional
    public Space ApprovedSpace(Long spaceId) {
        log.info("ApprovedSpace()....");
        Space space = selectSpace(spaceId);
        log.info(String.valueOf(spaceId));
        space.setSpaceStatus("approved");    //변경감지
        return space;
    }

    @Transactional
    public Space RejectedSpace(Long spaceId) {
        log.info("RejectedSpace()....");
        Space space = selectSpace(spaceId);
        log.info(String.valueOf(spaceId));
        space.setSpaceStatus("rejected");    //변경감지
        return space;
    }

}
