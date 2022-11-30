package com.golfzonTech4.worktalk.repository.pay;

import com.golfzonTech4.worktalk.domain.Pay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PayRepository extends JpaRepository<Pay, Long>, PayRepositoryCustom {
    Optional<Pay> findByCustomerUid(String customerUid);
}
