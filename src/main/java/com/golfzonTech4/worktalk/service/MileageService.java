package com.golfzonTech4.worktalk.service;

import com.golfzonTech4.worktalk.domain.Member;
import com.golfzonTech4.worktalk.domain.Mileage;
import com.golfzonTech4.worktalk.domain.Mileage_status;
import com.golfzonTech4.worktalk.domain.Pay;
import com.golfzonTech4.worktalk.dto.mileage.MileageDto;
import com.golfzonTech4.worktalk.dto.mileage.MileageFindDto;
import com.golfzonTech4.worktalk.repository.MemberRepository;
import com.golfzonTech4.worktalk.repository.mileage.MileageRepository;
import com.golfzonTech4.worktalk.repository.pay.PayRepository;
import com.golfzonTech4.worktalk.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
    public Long save(MileageDto dto) {
        log.info("save : {}", dto);

        String userName = SecurityUtil.getCurrentUsername().get();
        Pay findPay = payRepository.findById(dto.getPayId()).get();
        Member member = memberRepository.findByName(userName).get();

        Mileage mileage = Mileage.builder()
                .pay(findPay)
                .status(Mileage_status.SAVED)
                .mileageAmount(dto.getMileageAmount())
                .mileageDate(dto.getMileageDate())
                .member(member)
                .build();

        return mileageRepository.save(mileage).getMileageId();
    }

    /**
     * 마일리지 사용 로직
     */
    public Long use(MileageDto dto) {
        log.info("use : {}", dto);

        String userName = SecurityUtil.getCurrentUsername().get();
        Pay findPay = payRepository.findById(dto.getPayId()).get();
        Member member = memberRepository.findByName(userName).get();
        int total = getTotal();

        if (dto.getMileageAmount() > getTotal()) {
            throw new IllegalArgumentException("마일리지 사용 희망액이 잔액보다 큽니다.");
        }

        Mileage mileage = Mileage.builder()
                .pay(findPay)
                .status(Mileage_status.USED)
                .mileageAmount(dto.getMileageAmount())
                .mileageDate(dto.getMileageDate())
                .member(member)
                .build();

        return mileageRepository.save(mileage).getMileageId();
    }

    /**
     * 마일리지 적립 취소 (삭제) 로직
     */
    public void cancelSave(Long payId) {
        log.info("delete : {}", payId);
        Mileage deleteMileage = mileageRepository.findById(payId).get();
        mileageRepository.delete(deleteMileage);
    }

    /**
     * 마일리지 사용 취소 (삭제) 로직
     */
    public void cancelUsage(Long payId) {
        log.info("delete : {}", payId);
        Mileage deleteMileage = mileageRepository.findById(payId).get();
        mileageRepository.delete(deleteMileage);
    }

    /**
     * 마일리지 이력 조회 로직
     */
    public List<MileageFindDto> findAllByName() {
        String currentUser = SecurityUtil.getCurrentUsername().get();
        return mileageRepository.findAllByName(currentUser);
    }

    /**
     * 사용 가능 마일리지 조회 로직
     */
    public int getTotal() {
        log.info("getTotal : {}");
        String currentUser = SecurityUtil.getCurrentUsername().get();
        Member findMember = memberRepository.findByName(currentUser).get();
        int totalSave = mileageRepository.getTotalSave(findMember.getId());
        int totalUse = mileageRepository.getTotalUse(findMember.getId());
        int total = totalSave - totalUse;
        return total;
    }

}
