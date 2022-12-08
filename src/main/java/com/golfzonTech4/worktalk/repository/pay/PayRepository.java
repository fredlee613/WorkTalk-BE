package com.golfzonTech4.worktalk.repository.pay;

import com.golfzonTech4.worktalk.domain.Pay;
import com.golfzonTech4.worktalk.domain.PaymentStatus;
import com.golfzonTech4.worktalk.dto.pay.PaySimpleDto;
import com.golfzonTech4.worktalk.dto.reservation.ReserveSimpleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PayRepository extends JpaRepository<Pay, Long>, PayRepositoryCustom {
    Optional<Pay> findByCustomerUid(String customerUid);

}
