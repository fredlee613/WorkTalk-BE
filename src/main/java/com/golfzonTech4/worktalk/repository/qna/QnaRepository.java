package com.golfzonTech4.worktalk.repository.qna;

import com.golfzonTech4.worktalk.domain.Qna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface QnaRepository extends JpaRepository<Qna, Long>,
        QuerydslPredicateExecutor<Qna>, QnaRepositoryCustom {

    Qna findByQnaId(Long qnaId); //QnA 선택

}
