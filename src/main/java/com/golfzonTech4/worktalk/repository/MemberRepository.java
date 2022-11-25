package com.golfzonTech4.worktalk.repository;

import com.golfzonTech4.worktalk.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MemberRepository {

    private final EntityManager em;

    /**
     * 회원 가입 로직
     */
    public void save(Member member) {
        log.info("save: {}", member);
        em.persist(member);
    }

    /**
     * 단일 회원 조회 로직(회원 번호 기준)
     */
    public Member findOne(Member member) {
        log.info("findOne : {}", member);
        return em.find(Member.class, member.getId());
    }

    /**
     * 단일 회원 조회 로직(회원 이름 기준)
     */
    public Member findOneByName(String name) {
        log.info("findByName: {}", name);
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getSingleResult();
    }

    /**
     * 단일 회원 조회 로직(회원 이메일 기준)
     */
    public Member findOneByEmail(String email) {
        log.info("findOneByEmail: {}", email);
        return em.createQuery("select m from Member m where m.email = :email", Member.class)
                .setParameter("email", email)
                .getSingleResult();
    }
    
    /**
     * 회원 리스트 조회 (메일 기준)
     */
    public List<Member> findByName(String name) {
        log.info("findByName: {}", name);
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

    /**
     * 회원 리스트 조회 (메일 기준)
     */
    public List<Member> findByEmail(String email) {
        log.info("findByEmail: {}", email);
        return em.createQuery("select m from Member m where m.email = :email", Member.class)
                .setParameter("email", email)
                .getResultList();
    }

    /**
     * 전체 회원 조회
     */
    public List<Member> findAll() {
        log.info("findAll");
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
}
