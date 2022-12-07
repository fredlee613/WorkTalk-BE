package com.golfzonTech4.worktalk.repository.space;

import com.golfzonTech4.worktalk.domain.Space;
import com.golfzonTech4.worktalk.repository.space.SpaceRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface SpaceRepository extends JpaRepository<Space, Long>,
        QuerydslPredicateExecutor<Space>, SpaceRepositoryCustom {

    Space findBySpaceId(Long spaceId); //사무공간 상세페이지

//    @Query("select s from Space s where s.spaceStatus like 'approved'")
//    List<Space> findAllBySpaceStatus(); //메인페이지 사무공간리스트

    List<Space> findAllByMemberId(Long memberId); // 호스트가 등록한 사무공간리스트

//    save(): 레코드 저장
//    findOne(): PK로 레코드 한 건 찾기
//    findAll(): 전체 레코드 불러오기
//    count(): 레코드 갯수
//    delete(): 레코드 삭제
}
