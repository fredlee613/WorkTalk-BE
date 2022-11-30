package com.golfzonTech4.worktalk.service;

import com.golfzonTech4.worktalk.domain.Member;
import com.golfzonTech4.worktalk.domain.Space;
import com.golfzonTech4.worktalk.domain.SpaceImgg;
import com.golfzonTech4.worktalk.exception.NotFoundMemberException;
import com.golfzonTech4.worktalk.repository.MemberRepository;
import com.golfzonTech4.worktalk.repository.SpaceRepository;
import com.golfzonTech4.worktalk.dto.space.SpaceInsertDto;
import com.golfzonTech4.worktalk.dto.space.SpaceUpdateDto;
import com.golfzonTech4.worktalk.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
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

    private final SpaceImgService spaceImgService;


    //사무공간 등록
    @Transactional
    public Space createSpace(SpaceInsertDto dto){

        Optional<Member> member = memberRepository.findByName(dto.getName());

        if(!member.isPresent()){
            throw new EntityNotFoundException("Member Not Found");
        }

        Space spaceToCreate = new Space();
        BeanUtils.copyProperties(dto, spaceToCreate);
        spaceToCreate.setMember(member.get());
        return spaceRepository.save(spaceToCreate);
    }
    //사무공간 등록-다중이미지추가
    @Transactional
    public Long createiSpace(SpaceInsertDto dto,
                             List<MultipartFile> multipartFileList) throws Exception{
        Optional<Member> member = memberRepository.findByName(dto.getName());
        if(!member.isPresent()){
            throw new EntityNotFoundException("Member Not Found");
        }

        Space spaceToCreate = new Space();
        BeanUtils.copyProperties(dto, spaceToCreate);
        spaceToCreate.setMember(member.get());
        spaceRepository.save(spaceToCreate);

        //이미지 등록
        for (int i=0; i< multipartFileList.size();i++){
            SpaceImgg spaceImgg = new SpaceImgg();
            spaceImgg.setSpace(spaceToCreate);
            spaceImgService.saveSpaceImg(spaceImgg, multipartFileList.get(i));
        }
        return spaceToCreate.getSpaceId();

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
        findSpace.setSpaceImg(dto.getSpaceImg());
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
    public List<Space> selectSpaceAll(){
        return spaceRepository.findAll();
    }

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

}
