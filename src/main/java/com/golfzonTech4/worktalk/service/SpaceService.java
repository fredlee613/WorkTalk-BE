package com.golfzonTech4.worktalk.service;

import com.golfzonTech4.worktalk.domain.Member;
import com.golfzonTech4.worktalk.domain.Review;
import com.golfzonTech4.worktalk.domain.Space;
import com.golfzonTech4.worktalk.domain.SpaceImg;
import com.golfzonTech4.worktalk.dto.space.*;
import com.golfzonTech4.worktalk.repository.ListResult;
import com.golfzonTech4.worktalk.repository.MemberRepository;
import com.golfzonTech4.worktalk.repository.ReviewRepository;
import com.golfzonTech4.worktalk.repository.space.SpaceImgRepository;
import com.golfzonTech4.worktalk.repository.space.SpaceRepository;
import com.golfzonTech4.worktalk.service.AwsS3Service;
import com.golfzonTech4.worktalk.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SpaceService {

    private final MemberRepository memberRepository;

    private final MemberService memberService;

    private final SpaceRepository spaceRepository;
    private final SpaceImgRepository spaceImgRepository;
    private final ReviewRepository reviewRepository;

    private final AwsS3Service awsS3Service;

    //사무공간 등록
    @Transactional
    public Space createSpace(SpaceInsertDto dto) {

        Optional<Member> member = memberRepository.findByName(dto.getName());

        if(!member.isPresent()){
            throw new EntityNotFoundException("Member Not Found");
        }
        if(member.get().getActivated() == 1){
            throw new EntityNotFoundException("계정 승인이 완료되면 이용할 수 있어요");
        }

        Space spaceToCreate = new Space();
        BeanUtils.copyProperties(dto, spaceToCreate);
        spaceToCreate.setMember(member.get());
        return spaceRepository.save(spaceToCreate);

    }
    //사무공간 등록-다중이미지추가
    @Transactional
    public Long uploadImage(SpaceImgDto dto, List<MultipartFile> multipartFileList) throws Exception{

        Space findSpace = spaceRepository.findBySpaceId(dto.getSpaceId());
        List<String> imageurlList = new ArrayList<>();
        if (multipartFileList.size() > 0){
            try {
                imageurlList.addAll(awsS3Service.upload(multipartFileList));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            imageurlList.add(null);
        }
        Space space = new Space();

        for (String imageurl : imageurlList){
            SpaceImg spaceImg = new SpaceImg();
            spaceImg.setSpace(findSpace);
            spaceImg.setImgName(imageurl);
            spaceImgRepository.save(spaceImg);
        }

        return findSpace.getSpaceId();

    }

    //사무공간 수정
    @Transactional
    public Space updateSpace(SpaceUpdateDto dto){
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
    public Space selectSpace(Long spaceId){
        log.info("selectSpace()....");
        Space space = spaceRepository.findBySpaceId(spaceId);
        if(space != null){
            return space;
        }

        throw new EntityNotFoundException("해당 사무공간을 찾지 못했습니다.");
    }

    //호스트가 등록한 사무공간 리스트 조회
    public List<Space> selectSpaceByHost(String name) {
        log.info("selectSpaceByHost()....");
//        Optional<String> currentUsername = SecurityUtil.getCurrentUsername();
//        if (currentUsername.isEmpty()) throw new NotFoundMemberException("Member not found");
//        Member findMember = memberService.findByName(currentUsername.get());
        Optional<Member> findMember = memberRepository.findByName(name);

//        if(!optionalMember.isPresent()){
//            throw new EntityNotFoundException("Member Not Found");
//        }
        return spaceRepository.findAllByMemberId(findMember.get().getId());
    }

    //유저-사무공간 리스트 조회
    public ListResult getMainSpacePage(PageRequest pageRequest, SpaceSearchDto dto){
        PageImpl<SpaceMainDto> result = spaceRepository.getMainSpacePage(pageRequest, dto.getSearchSpaceType(), dto.getSearchSpaceName(), dto.getSearchAddress());
        return new ListResult(result.getTotalElements(), result.getContent());
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
    public void deleteSpace(Long spaceId){
        log.info("deleteSpace()....");
        spaceRepository.deleteById(spaceId);
    }

    @Transactional
    public Space ApprovedSpace(Long spaceId){
        log.info("ApprovedSpace()....");
        Space space = selectSpace(spaceId);
        log.info(String.valueOf(spaceId));
        space.setSpaceStatus("approved");    //변경감지
        return space;
    }

    @Transactional
    public Space RejectedSpace(Long spaceId){
        log.info("RejectedSpace()....");
        Space space = selectSpace(spaceId);
        log.info(String.valueOf(spaceId));
        space.setSpaceStatus("rejected");    //변경감지
        return space;
    }

}
