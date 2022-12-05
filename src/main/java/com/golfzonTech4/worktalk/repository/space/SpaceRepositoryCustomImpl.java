package com.golfzonTech4.worktalk.repository.space;

import com.golfzonTech4.worktalk.domain.QSpace;
import com.golfzonTech4.worktalk.domain.QSpaceImg;
import com.golfzonTech4.worktalk.domain.Space;
import com.golfzonTech4.worktalk.dto.space.QSpaceMainDto;
import com.golfzonTech4.worktalk.dto.space.SpaceMainDto;
import com.golfzonTech4.worktalk.dto.space.SpaceSearchDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;


import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static com.golfzonTech4.worktalk.domain.QSpace.space;
import static com.golfzonTech4.worktalk.domain.QSpaceImg.spaceImg;

public class SpaceRepositoryCustomImpl implements SpaceRepositoryCustom{

    private JPAQueryFactory queryFactory; // 동적 쿼리 생성 위한 클래스

    // JPAQueryFactory 생성자로 EntityManager 넣어줌
    public SpaceRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Space> getAdminSpacePage(SpaceSearchDto spaceSearchDto, Pageable pageable) {
        return null;
    }

    @Override
    public List<SpaceMainDto> getMainPage(Integer spaceType, String spaceName, String address) {
        return null;
//        return queryFactory
//                .select(
//                        new QSpaceMainDto(
//                                space.spaceId,
//                                space.spaceName,
//                                space.address,
//                                space.spaceType,
//                                spaceImg.imgName)
//                )
//                .from(spaceImg)
//                .join(space.spaceImg, space)
//                .where(eqSpaceType(spaceType), containName(spaceName), containAddress(address), space.spaceStatus.eq("approved"))
//                .orderBy(space.spaceId.desc())
//                .fetch();
    }


    @Override
    public Page<SpaceMainDto> getMainSpacePage(Pageable pageable, Integer spaceType, String spaceName, String address) {
        return null;
//        QSpace space = QSpace.space;
//        QSpaceImg spaceImg = QSpaceImg.spaceImg;
//
//        List<SpaceMainDto> content = queryFactory
//                .select(
//                        new QSpaceMainDto(
//                                space.spaceId,
//                                space.spaceName,
//                                space.address,
//                                space.spaceType,
//                                spaceImg.imgName)
//                )
//                .from(spaceImg)
//                .join(spaceImg.space, space)
//                .where(eqSpaceType(spaceType), containName(spaceName), containAddress(address))
//                .orderBy(space.spaceId.desc())
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        long total = queryFactory
//                .select(space.count())
//                .from(spaceImg)
//                .join(spaceImg.space, space)
//                .where(eqSpaceType(spaceType), containName(spaceName), containAddress(address))
//                .fetchOne();
//
//        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression eqSpaceType(Integer spaceType) {
        if(spaceType == null) {
            return null;
        }
        return space.spaceType.eq(spaceType);
    }

    private BooleanExpression containName(String spaceName) {
        if(spaceName == null || spaceName.isEmpty()) {
            return null;
        }
        return space.spaceName.containsIgnoreCase(spaceName);
    }

    private BooleanExpression containAddress(String address) {
        if(address == null || address.isEmpty()) {
            return null;
        }
        return space.address.containsIgnoreCase(address);
    }


}
