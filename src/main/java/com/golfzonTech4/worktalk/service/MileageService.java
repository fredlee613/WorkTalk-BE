package com.golfzonTech4.worktalk.service;

import com.golfzonTech4.worktalk.domain.Member;
import com.golfzonTech4.worktalk.domain.Mileage;
import com.golfzonTech4.worktalk.domain.Mileage_status;
import com.golfzonTech4.worktalk.domain.Pay;
import com.golfzonTech4.worktalk.dto.mileage.MileageDto;
import com.golfzonTech4.worktalk.dto.mileage.MileageFindDto;
import com.golfzonTech4.worktalk.repository.ListResult;
import com.golfzonTech4.worktalk.repository.member.MemberRepository;
import com.golfzonTech4.worktalk.repository.mileage.MileageRepository;
import com.golfzonTech4.worktalk.repository.pay.PayRepository;
import com.golfzonTech4.worktalk.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MileageService {

    private final MileageRepository mileageRepository;
    private final PayRepository payRepository;
    private final MemberRepository memberRepository;

    /**
     * 마일리지 적립 로직
     */
    @Transactional
    public Long save(MileageDto dto) {
        log.info("save : {}", dto);

        String userName = SecurityUtil.getCurrentUsername().get();
        // 선결제일 경우에는 클라이언트 side 에서 온 request로 저장되기에 사용자 호출이 가능하다.
        Pay findPay = payRepository.findById(dto.getPayId()).get();
        Member member = memberRepository.findByName(userName).get();

        Mileage mileage = Mileage.builder()
                .pay(findPay)
                .status(Mileage_status.SAVED)
                .mileageAmount(dto.getMileageAmount())
                .mileageDate(LocalDate.now())
                .member(member)
                .build();

        return mileageRepository.save(mileage).getMileageId();
    }

    /**
     * 마일리지 사용 로직
     */
    @Transactional
    public Long use(MileageDto dto) {
        log.info("use : {}", dto);

        String userName = SecurityUtil.getCurrentUsername().get();
        Pay findPay = payRepository.findById(dto.getPayId()).get();
        Member member = memberRepository.findByName(userName).get();
        int total = getTotal();
        log.info("total : {}", total);

        if (dto.getMileageAmount() > total) {
            throw new IllegalArgumentException("마일리지 사용 희망액이 잔액보다 큽니다.");
        }

        Mileage mileage = Mileage.builder()
                .pay(findPay)
                .status(Mileage_status.USED)
                .mileageAmount(dto.getMileageAmount())
                .mileageDate(LocalDate.now())
                .member(member)
                .build();

        return mileageRepository.save(mileage).getMileageId();
    }

    /**
     * 마일리지 적립 취소 (삭제) 로직
     */
    @Transactional
    public void cancelSave(Long payId) {
        log.info("cancelSave : {}", payId);
        Optional<Mileage> deleteMileage = mileageRepository.findByPay(payId, Mileage_status.SAVED);
        if (! deleteMileage.isEmpty()) mileageRepository.delete(deleteMileage.get());
    }

    /**
     * 마일리지 사용 취소 (삭제) 로직
     */
    @Transactional
    public void cancelUsage(Long payId) {
        log.info("cancelUsage : {}", payId);
        Optional<Mileage> deleteMileage = mileageRepository.findByPay(payId, Mileage_status.USED);
        if (! deleteMileage.isEmpty()) mileageRepository.delete(deleteMileage.get());
    }

    /**
     * 마일리지 이력 조회 로직
     */
    public ListResult findAllByName() {
        String currentUser = SecurityUtil.getCurrentUsername().get();
        List<MileageFindDto> findMileages = mileageRepository.findAllByName(currentUser);
        return new ListResult((long) getTotal(), findMileages);
    }

    /**
     * 사용 가능 마일리지 조회 로직
     */
    public int getTotal() {
        log.info("getTotal : {}");
        String currentUser = SecurityUtil.getCurrentUsername().get();
        Member findMember = memberRepository.findByName(currentUser).get();

        Integer totalSave = mileageRepository.getTotalSave(findMember.getId());
        totalSave = totalSave == null ? 0 : totalSave;

        Integer totalUse = mileageRepository.getTotalUse(findMember.getId());
        totalUse = totalUse == null ? 0 : totalUse;

        int total = totalSave - totalUse;
        return total;
    }

    /**
     * 마일리지 리스트 조회 (payId기준)
     */
    public List<Mileage> findAllByPay(Long payId) {
        log.info("getListByPay: {}", payId);
        return mileageRepository.findAllByPay(payId);
    }
}
