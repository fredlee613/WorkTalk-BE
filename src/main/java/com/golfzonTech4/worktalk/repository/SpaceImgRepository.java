package com.golfzonTech4.worktalk.repository;

import com.golfzonTech4.worktalk.domain.Space;
import com.golfzonTech4.worktalk.domain.SpaceImgg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpaceImgRepository  extends JpaRepository<SpaceImgg, Long> {

    List<SpaceImgg> findBySpace(Space space);
}
