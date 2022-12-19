package com.golfzonTech4.worktalk.repository.qna;

import com.golfzonTech4.worktalk.domain.Qna;
import com.golfzonTech4.worktalk.domain.QnaType;
import com.golfzonTech4.worktalk.dto.qna.QnaDetailDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QnaRepository extends JpaRepository<Qna, Long>,
        QuerydslPredicateExecutor<Qna>, QnaRepositoryCustom {

    Qna findByQnaId(Long qnaId); //QnA 선택

    @Query("select distinct new com.golfzonTech4.worktalk.dto.qna.QnaDetailDto" +
            "(q.qnaId, s.spaceId, q.member.id, q.type, q.content, q.lastModifiedDate, qc.qnaCommentId, qc.qnacomment, qc.lastModifiedDate) " +
            "from Qna q left join QnaComment qc on q.qnaId = qc.qna.qnaId left join q.space s on s.spaceId = q.space.spaceId where s.spaceId = :spaceId")
    List<QnaDetailDto> findQnaDtoListBySpaceId(@Param("spaceId") Long spaceId);//해당 사무공간의 QnA 리스트

    @Query("select new com.golfzonTech4.worktalk.dto.qna.QnaDetailDto" +
            "(q.qnaId, q.space.spaceId, q.member.id, q.type, q.content, q.lastModifiedDate, qc.qnaCommentId, qc.qnacomment, qc.lastModifiedDate, q.space.spaceName) " +
            "from Qna q left join QnaComment qc on q.qnaId = qc.qna.qnaId left join q.member m on m.name = q.member.name where m.name = :name")
    List<QnaDetailDto> findQnaDtoListByMember(@Param("name") String name);//접속자의 QnA 리스트


}
