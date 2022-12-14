package com.golfzonTech4.worktalk.repository.customercenter;

import com.golfzonTech4.worktalk.domain.CustomerCenter;
import com.golfzonTech4.worktalk.dto.customercenter.CustomerCenterDetailDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerCenterRepository extends JpaRepository<CustomerCenter, Long>,
        QuerydslPredicateExecutor<CustomerCenter>, CustomerCenterRepositoryCustom {

    CustomerCenter findByCcId(Long ccId); //문의 선택

    @Query("select new com.golfzonTech4.worktalk.dto.customercenter.CustomerCenterDetailDto" +
            "(c.ccId, c.member.id, c.title, c.content, c.type, c.lastModifiedDate, cc.content, cc.lastModifiedDate) " +
            "from CustomerCenter c left join CustomerComment cc on c.ccId = cc.customerCenter.ccId left join c.member m on c.member.name = m.name where m.name = :name")
    List<CustomerCenterDetailDto> findccDtoListByMember(@Param("name") String name);//접속자의 1대1문의 내역 리스트

    @Query("select new com.golfzonTech4.worktalk.dto.customercenter.CustomerCenterDetailDto" +
            "(c.ccId, c.member.id, c.title, c.content, c.type, c.lastModifiedDate, cc.content, cc.lastModifiedDate) " +
            "from CustomerCenter c left join CustomerComment cc on c.ccId = cc.customerCenter.ccId")
    List<CustomerCenterDetailDto> findAllcc();//마스터용 전체 1대1문의 리스트
}
