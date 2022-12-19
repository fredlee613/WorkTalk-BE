package com.golfzonTech4.worktalk.repository.customercenter;

import com.golfzonTech4.worktalk.domain.CustomerCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerCenterRepository extends JpaRepository<CustomerCenter, Long>,
        QuerydslPredicateExecutor<CustomerCenter>, CustomerCenterRepositoryCustom {

    CustomerCenter findByCcId(Long ccId); //문의 선택

}
