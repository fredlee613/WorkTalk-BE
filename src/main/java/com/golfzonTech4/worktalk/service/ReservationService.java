package com.golfzonTech4.worktalk.service;

import com.golfzonTech4.worktalk.domain.*;
import com.golfzonTech4.worktalk.dto.penalty.PenaltyDto;
import com.golfzonTech4.worktalk.dto.reservation.ReserveOrderSearch;
import com.golfzonTech4.worktalk.dto.reservation.ReserveCheckDto;
import com.golfzonTech4.worktalk.dto.reservation.ReserveDto;
import com.golfzonTech4.worktalk.dto.reservation.ReserveSimpleDto;
import com.golfzonTech4.worktalk.exception.NotFoundMemberException;
import com.golfzonTech4.worktalk.repository.ListResult;
import com.golfzonTech4.worktalk.repository.RoomRepository;
import com.golfzonTech4.worktalk.repository.reservation.ReservationRepository;
import com.golfzonTech4.worktalk.repository.reservation.ReservationSimpleRepository;
import com.golfzonTech4.worktalk.repository.reservation.query.ReservationRepositoryQuery;
import com.golfzonTech4.worktalk.util.SecurityUtil;
import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ReservationService {

    private final MemberService memberService;
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationSimpleRepository reservationSimpleRepository;
    private final PenaltyService penaltyService;
    private final ReservationRepositoryQuery reservationRepositoryQuery;

    /**
     * 예약 기능
     */
    @Transactional
    public Long reserve(ReserveDto dto) {
        log.info("reserve : {}", dto);

        Optional<String> currentUsername = SecurityUtil.getCurrentUsername();
        // 로그인 값이 없을 경우 예외처리
        if (currentUsername.isEmpty()) throw new NotFoundMemberException("Member not found");

        Member findMember = memberService.findByName(currentUsername.get());
        log.info("findMember : {}", findMember.toString());

        Room findRoom = roomRepository.findByRoomId(dto.getRoom_id());
        log.info("findRoom : {}", findRoom.toString());

        // 오피스의 경우 체크인 일자가 체크아웃 일자보다 늦을 경우 예외처리
        // 그 외의 경우 체크인 시간이 체크아웃 시간보다 늦을 경우 예외처리
        validateDateTime(dto, findRoom);

        BookDate bookDate = new BookDate(
                LocalDateTime.now(),
                dto.getCheckInDate(),
                dto.getCheckOutDate(),
                dto.getCheckInTime(),
                dto.getCheckOutTime());

        log.info("bookDate: {}", bookDate);

        Reservation reservation = Reservation.makeReservation(findMember, findRoom, bookDate, dto.getAmount(), dto.getPaymentStatus());

        return reservationRepository.save(reservation).getReserveId();
    }

    /**
     * 예약 시간 관련 시간 검증
     */
    private static void validateDateTime(ReserveDto reserveDto, Room room) {
        if (room.getRoomType().equals(RoomType.OFFICE)) {
            // 오피스의 경우 날짜 비교
            if (!BookDate.validDate(reserveDto.getCheckInDate(), reserveDto.getCheckOutDate())) {
                throw new IllegalArgumentException("잘못된 날짜값 입력입니다.");
            }
        } else {
            // 그 외의 경우 시간 비교
            if (!BookDate.validTime(reserveDto.getCheckInTime(), reserveDto.getCheckOutTime())) {
                throw new IllegalArgumentException("잘못된 시간값 입력입니다.");
            }
        }
    }

    /**
     * 사용자 예약 취소 기능
     */
    @Transactional
    public Map<String, Object> cancelByUser(Long reserveId, String cancelReason) throws IamportResponseException, IOException {
        log.info("cancelByUser : {}, {}", reserveId, cancelReason);

        // 해당 값이 Null일 경우 NoSuchElementException 발생
        Reservation findReservation = reservationRepository.findById(reserveId).get();

        LocalDate checkInDate = findReservation.getBookDate().getCheckInDate();
        LocalDateTime reserveDate = findReservation.getBookDate().getReserveDate();
        Integer checkInTime = findReservation.getBookDate().getCheckInTime();
        LocalDateTime initTime = BookDate.getInitTime(checkInDate, checkInTime);

        int periodMinutes = BookDate.getPeriodMinutes(LocalDateTime.now(), initTime);
        log.info("periodMinutes : {}", periodMinutes);

        // 사용 시간 전부터는 예약 취소 불가
        if (periodMinutes < 60) {
            throw new IllegalStateException("취소가 가능한 시간을 지났습니다.");
        }

        findReservation.getBookDate().setCancelDate(LocalDateTime.now());
        findReservation.setReserveStatus(ReserveStatus.CANCELED_BY_USER);
        findReservation.setCancelReason(cancelReason);

        // 예약 시간으로 부터 현재까지의 시간 차(초 기준)
        int reservePeriod = BookDate.getPeriodSeconds(LocalDateTime.now(), reserveDate);

        log.info("reservePeriod : {}", reservePeriod);

        int flag = reservePeriod <= 3600 ? 0 : 1; // 1시간 이하일 경우 0, 초과 시 1

        log.info("before map put >> {} : {} : {}", reserveId, flag, findReservation.getPaymentStatus());

        Map result = new HashMap<>();
        result.put("reserveId", reserveId);
        result.put("flag", flag);
        result.put("status", findReservation.getPaymentStatus().toString());
        return result;
    }

    /**
     * 호스트는 예약 취소 기능
     */
    @Transactional
    public Map<String, Object> cancelByHost(Long reserveId, String cancelReason) throws IamportResponseException, IOException {
        log.info("cancelByHost : {}, {}", reserveId, cancelReason);
        // 해당 값이 Null일 경우 NoSuchElementException 발생
        Reservation findReservation = reservationRepository.findById(reserveId).get();

        findReservation.getBookDate().setCancelDate(LocalDateTime.now());
        findReservation.setReserveStatus(ReserveStatus.CANCELED_BY_HOST);
        findReservation.setCancelReason(cancelReason);

        Map result = new HashMap<>();
        result.put("reserveId", reserveId);
        String status = findReservation.getPaymentStatus().toString();
        log.info("status : {}", status);
        result.put("status", status);
        return result;
    }

    /**
     * 매 시 30분마다 미결제된 예약을 조회 후 NOSHOW로 수정
     * NOSHOW처리된 예약자의 NOSHOW 이력 조회
     * 해당 이력건이 3회 이상일 경우 페널티 부여
     */
    @Scheduled(cron = "30 * * * *")
    @Transactional
    public int updateNoShow() {
        log.info("updateNoShow");
        log.info("current time : {}", LocalDateTime.now());
        List<ReserveSimpleDto> reservaionList = findAllByTime();
//        List<Long> memberList = new ArrayList<>();
        Set<Long> memberSet = new HashSet<>();
        for (ReserveSimpleDto reserveSimpleDto : reservaionList) {
            changeToNoShow(reserveSimpleDto.getReserveId());
            memberSet.add(reserveSimpleDto.getMemberId());
            log.info("memberSet.size() : {}", memberSet.size());
        }
        int count = 0;
        if (memberSet.size() != 0) {
            for (Long memberId : memberSet) {
                log.info("memberId: {}", memberId);
                Long noShowCount = countNoShow(memberId);
                if (noShowCount >= 3) {
                    PenaltyDto dto = new PenaltyDto(memberId, "지속적인 비결제로 인한 페널티 부여", PenaltyType.NOSHOW);
                    Long result = penaltyService.addPenalty(dto);
                    if (result != 0) count++;
                }
                log.info("memberName's count: {}", noShowCount);
            }
            return count;
        }
        return count;
    }

    /**
     * 1시간 내의 미결제 예약 내역을 조회
     */
    public List<ReserveSimpleDto> findAllByTime() {
        log.info("findAllByTime");
        List<ReserveSimpleDto> result = reservationSimpleRepository.findAllByTime();
        for (ReserveSimpleDto reserveSimpleDto : result) {
            log.info("reserveSimpleDto : {}", reserveSimpleDto);
        }
        return result;
    }


    /**
     * 결제 생태가 미결제인 예약 건에 대하여 예약 상태를 NOSHOW로 변경
     */
    @Transactional
    public void changeToNoShow(Long reservedId) {
        log.info("changeToNoShow : {}", reservedId);
        Reservation findReserve = reservationRepository.findById(reservedId).get();
        if (findReserve.getPaid() == 0) {
            findReserve.setReserveStatus(ReserveStatus.NOSHOW);
        }
    }

    /**
     * 해당 회원의 예약 Noshow 카운트 조회
     */
    public Long countNoShow(Long memberId) {
        log.info("countNoShow : {}", memberId);
        return reservationSimpleRepository.countNoShow(memberId, ReserveStatus.NOSHOW);
    }


    /**
     * 접속자(USER/HOST)명을 기준으로 예약 리스트 페이징 조회(QueryDsl + JPQL/Criteria)
     */
    public ListResult findAllByName(PageRequest pageRequest, ReserveOrderSearch dto) {
        log.info("findAllByName: {}, {}", pageRequest, dto);
        String name = SecurityUtil.getCurrentUsername().get();
        String role = SecurityUtil.getCurrentUserRole().get();
        if (role.equals(MemberType.ROLE_USER)) {
            PageImpl<ReserveSimpleDto> result = reservationSimpleRepository.findAllByUserPage(name, pageRequest, dto.getPaid(), dto.getPaymentStatus());
            return new ListResult<>((long)result.getSize(), result);

        } else {
            PageImpl<ReserveSimpleDto> result = reservationRepositoryQuery.findAllByHost(name, dto.getPaid(), dto.getRoomType(), dto.getPaymentStatus(), pageRequest);
            return new ListResult<>((long)result.getSize(), result);
        }
    }


    /**
     * 접속자(USER)명을 기준으로 예약 리스트 페이징 조회(QueryDsl)
     */
    public ListResult findAllByUserPage(PageRequest pageRequest, ReserveOrderSearch reserveOrderSearch) {
        String name = SecurityUtil.getCurrentUsername().get();
        log.info("findAllByUser : {}", name);
        PageImpl<ReserveSimpleDto> result = reservationSimpleRepository.findAllByUserPage(name, pageRequest, reserveOrderSearch.getPaid(), reserveOrderSearch.getPaymentStatus());
        return new ListResult(result.getTotalElements(), result.getContent());
    }

    /**
     * 선택 일자를 기준으로 예약 리스트 조회
     * 오피스의 경우 일별로 조회
     * 그 외의 사무공간의 경우 시간별로 조회
     */
    public List<ReserveCheckDto> findBookedReservation(ReserveCheckDto dto) {
        log.info("findBookedReservation : {}", dto);
        if (dto.getRoomType().equals(RoomType.OFFICE)) {
            return reservationSimpleRepository.findBookedOffice(dto.getRoomId(), dto.getInitDate(), dto.getEndDate());
        } else {
            return reservationSimpleRepository.findBookedRoom(dto.getRoomId(), dto.getInitDate(), dto.getInitTime(), dto.getEndTime());
        }
    }

    /**
     * 호스트가 관리하는 공간 들에 대한 예약 리스트 페이지 조회
     */
    public ListResult findAllByHostPage(ReserveOrderSearch reserveOrderSearch, PageRequest pageRequest) {
        String currentUsername = SecurityUtil.getCurrentUsername().get();
        log.info("findAllByHost : {}, {}, {}", currentUsername, reserveOrderSearch, pageRequest);
        if (reserveOrderSearch.getRoomType() != null) {
            if (reserveOrderSearch.getPaymentStatus() != null && reserveOrderSearch.getPaid() != null) {
                log.info("findAllByHostPageBoth");
                Page<ReserveSimpleDto> result = reservationSimpleRepository.findAllByHostPageBoth(currentUsername, reserveOrderSearch.getPaid(), reserveOrderSearch.getRoomType(), reserveOrderSearch.getPaymentStatus(), pageRequest);
                return new ListResult(result.getTotalElements(), result.getContent());

            } else {
                log.info("findAllByHostPageRoom");
                Page<ReserveSimpleDto> result = reservationSimpleRepository.findAllByHostPageRoom(currentUsername, reserveOrderSearch.getRoomType(), pageRequest);
                return new ListResult(result.getTotalElements(), result.getContent());
            }
        } else {
            if (reserveOrderSearch.getPaymentStatus() != null && reserveOrderSearch.getPaid() != null) {
                log.info("findAllByHostPagePaid");
                Page<ReserveSimpleDto> result = reservationSimpleRepository.findAllByHostPagePaid(currentUsername, reserveOrderSearch.getPaid(), reserveOrderSearch.getPaymentStatus(), pageRequest);
                return new ListResult(result.getTotalElements(), result.getContent());
            }
        }
        log.info("findAllByHostPage");
        Page<ReserveSimpleDto> result = reservationSimpleRepository.findAllByHostPage(currentUsername, pageRequest);
        return new ListResult(result.getTotalElements(), result.getContent());
    }

    public Optional<Reservation> findById(Long reserveId) {
        return reservationRepository.findById(reserveId);
    }

    /**
     * 예약 이용 완료 처리 로직
     */
    @Transactional(rollbackFor = Exception.class)
    public void end(Long reserveId) {
        Reservation findReservation = reservationRepository.findById(reserveId).get();
        findReservation.setReserveStatus(ReserveStatus.USED);
    }
}
