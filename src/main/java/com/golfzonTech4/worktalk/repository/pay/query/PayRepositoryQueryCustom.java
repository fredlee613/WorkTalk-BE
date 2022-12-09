package com.golfzonTech4.worktalk.repository.pay.query;

import com.golfzonTech4.worktalk.domain.PaymentStatus;
import com.golfzonTech4.worktalk.dto.pay.PaySimpleDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;

public interface PayRepositoryQueryCustom {

    PageImpl<PaySimpleDto> findAllByUser(String name, LocalDateTime time, PaymentStatus payStatus, PageRequest pageRequest);
    PageImpl<PaySimpleDto> findAllByHost(String name, LocalDateTime time, PageRequest pageRequest);
}
