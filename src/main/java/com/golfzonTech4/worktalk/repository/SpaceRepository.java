package com.golfzonTech4.worktalk.repository;

import com.golfzonTech4.worktalk.domain.Space;
import com.golfzonTech4.worktalk.dto.space.SpaceMainDto;
import com.golfzonTech4.worktalk.repository.space.SpaceRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface SpaceRepository extends JpaRepository<Space, Long>,
        QuerydslPredicateExecutor<Space>, SpaceRepositoryCustom {

    Space findBySpaceId(Long spaceId); //사무공간 상세페이지

//    @Query("select distinct new com.golfzonTech4.worktalk.dto.space.SpaceMainDto (s.spaceId, s.spaceName, s.address, s.spaceType, img.imgName) from Space s left join SpaceImg img on s.spaceId = img.space.spaceId where s.spaceStatus like 'approved'")
    @Query(value = "select distinct new com.golfzonTech4.worktalk.dto.space.SpaceMainDto (s.space_id, s.space_name, s.address, s.space_type, img.img_name) from space s left join space_img img on s.space_id = img.space_id where s.space_status like 'approved'", nativeQuery = true)
    List<SpaceMainDto> findAllBySpaceStatus(); //메인페이지 사무공간리스트

    List<Space> findAllByMemberId(Long memberId); // 호스트가 등록한 사무공간리스트

//    save(): 레코드 저장
//    findOne(): PK로 레코드 한 건 찾기
//    findAll(): 전체 레코드 불러오기
//    count(): 레코드 갯수
//    delete(): 레코드 삭제
}
