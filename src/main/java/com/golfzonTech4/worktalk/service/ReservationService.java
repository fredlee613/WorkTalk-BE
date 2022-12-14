package com.golfzonTech4.worktalk.service;

import com.golfzonTech4.worktalk.domain.*;
import com.golfzonTech4.worktalk.dto.pay.PayInsertDto;
import com.golfzonTech4.worktalk.dto.penalty.PenaltyDto;
import com.golfzonTech4.worktalk.dto.reservation.ReserveOrderSearch;
import com.golfzonTech4.worktalk.dto.reservation.ReserveCheckDto;
import com.golfzonTech4.worktalk.dto.reservation.ReserveSimpleDto;
import com.golfzonTech4.worktalk.exception.NotFoundMemberException;
import com.golfzonTech4.worktalk.repository.ListResult;
import com.golfzonTech4.worktalk.repository.room.RoomRepository;
import com.golfzonTech4.worktalk.repository.member.MemberRepository;
import com.golfzonTech4.worktalk.repository.reservation.ReservationRepository;
import com.golfzonTech4.worktalk.repository.reservation.ReservationSimpleRepository;
import com.golfzonTech4.worktalk.repository.reservation.query.ReservationRepositoryQuery;
import com.golfzonTech4.worktalk.repository.reservation.temp.TempReservationRepository;
import com.golfzonTech4.worktalk.util.SecurityUtil;
import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ReservationService {

    private final MemberRepository memberRepository;
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationSimpleRepository reservationSimpleRepository;
    private final PenaltyService penaltyService;
    private final TempReservationRepository tempReservationRepository;
    private final PayService payService;

    /**
     * 예약 기능
     */
    @Transactional(rollbackFor = {Exception.class})
    public Long reserve(PayInsertDto payDto) throws IamportResponseException, IOException, IllegalAccessException {
        log.info("reserve : {}", payDto);

        TempReservation temp = tempReservationRepository.findById(payDto.getReserveId()).get();
        log.info("tempReservation : {}", temp);

        Room findRoom = roomRepository.findByRoomId(temp.getRoomId());
        log.info("findRoom : {}", findRoom.toString());

        // 예약 중복 검증
        checkReservations(findRoom, temp); // 이미 확정된 예약건이 있을 경우 예외 발생

        Optional<String> currentUsername = SecurityUtil.getCurrentUsername();
        // 로그인 값이 없을 경우 예외처리
        if (currentUsername.isEmpty()) throw new NotFoundMemberException("Member not found");

        Member findMember = memberRepository.findByName(currentUsername.get()).get();
        // 노쇼로 이용이 제한된 사용자일 경우 예외 처리
        if (findMember.getActivated() == 0) {
            throw new IllegalAccessException("이용이 제한된 계정입니다.");
        }
        log.info("findMember : {}", findMember.toString());



        // 오피스의 경우 체크인 일자가 체크아웃 일자보다 늦을 경우 예외처리
        // 그 외의 경우 체크인 시간이 체크아웃 시간보다 늦을 경우 예외처리
        log.info("validating time....");
        validateDateTime(findRoom.getRoomType(), temp.getBookDate());

        // 가격 검증
        if (!validAmount(payDto.getReserveAmount(), findRoom, temp.getBookDate(), payDto.getMileageUsage())) {
            throw new IllegalArgumentException("잘못된 가격값입니다.");
        }

        PaymentStatus paymentStatus;
        if (payDto.getPayStatus() == PaymentStatus.DEPOSIT || payDto.getPayStatus() == PaymentStatus.PREPAID) {
            paymentStatus = PaymentStatus.PREPAID;
        } else {
            paymentStatus = PaymentStatus.POSTPAID;
        }
        Reservation reservation = Reservation.makeReservation(
                findMember, findRoom, temp.getBookDate(), payDto.getReserveAmount(), paymentStatus);

        Reservation result = reservationRepository.save(reservation);
        payDto.setReserveId(result.getReserveId());

        if (payDto.getPayStatus() == PaymentStatus.DEPOSIT) {  // 결제 유형에 따른 분기: 1. 선결제 중 보증금만 결제
            log.info("prepaid....");
            payService.prepaid(payDto);
        } else if (payDto.getPayStatus() == PaymentStatus.PREPAID) { // 결제 유형에 따른 분기: 2. 선결제 중 전액 결제
            log.info("prepaid....");
            payService.prepaid(payDto);
            log.info("setPaid to 1....");
            result.setPaid(1);
        } else { // 결제 유형에 따른 분기: 3. 후결제 (보증금 결제 후 예약)
            log.info("schedule....");
            payService.schedule(payDto);
        }
        return result.getReserveId();
    }

    /**
     * 예약 시간 관련 시간 검증
     */
    private static void validateDateTime(RoomType roomType, BookDate bookDate) {
        log.info("validateDateTime.... {}, {}",roomType, bookDate);
        if (roomType.equals(RoomType.OFFICE)) {
            // 오피스의 경우 날짜 비교
            if (!BookDate.validDate(bookDate.getCheckInDate(), bookDate.getCheckOutDate())) {
                log.info("result : {}, {} .... failed!!", bookDate.getCheckInDate(), bookDate.getCheckOutDate());
                throw new IllegalArgumentException("잘못된 날짜값 입력입니다.");
            }
        } else {
            // 그 외의 경우 시간 비교
            if (!BookDate.validTime(bookDate.getCheckInTime(), bookDate.getCheckOutTime())) {
                throw new IllegalArgumentException("잘못된 시간값 입력입니다.");
            }
        }
    }

    /**
     * 사용자 예약 취소 기능
     */
    @Transactional(rollbackFor = {Exception.class})
    public int cancelByUser(Long reserveId, String cancelReason) throws IamportResponseException, IOException {
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

        // 결제 취소 로직
        int flag = reservePeriod <= 3600 ? 0 : 1; // 1시간 이하일 경우 0, 초과 시 1
        int count = 0;
        if (findReservation.getPaymentStatus().equals(PaymentStatus.PREPAID)) {
            count = payService.cancelPrepaid(findReservation.getReserveId(), flag);
        } else {
            count = payService.cancelPostPaid(findReservation.getReserveId(), flag);
        }
        return count;
    }

    /**
     * 호스트는 예약 취소 기능
     */
    @Transactional(rollbackFor = {Exception.class})
    public int cancelByHost(Long reserveId, String cancelReason) throws IamportResponseException, IOException {
        log.info("cancelByHost : {}, {}", reserveId, cancelReason);
        // 해당 값이 Null일 경우 NoSuchElementException 발생
        Reservation findReservation = reservationRepository.findById(reserveId).get();

        findReservation.getBookDate().setCancelDate(LocalDateTime.now());
        findReservation.setReserveStatus(ReserveStatus.CANCELED_BY_HOST);
        findReservation.setCancelReason(cancelReason);

        int count = 0;
        // 결제 취소 로직
        if (findReservation.getPaymentStatus().equals(PaymentStatus.PREPAID))
            count = payService.cancelPrepaid(findReservation.getReserveId(), 0);
        else count = payService.cancelPostPaid(findReservation.getReserveId(), 0);

        return count;
    }

    /**
     * 매 시 30분마다 미결제된 예약을 조회 후 NOSHOW로 수정
     * NOSHOW처리된 예약자의 NOSHOW 이력 조회
     * 해당 이력건이 3회 이상일 경우 페널티 부여
     */
    @Scheduled(cron = "0 30 * * * *")
    @Transactional(rollbackFor = {Exception.class})
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

    public ListResult findAllByName(ReserveOrderSearch dto, PageRequest pageRequest) {
        log.info("findAllByName : {}, {}", dto, pageRequest);
        String name = SecurityUtil.getCurrentUsername().get();
        String role = SecurityUtil.getCurrentUserRole().get();
        log.info("name: {}, role : {}", name, role);
        if (role.equals(MemberType.ROLE_USER.toString())) {
            PageImpl<ReserveSimpleDto> result = reservationSimpleRepository.findAllByUserPage(name, pageRequest, dto.getReserveStatus(), dto.getSpaceType());
            return new ListResult(result.getTotalElements(), result.getContent());
        } else {
            PageImpl<ReserveSimpleDto> result = reservationSimpleRepository.findAllByHostPage(name, pageRequest, dto.getReserveStatus(), dto.getSpaceType());
            return new ListResult(result.getTotalElements(), result.getContent());
        }
    }


    /**
     * 선택 일자를 기준으로 예약 리스트 조회
     * 오피스의 경우 일별로 조회
     * 그 외의 사무공간의 경우 시간별로 조회
     */
    public List<ReserveCheckDto> findBookedReservation(ReserveCheckDto dto) {
        log.info("findBookedReservation : {}", dto);

        tempReservationRepository.deleteByTime(); // 작성한지 1분이 경과한 임시 데이터 삭제

        List<ReserveCheckDto> result = new ArrayList<>();
        if (dto.getRoomType().equals(RoomType.OFFICE)) {
            List<ReserveCheckDto> tempBookedOffices = tempReservationRepository.findBookedOffice(dto.getRoomId(), dto.getInitDate(), dto.getEndDate());
            List<ReserveCheckDto> reservedOffices = reservationSimpleRepository.findBookedOffice(dto.getRoomId(), dto.getInitDate(), dto.getEndDate());
            result.addAll(tempBookedOffices);
            result.addAll(reservedOffices);
        } else {
            List<ReserveCheckDto> tempBookedRooms = tempReservationRepository.findBookedRoom(dto.getRoomId(), dto.getInitDate());
            List<ReserveCheckDto> reservedRooms = reservationSimpleRepository.findBookedRoom(dto.getRoomId(), dto.getInitDate());
            result.addAll(tempBookedRooms);
            result.addAll(reservedRooms);
        }
        return result;
    }

    /**
     * 선택 일자를 기준으로 예약 리스트 조회
     * 오피스의 경우 일별로 조회
     * 그 외의 사무공간의 경우 시간별로 조회
     * 임시 테이블 제외 => 결제 전 확인
     */
    public void checkReservations(Room room, TempReservation temp) {
        log.info("checkReservations : {}", room, temp);
        List<ReserveCheckDto> result = null;
        if (room.getRoomType().equals(RoomType.OFFICE)) {
            if (!reservationSimpleRepository.findBookedOffice(room.getRoomId(), temp.getBookDate().getCheckInDate(), temp.getBookDate().getCheckOutDate()).isEmpty()) {
                throw new IllegalStateException("이미 예약건이 존재합니다");
            }
        } else {
            if (reservationSimpleRepository.checkBookedRoom(room.getRoomId(), temp.getBookDate().getCheckInDate(), temp.getBookDate().getCheckInTime(), temp.getBookDate().getCheckOutTime()).isEmpty()) {
                throw new IllegalStateException("이미 예약건이 존재합니다.");
            }
        }
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

    /**
     * 가격 비교 로직
     * JS 상에서 가격이 임의로 변경되어 넘어올 수 있기 때문에 DB상에서 가격 조회 후 비교
     * 올바른 가격 값이 아닐 경우 예외 처리
     */
    static boolean validAmount(int actualAmount, Room room, BookDate bookDate, int mileageUsage) {
        log.info("validAmount : {}, {}, {}, {}", actualAmount, room, bookDate, mileageUsage);
        int period;
        if (room.getRoomType() == RoomType.OFFICE)  period = BookDate.getPeriodDate(bookDate.getCheckOutDate(), bookDate.getCheckInDate());
        else  period = BookDate.getPeriodHours(bookDate.getCheckInTime(), bookDate.getCheckOutTime());
        int expectedAmount = room.getRoomPrice() * period - mileageUsage;
        log.info("expectedAmount : {}", expectedAmount);
        if (actualAmount == expectedAmount) {
            return true;
        } else return false;
    }
}
