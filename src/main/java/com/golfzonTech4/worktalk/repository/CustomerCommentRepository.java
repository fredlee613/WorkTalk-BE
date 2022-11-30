package com.golfzonTech4.worktalk.repository;

import com.golfzonTech4.worktalk.domain.CustomerComment;
import com.golfzonTech4.worktalk.domain.QnaComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerCommentRepository extends JpaRepository<CustomerComment, Long> {

    CustomerComment findByCcId(Long ccId);
}
