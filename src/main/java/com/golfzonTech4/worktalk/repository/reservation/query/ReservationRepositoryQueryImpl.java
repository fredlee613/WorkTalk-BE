package com.golfzonTech4.worktalk.repository.reservation.query;

import com.golfzonTech4.worktalk.domain.*;
import com.golfzonTech4.worktalk.dto.reservation.ReserveSimpleDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ReservationRepositoryQueryImpl implements ReservationRepositoryQueryCustom {

    private final EntityManager em;

    @Override
    public PageImpl<ReserveSimpleDto> findAllByHost(
            String name, Integer paid, RoomType roomType,
            PaymentStatus paymentStatus, PageRequest pageRequest) {
        log.info("findAllByHost : {}, {}, {}, {}, {}", name, paid, roomType, paymentStatus, pageRequest);
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ReserveSimpleDto> cq = builder.createQuery(ReserveSimpleDto.class);
        Root<Reservation> r = cq.from(Reservation.class);

        Join<Reservation, Member> m = r.join("member");
//        Join<Reservation, BookDate> b = r.join("bookDate");
        Join<Reservation, Room> ro = r.join("room");
        Join<Room, Space> s = ro.join("space");
        
        // 파리미터 값에 대한 조건 분기
        ArrayList<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(s.get("member").get("name"), name));
        if (paid != null || paymentStatus != null) {
            predicates.add(builder.equal(r.get("paid"), paid));
            predicates.add(builder.equal(r.get("paymentStatus"), paymentStatus));
        }
        if (roomType != null) {
            predicates.add(builder.equal(ro.get("roomType"), roomType));
        }

        cq.multiselect(ro.get("roomName"), r.get("paid"), r.get("reserveId"), m.get("id"), ro.get("roomId"), r.get("bookDate"),
                m.get("name"), r.get("reserveStatus"), r.get("paymentStatus"), ro.get("roomType"), r.get("reserveAmount"))
                .where(predicates.toArray(new Predicate[0]))
                .orderBy(builder.desc(r.get("reserveId")));;

        TypedQuery<ReserveSimpleDto> query = em.createQuery(cq);
        List<ReserveSimpleDto> result = query.setFirstResult((int) pageRequest.getOffset())
                .setMaxResults(pageRequest.getPageSize() + 1)
                .getResultList();

        TypedQuery<ReserveSimpleDto> query1 = em.createQuery(cq);

        long total = query1
                .getResultList().size();

        log.info("size : {}", total);

        return new PageImpl<>(result, pageRequest, total);
    }

}
