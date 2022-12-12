package com.golfzonTech4.worktalk.repository.space;

import com.golfzonTech4.worktalk.domain.QReservation;
import com.golfzonTech4.worktalk.domain.QSpace;
import com.golfzonTech4.worktalk.dto.space.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.golfzonTech4.worktalk.domain.QReservation.reservation;
import static com.golfzonTech4.worktalk.domain.QRoom.room;
import static com.golfzonTech4.worktalk.domain.QSpace.space;
import static com.golfzonTech4.worktalk.domain.QSpaceImg.spaceImg;

@Slf4j
public class SpaceRepositoryCustomImpl implements SpaceRepositoryCustom{

    private JPAQueryFactory queryFactory; // 동적 쿼리 생성 위한 클래스

    // JPAQueryFactory 생성자로 EntityManager 넣어줌
    public SpaceRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public PageImpl<SpaceMainDto> getMainSpacePage(PageRequest pageRequest, SpaceSearchDto dto) {

        QSpace space = QSpace.space;
//        QSpaceImg spaceImg = QSpaceImg.spaceImg;

        List<SpaceMainDto> content = queryFactory
                .select(
                        new QSpaceMainDto(
                                space.spaceId,
                                space.spaceName,
                                space.address,
                                space.detailAddress,
                                space.spaceType,
                                reservation.bookDate.checkInDate,
                                reservation.bookDate.checkOutDate,
                                reservation.bookDate.checkInTime,
                                reservation.bookDate.checkOutTime)
                )
                .from(space)
                .join(room).on(room.space.spaceId.eq(space.spaceId))
                .join(reservation).on(reservation.room.roomId.eq(room.roomId))
                .where(eqSpaceType(dto.getSearchSpaceType()), containName(dto.getSearchSpaceName()),
                        containAddress(dto.getSearchAddress()), space.spaceStatus.eq("approved"),
                        possibleDate(dto.getSearchSpaceType(), dto.getSearchStartDate(),
                                dto.getSearchEndDate(), dto.getSearchStartTime(), dto.getSearchEndTime()))
                .orderBy(space.spaceId.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        List<Long> spaceIds = content.stream().map(SpaceMainDto::getSpaceId).collect(Collectors.toList());

        List<SpaceImgDto> images = queryFactory.select(new QSpaceImgDto(spaceImg.spaceImgId, spaceImg.spaceImgUrl, spaceImg.space.spaceId))
                .from(spaceImg)
                .where(spaceImg.space.spaceId.in(spaceIds))
                .fetch();

        Map<Long, List<SpaceImgDto>> imgIdsMap = images.stream().collect(Collectors.groupingBy(SpaceImgDto::getSpaceId));

        content.forEach(s -> s.setSpaceImgList(imgIdsMap.get(s.getSpaceId())));

        long total = queryFactory
                .select(space.count())
                .from(space)
                .join(room).on(room.space.spaceId.eq(space.spaceId))
                .join(reservation).on(reservation.room.roomId.eq(room.roomId))
                .where(eqSpaceType(dto.getSearchSpaceType()), containName(dto.getSearchSpaceName()),
                        containAddress(dto.getSearchAddress()), space.spaceStatus.eq("approved"),
                        possibleDate(dto.getSearchSpaceType(), dto.getSearchStartDate(),
                                dto.getSearchEndDate(), dto.getSearchStartTime(), dto.getSearchEndTime()))
                .fetchOne();

        return new PageImpl<>(content, pageRequest, total);
    }

    private BooleanExpression eqSpaceType(Integer searchSpaceType) {
        if(searchSpaceType == null) {
            return null;
        }
        return space.spaceType.eq(searchSpaceType);
    }

    private BooleanExpression containName(String searchSpaceName) {
        if(searchSpaceName == null || searchSpaceName.isEmpty()) {
            return null;
        }
        return space.spaceName.containsIgnoreCase(searchSpaceName);
    }

    private BooleanExpression containAddress(String searchAddress) {
        if(searchAddress == null || searchAddress.isEmpty()) {
            return null;
        }
        return space.address.containsIgnoreCase(searchAddress);
    }

    private BooleanExpression possibleDate(Integer spaceType, LocalDate searchStartDate, LocalDate searchEndDate,
                                           Integer searchStartTime, Integer searchEndTime){

        if(searchStartDate == null){
            return null;
        }
        else if(spaceType == 1){
            return reservation.bookDate.checkInDate.notBetween(searchStartDate, searchEndDate)
                    .or(reservation.bookDate.checkOutDate.notBetween(searchStartDate, searchEndDate));
        }
        else {
            return reservation.bookDate.checkInDate.eq(searchStartDate)
                    .and(reservation.bookDate.checkInTime.notBetween(searchStartTime, searchEndTime))
                    .or(reservation.bookDate.checkOutTime.notBetween(searchStartTime, searchEndTime));
        }
    }


}
