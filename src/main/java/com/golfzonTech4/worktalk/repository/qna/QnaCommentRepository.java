package com.golfzonTech4.worktalk.repository.qna;

import com.golfzonTech4.worktalk.domain.Qna;
import com.golfzonTech4.worktalk.domain.QnaComment;
import com.golfzonTech4.worktalk.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QnaCommentRepository extends JpaRepository<QnaComment, Long> {

    QnaComment findByQnaCommentId(Long qnaCommentId); //QnA 선택
}
