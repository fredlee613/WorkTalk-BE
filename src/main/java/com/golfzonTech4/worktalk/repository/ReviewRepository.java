package com.golfzonTech4.worktalk.repository;

import com.golfzonTech4.worktalk.domain.Qna;
import com.golfzonTech4.worktalk.domain.Review;
import com.golfzonTech4.worktalk.dto.qna.QnaDetailDto;
import com.golfzonTech4.worktalk.dto.review.ReviewDetailDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Review findByReviewId(Long reviewId); //review 선택

    //review, reservation, room, space 조인
    @Query("select distinct new com.golfzonTech4.worktalk.dto.review.ReviewDetailDto" +
            "(r.reviewId, re.reserveId, r.member.id, r.content, r.lastModifiedDate, r.grade) " +
            "from Review r left join Reservation re on r.reservation.reserveId = re.reserveId " +
            "left join re.room ro on re.room.roomId = ro.roomId " +
            "left join ro.space s on s.spaceId = :spaceId")
    List<ReviewDetailDto> findReviewsDtoListBySpaceId(@Param("spaceId") Long spaceId);//해당 사무공간의 후기 리스트

    //review, reservation, member 조인
    @Query("select distinct new com.golfzonTech4.worktalk.dto.review.ReviewDetailDto" +
            "(r.reviewId, re.reserveId, r.member.id, r.content, r.lastModifiedDate, r.grade) " +
            "from Review r left join Reservation re on r.reservation.reserveId = re.reserveId " +
            "left join r.member m on m.name = :name")
    List<ReviewDetailDto> findReviewsDtoListByMember(@Param("name") String name);//접속자의 후기 리스트

}