package com.golfzonTech4.worktalk.service;

import com.golfzonTech4.worktalk.domain.Member;
import com.golfzonTech4.worktalk.domain.Qna;
import com.golfzonTech4.worktalk.domain.Room;
import com.golfzonTech4.worktalk.domain.Space;
import com.golfzonTech4.worktalk.dto.qna.QnaDetailDto;
import com.golfzonTech4.worktalk.dto.qna.QnaInsertDto;
import com.golfzonTech4.worktalk.dto.qna.QnaUpdateDto;
import com.golfzonTech4.worktalk.exception.NotFoundMemberException;
import com.golfzonTech4.worktalk.repository.MemberRepository;
import com.golfzonTech4.worktalk.repository.QnaRepository;
import com.golfzonTech4.worktalk.repository.SpaceRepository;
import com.golfzonTech4.worktalk.util.SecurityUtil;
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
public class QnaService {

    private final SpaceRepository spaceRepository;
    private final QnaRepository qnaRepository;
    private final MemberService memberService;

    @Transactional
    public Qna createQna(QnaInsertDto dto){
        log.info("createQna()....");
        Optional<String> member = SecurityUtil.getCurrentUsername();
        Optional<Space> space = Optional.ofNullable(spaceRepository.findBySpaceId(dto.getSpaceId()));

        if(!member.isPresent()) throw new EntityNotFoundException("Member Not Found");
        if(!space.isPresent()) throw new EntityNotFoundException("Space Not Found");

        Member findMember = memberService.findByName(member.get());
        Space findSpace = spaceRepository.findBySpaceId(dto.getSpaceId());

        Qna qnaToCreate = new Qna();
        BeanUtils.copyProperties(dto, qnaToCreate);
        qnaToCreate.setMember(findMember);
        qnaToCreate.setSpace(findSpace);

        return  qnaRepository.save(qnaToCreate);
    }

    @Transactional
    public void updateQna(Long qnaId, QnaUpdateDto dto) {
        log.info("updateQna()....");
        Optional<String> currentUsername = SecurityUtil.getCurrentUsername();

        Optional<Qna> optionalQna = Optional.ofNullable(qnaRepository.findByQnaId(qnaId));

        if (!currentUsername.isPresent()) throw new EntityNotFoundException("Member Not Found");
        if (!optionalQna.isPresent()) throw new EntityNotFoundException("QnA Not Found");

        Member findMember = memberService.findByName(currentUsername.get());
        //qna작성자와 접속자가 같은지 확인
        if (findMember.getId() == optionalQna.get().getMember().getId()) {
            Qna qna = optionalQna.get();
            qna.setContent(dto.getContent());//dirty checking
        } else
        throw new EntityNotFoundException("수정 권한이 없습니다.");
    }

    @Transactional
    public void deleteQna(Long qnaId){
        log.info("deleteQna()....");

        Optional<String> currentUsername = SecurityUtil.getCurrentUsername();
        if (!currentUsername.isPresent()) throw new EntityNotFoundException("Member Not Found");

        Optional<Qna> optionalQna = Optional.ofNullable(qnaRepository.findByQnaId(qnaId));

        Member findMember = memberService.findByName(currentUsername.get());
        //qna작성자와 접속자가 같은지 확인
        if (findMember.getId() == optionalQna.get().getMember().getId()) {
            qnaRepository.deleteById(qnaId);
        } else
            throw new EntityNotFoundException("삭제 권한이 없습니다.");
    }

    public List<QnaDetailDto> getQnas(Long spaceId) {
        log.info("getQnas()....");
        return qnaRepository.findQnaDtoListBySpaceId(spaceId);
    }

    public List<QnaDetailDto> QnaListPage() {
        log.info("QnaListPage()....");

        Optional<String> currentUsername = SecurityUtil.getCurrentUsername();
        if (currentUsername.isEmpty()) throw new EntityNotFoundException("Member not found");

        return qnaRepository.findQnaDtoListByMember(currentUsername.get());
    }

}
