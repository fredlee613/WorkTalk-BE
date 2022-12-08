package com.golfzonTech4.worktalk.service;

import com.golfzonTech4.worktalk.domain.Member;
import com.golfzonTech4.worktalk.domain.Penalty;
import com.golfzonTech4.worktalk.dto.penalty.PenaltyDto;
import com.golfzonTech4.worktalk.dto.penalty.PenaltySearchDto;
import com.golfzonTech4.worktalk.repository.ListResult;
import com.golfzonTech4.worktalk.repository.member.MemberRepository;
import com.golfzonTech4.worktalk.repository.penalty.PenaltyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PenaltyService {

    private final PenaltyRepository penaltyRepository;
    private final MemberRepository memberRepository;

    /**
     * 페널티 부여 매서드
     */
    @Transactional
    public Long addPenalty(PenaltyDto dto) {
        log.info("penalize : {}", dto);
        Member findMember = memberRepository.findById(dto.getMemberId()).get();
        Penalty penalty = Penalty.builder()
                .member(findMember)
                .penaltyReason(dto.getPenaltyReason())
                .penaltyType(dto.getPenaltyType())
                .penaltyDate(LocalDateTime.now())
                .build();
        // 페널티 데이터 저장
        Penalty savedPenalty = penaltyRepository.save(penalty);
        // 해당 사용자에게 페널티 부여
        findMember.setActivated(0);
        return savedPenalty.getPenaltyId();
    }

    /**
     * 페널티 회수 매서드
     */
    @Transactional
    public void removePenalty(Long penaltyId) {
        log.info("removePenalty : {}", penaltyId);
        Penalty findPenalty = penaltyRepository.findById(penaltyId).get();
        // 해당 사용자의 페널티 회수
        Member findMember = findPenalty.getMember();
        findMember.setActivated(1);
        // 페널티 데이터 삭제
        penaltyRepository.delete(findPenalty);
    }

    /**
     * 페널티 리스트 조회
     */
    public ListResult findPenalties() {
        List<PenaltySearchDto> findPenalties = penaltyRepository.findPenalties();
        return new ListResult((long) findPenalties.size(), findPenalties);
    }

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void updatePenalties() {
        List<Penalty> penalties = penaltyRepository.findAll();
        for (Penalty penalty : penalties) {
            if (penalty.getPenaltyDate().plusDays(7).isBefore(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT))) {
                Optional<Member> member = memberRepository.findById(penalty.getMember().getId());
                if (member.isPresent()){ member.get().setActivated(1);}

                penaltyRepository.delete(penalty);
            }
        }
    }

}
