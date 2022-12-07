package com.golfzonTech4.worktalk.repository;

import com.golfzonTech4.worktalk.domain.CustomerCenter;
import com.golfzonTech4.worktalk.domain.Qna;
import com.golfzonTech4.worktalk.dto.customercenter.CustomerCenterDetailDto;
import com.golfzonTech4.worktalk.dto.qna.QnaDetailDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerCenterRepository extends JpaRepository<CustomerCenter, Long> {

    CustomerCenter findByCcId(Long ccId); //문의 선택

    @Query("select new com.golfzonTech4.worktalk.dto.customercenter.CustomerCenterDetailDto" +
            "(c.ccId, c.member.id, c.title, c.content, c.type, c.lastModifiedDate, cc.content, cc.lastModifiedDate) " +
            "from CustomerCenter c left join CustomerComment cc on c.ccId = cc.ccId left join c.member m on m.name = :name")
    List<CustomerCenterDetailDto> findccDtoListByMember(@Param("name") String name);//접속자의 1대1문의 내역 리스트

    @Query("select new com.golfzonTech4.worktalk.dto.customercenter.CustomerCenterDetailDto" +
            "(c.ccId, c.member.id, c.title, c.content, c.type, c.lastModifiedDate, cc.content, cc.lastModifiedDate) " +
            "from CustomerCenter c left join CustomerComment cc on c.ccId = cc.ccId")
    List<CustomerCenterDetailDto> findAllcc();//마스터용 전체 1대1문의 리스트
}
