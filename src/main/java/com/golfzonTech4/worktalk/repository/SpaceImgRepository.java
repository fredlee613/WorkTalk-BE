package com.golfzonTech4.worktalk.repository;

import com.golfzonTech4.worktalk.domain.SpaceImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpaceImgRepository extends JpaRepository<SpaceImg, Long> {

    List<SpaceImg> findBySpace(Long space);
}
