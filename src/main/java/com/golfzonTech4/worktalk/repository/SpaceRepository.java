package com.golfzonTech4.worktalk.repository;

import com.golfzonTech4.worktalk.domain.Member;
import com.golfzonTech4.worktalk.domain.Space;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository

public interface SpaceRepository extends JpaRepository<Space, Long> {

    Space findBySpaceId(Long spaceId); //사무공간 상세페이지

//    List<Space> findAllByMember(Member member); //호스트가 등록한 사무공간리스트

    List<Space> findAllByMemberId(Long memberId); // 호스트가 등록한 사무공간리스트

//    save(): 레코드 저장
//    findOne(): PK로 레코드 한 건 찾기
//    findAll(): 전체 레코드 불러오기
//    count(): 레코드 갯수
//    delete(): 레코드 삭제
}
