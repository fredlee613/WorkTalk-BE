package com.golfzonTech4.worktalk.repository.pay.query;

import com.golfzonTech4.worktalk.domain.Pay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayRepositoryQuery extends JpaRepository<Pay, Long>, PayRepositoryQueryCustom {
}
