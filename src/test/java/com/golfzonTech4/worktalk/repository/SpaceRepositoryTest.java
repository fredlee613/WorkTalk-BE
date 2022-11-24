package com.golfzonTech4.worktalk.repository;

import com.golfzonTech4.worktalk.domain.Space;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SpaceRepositoryTest {

    @Autowired SpaceRepository spaceRepository;

    @Test
    @DisplayName("사무공간 등록 테스트")
    public void createSpaceTest(){
        Space space = new Space();
    }
}