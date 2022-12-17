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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.golfzonTech4.worktalk.domain.QReservation.reservation;
import static com.golfzonTech4.worktalk.domain.QRoom.room;
import static com.golfzonTech4.worktalk.domain.QSpaceImg.spaceImg;

@Slf4j
public class SpaceRepositoryCustomImpl implements SpaceRepositoryCustom{

    private JPAQueryFactory queryFactory; // 동적 쿼리 생성 위한 클래스

    // JPAQueryFactory 생성자로 EntityManager 넣어줌
    public SpaceRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }
    QSpace space = QSpace.space;

    @Override
    public PageImpl<SpaceMainDto> getMainSpacePage(PageRequest pageRequest, SpaceSearchDto dto) {

        List<SpaceMainDto> content = queryFactory
                .select(
                        new QSpaceMainDto(
                                space.spaceId,
                                space.spaceName,
                                space.address,
                                space.detailAddress,
                                space.spaceType)
                ).distinct()
                .from(space)
                .leftJoin(room).on(room.space.spaceId.eq(space.spaceId))
                .leftJoin(reservation).on(reservation.room.roomId.eq(room.roomId).and(possibleDate(dto.getSearchSpaceType(), dto.getSearchStartDate(),
                        dto.getSearchEndDate(), dto.getSearchStartTime(), dto.getSearchEndTime())))
                .where(eqSpaceType(dto.getSearchSpaceType()), containName(dto.getSearchSpaceName()),
                        containAddress(dto.getSearchAddress()), space.spaceStatus.eq("approved"), ifsearchDate(dto.getSearchStartDate()))
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
                .select(space.countDistinct())
                .from(space)
                .leftJoin(room).on(room.space.spaceId.eq(space.spaceId))
                .leftJoin(reservation).on(reservation.room.roomId.eq(room.roomId).and(possibleDate(dto.getSearchSpaceType(), dto.getSearchStartDate(),
                        dto.getSearchEndDate(), dto.getSearchStartTime(), dto.getSearchEndTime())))
                .where(eqSpaceType(dto.getSearchSpaceType()), containName(dto.getSearchSpaceName()),
                        containAddress(dto.getSearchAddress()), space.spaceStatus.eq("approved"), ifsearchDate(dto.getSearchStartDate()))
                .fetchOne();

        return new PageImpl<>(content, pageRequest, total);
    }

    @Override
    public List<SpaceMainDto> getHostSpacePage(String name) {
        List<SpaceMainDto> content = queryFactory
                .select(
                        new QSpaceMainDto(
                                space.spaceId,
                                space.spaceName,
                                space.address,
                                space.detailAddress,
                                space.spaceType,
                                space.spaceStatus)
                ).distinct()
                .from(space)
                .where(space.member.name.eq(name))
                .orderBy(space.spaceId.desc())
                .fetch();

        List<Long> spaceIds = content.stream().map(SpaceMainDto::getSpaceId).collect(Collectors.toList());
        List<SpaceImgDto> images = queryFactory.select(new QSpaceImgDto(spaceImg.spaceImgId, spaceImg.spaceImgUrl, spaceImg.space.spaceId))
                .from(spaceImg)
                .where(spaceImg.space.spaceId.in(spaceIds))
                .fetch();
        Map<Long, List<SpaceImgDto>> imgIdsMap = images.stream().collect(Collectors.groupingBy(SpaceImgDto::getSpaceId));
        content.forEach(s -> s.setSpaceImgList(imgIdsMap.get(s.getSpaceId())));

        return content;
    }

    @Override
    public List<SpaceDetailDto> getSpaceDetailPage(Long spaceId) {

        List<SpaceDetailDto> content = queryFactory.select(
                        new QSpaceDetailDto(
                                space.member.id,
                                space.spaceId,
                                space.member.name,
                                space.member.email,
                                space.member.tel,
                                space.spaceName,
                                space.spaceDetail,
                                space.postcode,
                                space.address,
                                space.detailAddress,
                                space.regCode,
                                space.spaceType))
                .from(space)
                .where(space.spaceId.eq(spaceId))
                .fetch();

        List<Long> spaceIds = content.stream().map(SpaceDetailDto::getSpaceId).collect(Collectors.toList());

        List<SpaceImgDto> images = queryFactory.select(new QSpaceImgDto(spaceImg.spaceImgId, spaceImg.spaceImgUrl, spaceImg.space.spaceId))
                .from(spaceImg)
                .where(spaceImg.space.spaceId.in(spaceIds))
                .fetch();

        Map<Long, List<SpaceImgDto>> imgIdsMap = images.stream().collect(Collectors.groupingBy(SpaceImgDto::getSpaceId));

        content.forEach(s -> s.setSpaceImgList(imgIdsMap.get(s.getSpaceId())));

        return content;
    }

    @Override
    public PageImpl<SpaceMasterDto> getSpaceMasterPage(PageRequest pageRequest, SpaceManageSortingDto dto) {
        List<SpaceMasterDto> content =  queryFactory.select(
                        new QSpaceMasterDto(
                                space.spaceId,
                                space.member.name,
                                space.spaceName,
                                space.spaceType,
                                space.regCode,
                                space.spaceStatus
                        ))
                .from(space)
                .where(eqSpaceType(dto.getSearchSpaceType()), eqSpaceStatus(dto.getSearchSpaceStatus()))
                .orderBy(space.spaceId.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        long total = queryFactory
                .select(space.count())
                .from(space)
                .where(eqSpaceType(dto.getSearchSpaceType()), eqSpaceStatus(dto.getSearchSpaceStatus()))
                .fetchOne();

        return new PageImpl<>(content, pageRequest, total);

    }

    private BooleanExpression eqSpaceStatus(String searchSpaceStatus) {
        if(searchSpaceStatus == null || searchSpaceStatus.isEmpty()) {
            return null;
        }
        return space.spaceStatus.eq(searchSpaceStatus);
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
            return reservation.bookDate.checkOutDate.goe(searchStartDate)
                    .and(reservation.bookDate.checkInDate.loe(searchEndDate));
        }
        else {
            return reservation.bookDate.checkInDate.eq(searchStartDate)
                    .and(reservation.bookDate.checkOutTime.goe(searchStartTime))
                            .and(reservation.bookDate.checkInTime.loe(searchEndTime));
        }

    }

    //날짜검색을 할때만 필요한 조건
    private BooleanExpression ifsearchDate(LocalDate searchStartDate){
        if(searchStartDate == null) {
            return null;
        }
        return reservation.reserveId.isNull();
    }


}
