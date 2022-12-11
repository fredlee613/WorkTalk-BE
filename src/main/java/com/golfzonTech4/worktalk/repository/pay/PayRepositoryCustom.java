package com.golfzonTech4.worktalk.repository.pay;

import com.golfzonTech4.worktalk.domain.PaymentStatus;
import com.golfzonTech4.worktalk.dto.pay.PayInsertDto;
import com.golfzonTech4.worktalk.dto.pay.PaySimpleDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PayRepositoryCustom {
    public List<PayInsertDto> findAlByReserveId(Long reserveId);

    public Optional<PayInsertDto> findByCUid(String customerUid);

    public PageImpl<PaySimpleDto> findByUser(String name, LocalDateTime reserveDate, PaymentStatus paymentStatus,
                                             PageRequest pageRequest);
    public PageImpl<PaySimpleDto> findByHost(String name, LocalDateTime reserveDate, PaymentStatus paymentStatus,
                                             Integer spaceType, String roomName, PageRequest pageRequest);

    public List<PaySimpleDto> findRooms(String name);

}
