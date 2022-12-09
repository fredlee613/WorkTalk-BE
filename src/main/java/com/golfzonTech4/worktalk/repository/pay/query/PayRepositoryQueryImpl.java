package com.golfzonTech4.worktalk.repository.pay.query;

import com.golfzonTech4.worktalk.domain.*;
import com.golfzonTech4.worktalk.dto.pay.PaySimpleDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class PayRepositoryQueryImpl implements PayRepositoryQueryCustom{
    private final EntityManager em;


    @Override
    public PageImpl<PaySimpleDto> findAllByUser(String name, LocalDateTime time, PaymentStatus payStatus, PageRequest pageRequest) {
        log.info("findAllByUser : {}, {}, {}, {}", name, time, payStatus, pageRequest);
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<PaySimpleDto> cq = builder.createQuery(PaySimpleDto.class);
        Root<Pay> p = cq.from(Pay.class);

        Join<Pay, Reservation> r = p.join("reservation");
        Join<Reservation, Member> m = r.join("member");
        Join<Reservation, Room> ro = r.join("room");
        Join<Room, Space> s = ro.join("space");

        // 파리미터 값에 대한 조건 분기
        ArrayList<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(m.get("name"), name));
        if (time != null) {
            predicates.add(builder.greaterThanOrEqualTo(r.get("bookDate").get("reserveDate"), time));
        }
        if (payStatus != null) {
            predicates.add(builder.equal(p.get("payStatus"), payStatus));
        }

        cq.multiselect(r.get("bookDate").get("reserveDate"), s.get("spaceName"), ro.get("roomName"),
                        p.get("payAmount"), p.get("payStatus"), r.get("reserveStatus"), m.get("name"), r.get("reserveAmount"))
                .where(predicates.toArray(new Predicate[0]))
                .orderBy(builder.desc(r.get("reserveId")));

        TypedQuery<PaySimpleDto> query = em.createQuery(cq);
        List<PaySimpleDto> result = query.setFirstResult((int) pageRequest.getOffset())
                .setMaxResults(pageRequest.getPageSize() + 1)
                .getResultList();

        TypedQuery<PaySimpleDto> query1 = em.createQuery(cq);

        long total = query1
                .getResultList().size();

        log.info("size : {}", total);

        return new PageImpl<>(result, pageRequest, total);
    }

    @Override
    public PageImpl<PaySimpleDto> findAllByHost(String name, LocalDateTime time, PageRequest pageRequest) {
        log.info("findAllByUser : {}, {}, {}", name, time, pageRequest);
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<PaySimpleDto> cq = builder.createQuery(PaySimpleDto.class);
        Root<Pay> p = cq.from(Pay.class);

        Join<Pay, Reservation> r = p.join("reservation");
        Join<Reservation, Member> m = r.join("member");
        Join<Reservation, Room> ro = r.join("room");
        Join<Room, Space> s = ro.join("space");

        // 파리미터 값에 대한 조건 분기
        ArrayList<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(s.get("member").get("name"), name));
        if (time != null) {
            predicates.add(builder.greaterThanOrEqualTo(r.get("bookDate").get("reserveDate"), time));
        }
        cq.multiselect(r.get("bookDate").get("reserveDate"), s.get("spaceName"), ro.get("roomName"),
                        p.get("payAmount"), p.get("payStatus"), r.get("reserveStatus"), m.get("name"), m.get("tel"), r.get("reserveAmount"))
                .where(predicates.toArray(new Predicate[0]))
                .orderBy(builder.desc(r.get("reserveId")));

        TypedQuery<PaySimpleDto> query = em.createQuery(cq);
        List<PaySimpleDto> result = query.setFirstResult((int) pageRequest.getOffset())
                .setMaxResults(pageRequest.getPageSize() + 1)
                .getResultList();

        TypedQuery<PaySimpleDto> query1 = em.createQuery(cq);

        long total = query1
                .getResultList().size();

        log.info("size : {}", total);

        return new PageImpl<>(result, pageRequest, total);
    }
}
