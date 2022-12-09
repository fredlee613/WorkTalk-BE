package com.golfzonTech4.worktalk.repository.reservation.query;

import com.golfzonTech4.worktalk.domain.PaymentStatus;
import com.golfzonTech4.worktalk.domain.RoomType;
import com.golfzonTech4.worktalk.dto.reservation.ReserveSimpleDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

public interface ReservationRepositoryQueryCustom {

    public PageImpl<ReserveSimpleDto> findAllByHost(
            String name, Integer paid, RoomType roomType,
            PaymentStatus paymentStatus, PageRequest pageRequest);
}
