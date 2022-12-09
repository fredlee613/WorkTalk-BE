package com.golfzonTech4.worktalk.repository.member;

import com.golfzonTech4.worktalk.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    Optional<Member> findByName(String name); // 회원 단건 조회(이름 기준)
    Optional<Member> findByEmail(String email); // 회원 단건 조회(이메일 기준)

}
