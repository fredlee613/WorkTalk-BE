package com.golfzonTech4.worktalk.repository.penalty;

import com.golfzonTech4.worktalk.domain.Penalty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PenaltyRepository extends JpaRepository<Penalty, Long>, PenaltyRepositoryCustom{
}
