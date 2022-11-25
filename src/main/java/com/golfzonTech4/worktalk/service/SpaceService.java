package com.golfzonTech4.worktalk.service;

import com.golfzonTech4.worktalk.domain.Member;
import com.golfzonTech4.worktalk.domain.Space;
import com.golfzonTech4.worktalk.repository.MemberRepository;
import com.golfzonTech4.worktalk.repository.MemberRepository_me;
import com.golfzonTech4.worktalk.repository.SpaceRepository;
import com.golfzonTech4.worktalk.dto.space.SpaceInsertDto;
import com.golfzonTech4.worktalk.dto.space.SpaceUpdateDto;
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
public class SpaceService {

    private final MemberRepository_me memberRepository2;
    private final MemberRepository memberRepository;
    private final SpaceRepository spaceRepository;


    //사무공간 등록
    @Transactional
    public Space createSpace(SpaceInsertDto dto){
        Optional<Member> member = Optional.ofNullable(memberRepository.findOneByName(dto.getName()));
        if(!member.isPresent()){
            throw new EntityNotFoundException("Member Not Found");
        }

        Space spaceToCreate = new Space();
        BeanUtils.copyProperties(dto, spaceToCreate);
        spaceToCreate.setMember(member.get());
        return spaceRepository.save(spaceToCreate);
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
    public Space selectSpace(Long space_id){
        log.info("selectSpace()....");
        Space space = spaceRepository.findBySpaceId(space_id);
        if(space != null){
            return space;
        }

        throw new EntityNotFoundException("해당 사무공간을 찾지 못했습니다.");
    }

    //호스트가 등록한 사무공간 리스트 조회
    public List<Space> selectSpaceByHost(Member member) {
        log.info("selectSpaceByHost()....");
        return spaceRepository.findAllByMember(member);
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

}
