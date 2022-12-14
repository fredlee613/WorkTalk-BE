package com.golfzonTech4.worktalk.service;

import com.golfzonTech4.worktalk.domain.*;
import com.golfzonTech4.worktalk.dto.mileage.MileageDto;
import com.golfzonTech4.worktalk.dto.pay.PayInsertDto;
import com.golfzonTech4.worktalk.dto.pay.PayOrderSearch;
import com.golfzonTech4.worktalk.dto.pay.PaySimpleDto;
import com.golfzonTech4.worktalk.dto.pay.PayWebhookDto;
import com.golfzonTech4.worktalk.repository.ListResult;
import com.golfzonTech4.worktalk.repository.pay.PayRepository;
import com.golfzonTech4.worktalk.repository.pay.query.PayRepositoryQuery;
import com.golfzonTech4.worktalk.repository.reservation.ReservationRepository;
import com.golfzonTech4.worktalk.util.SecurityUtil;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.request.ScheduleData;
import com.siot.IamportRestClient.request.ScheduleEntry;
import com.siot.IamportRestClient.request.UnscheduleData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import com.siot.IamportRestClient.response.Schedule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PayService {
    private final PayRepository payRepository;
    private final ReservationRepository reservationRepository;
    private final MileageService mileageService;
    private final MyIamport myIamport;
    private final MailService mailService;
    private final PayRepositoryQuery payRepositoryQuery;

    /**
     * 결제 데이터 검증 및 저장 로직
     * 마일리지 사용 및 적립 로직
     */
    @Transactional
    public Pay save(PayInsertDto dto) {
        log.info("save : {}", dto);
        Reservation findReservation = reservationRepository.findById(dto.getReserveId()).get();

        Pay pay = Pay.builder().reservation(findReservation).impUid(dto.getImp_uid())
                .merchantUid(dto.getMerchant_uid()).payStatus(dto.getPayStatus())
                .payAmount(dto.getPayAmount()).build();

        Pay savedPay = payRepository.save(pay);

        return savedPay;
    }

    public void verify(String imp_uid, int payAmount) throws IamportResponseException, IOException {
        log.info("");
        IamportResponse<Payment> response = myIamport.getClient().paymentByImpUid(imp_uid);
        BigDecimal serverAmount = response.getResponse().getAmount();
        BigDecimal clientAmount = BigDecimal.valueOf(payAmount);
        log.info("clientAmount : {}, serverAmount : {}", clientAmount, serverAmount);
        if (!clientAmount.equals(serverAmount)) {
            new IllegalStateException("잘못된 가격값입니다.");
        }
    }

    /**
     * 선결제 데이터 DB 등록 로직
     * 선결제 관련 마일리지 등록 및 사용 로직
     */
    @Transactional(rollbackFor = {Exception.class})
    public Long prepaid(PayInsertDto dto) throws IamportResponseException, IOException {
        log.info("prepaid: {}", dto);
        // 가격 검증
        verify(dto.getImp_uid(), dto.getPayAmount());

        Pay savedPay = save(dto);

        // 선결제 중 잔금 납부일 경우 결제 상태
        if (dto.getMerchant_uid().contains("잔금")) {
            Optional<Reservation> result = reservationRepository.findById(dto.getReserveId());
            if (result.isPresent()) {
                result.get().setPaid(1);
            }
        }

        //마일리지 처리 로직
        useMileage(dto, savedPay);
        saveMileage(dto, savedPay);
        return savedPay.getPayId();
    }

    /**
     * 보증금 결제 데이터 DB 등록 로직
     * 예약 결제 및 예약 결제 데이터 DB 등록 로직(추후 결제 시 수정)
     */
    @Transactional
    public IamportResponse<List<Schedule>> schedule(PayInsertDto dto) throws IamportResponseException, IOException {
        log.info("schedule : {}", dto);
        Reservation findReservation = reservationRepository.findById(dto.getReserveId()).get();

        log.info("findReservation : {}", findReservation);

        verify(dto.getImp_uid(), dto.getPayAmount());

        Pay deposit = Pay.builder().reservation(findReservation).impUid(dto.getImp_uid())
                .merchantUid(dto.getMerchant_uid()).payStatus(PaymentStatus.DEPOSIT)
                .payAmount(dto.getPayAmount()).build();
        log.info("save : {}", deposit);
        payRepository.save(deposit); // 보증금 결제 데이터 DB 등록

        LocalDateTime endDate = BookDate.getEndTime(
                findReservation.getBookDate().getCheckOutDate(),
                findReservation.getBookDate().getCheckOutTime()).plusHours(1);
        log.info("endDate : {}", endDate);

        String merchant_uid = dto.getMerchant_uid() + " 예약결제";
        int balance = (int) (findReservation.getReserveAmount() * 0.8);

        log.info("merchant_uid : {}, balance : {}", merchant_uid, balance);

        ScheduleEntry scheduleEntry = new ScheduleEntry(
                merchant_uid,
                Timestamp.valueOf(endDate), // 자동 결제 시점 설정 (퇴실시간 + 1)
//                Timestamp.valueOf(LocalDateTime.now().plusMinutes(3)), // 테스트용 자동 결제 시점 설정 (퇴실시간 + 30초)
                BigDecimal.valueOf(balance)); // 자동 결제 금액 설정 (총 금액의 80프로)

        ScheduleData scheduleData = new ScheduleData(dto.getCustomer_uid());
        scheduleData.addSchedule(scheduleEntry);

        IamportResponse<List<Schedule>> response = myIamport.getClient().subscribeSchedule(scheduleData); // 예약 결제
        log.info("result >>> {}", response.getMessage());

        Pay bookedPay = Pay.builder().reservation(findReservation).merchantUid(merchant_uid)
                .customerUid(dto.getCustomer_uid()).payStatus(PaymentStatus.POSTPAID_BOOKED).payAmount(balance).build();

        Pay savedPay = payRepository.save(bookedPay);// 예약 결제 데이터 DB 등록

        // 마일리지 처리 로직
        // 마일리지 객체에 결제 정보를 보증금으로 설정할 경우 취소 시 정책에 따라서 취소되지 않을 수 있기에
        // 결제 정보를 후결제로 설정
        // 마일리지 사용 처리 로직
        useMileage(dto, savedPay);
        return response;
    }

    /**
     * 결제 취소 서비스 로직
     * 예약 시간에 따른 환불 정책여부를 int flag로 전달(0, 1)
     * 예약 1시간 이내 전액 환불
     * 예약 1시간 초과 선결제금액만 환불 (보증금 X)
     * 예약 결제 취소
     */
    @Transactional
    public int cancelPay(Long reserveId, Integer flag) throws IamportResponseException, IOException {
        log.info("cancelByHost");
        IamportClient client = myIamport.getClient();
        List<PayInsertDto> findPays = payRepository.findAlByReserveId(reserveId);
        log.info("findPays.size() : {}", findPays.size());
        int count = 0;
        for (PayInsertDto findPay : findPays) { // 해당 예약에서 발생한 결제 건수 만큼 반복
            log.info("findPay {}: ", findPay.toString());
            boolean condition_in = findPay.getPayStatus().equals(PaymentStatus.DEPOSIT) || findPay.getPayStatus().equals(PaymentStatus.PREPAID);
            boolean condition_out = findPay.getPayStatus().equals(PaymentStatus.PREPAID);

            if (flag == 0 ? condition_in : condition_out) { // 예약 1시간 내 취소 시 전액 환불, 예약 1시간 초과 후 취소 시 선급금만 환불 (보증금 X)
                // imp_uid 값으로 전액 취소
                IamportResponse<Payment> response = client.cancelPaymentByImpUid(new CancelData(findPay.getImp_uid(), true));
                log.info("response : {}", response);
                findPay.setPayStatus(PaymentStatus.REFUND); // 결제 데이터 상태를 환불로 변경
                Long canceledPay = save(findPay).getPayId(); // 취소 결제 데이터 추가
                log.info("canceledPay : {}", canceledPay);

                // 마일리지 데이터 취소 처리 로직
                mileageService.cancelSave(findPay.getPayId());
                mileageService.cancelUsage(findPay.getPayId());

                count++;
            } else if (findPay.getPayStatus().equals(PaymentStatus.POSTPAID_BOOKED)) { // 예약 결제의 경우 취소
                // customer_uid 값으로 예약 결제 취소
                IamportResponse<List<Schedule>> response = client.unsubscribeSchedule(new UnscheduleData(findPay.getCustomer_uid()));
                log.info("response : {}", response);
                findPay.setPayStatus(PaymentStatus.REFUND);
                payRepository.deleteById(findPay.getPayId()); // 해당 결제 데이터 삭제
                log.info("canceledPay : {}", findPay.getPayId());

                // 마일리지 데이터 취소 처리 로직


                count++;
            }
            // 해당 예약 건의 결제 상태를 미결제로 수정(paid = 0)
            Reservation findReservation = reservationRepository.findById(reserveId).get();
            findReservation.setPaid(0);
        }
        return count;
    }

    /**
     * 선 결제 취소
     */
    @Transactional
    public int cancelPrepaid(Long reserveId, Integer flag) throws IamportResponseException, IOException {
        log.info("cancelPrepaid");
        IamportClient client = myIamport.getClient();
        List<PayInsertDto> findPays = payRepository.findAlByReserveId(reserveId);
        log.info("findPays.size() : {}", findPays.size());
        int count = 0;
        if (flag == 0) { // 예약 1시간 이내일 경우
            for (PayInsertDto findPay : findPays) { // 보증금, 선결제 전체 취소
                // 해당 결제 건의 마일리지 내역 삭제
                mileageService.cancelUsage(findPay.getPayId());
                mileageService.cancelSave(findPay.getPayId());

                // 해당 결제 건 취소
                IamportResponse<Payment> response = client.cancelPaymentByImpUid(
                        new CancelData(findPay.getImp_uid(), true));// imp_uid 값으로 전액 취소
                log.info("response : {}", response);
                findPay.setPayStatus(PaymentStatus.REFUND); // 결제 데이터 상태를 환불로 변경
                Long canceledPay = save(findPay).getPayId(); // 취소 결제 데이터 추가
                log.info("canceledPay : {}", canceledPay);

                count++;
            }
        } else { // 예약 1시간 초과일 경우
            double rate = findPays.size() == 1 ? 1.0 : 0.8;
            // 일괄 계산 시 총액의 0.8만 취소, 보증금의 경우 취소 안됨
            for (PayInsertDto findPay : findPays) { // 선결제(일괄결제)만 취소, 보증금 제외
                // 해당 결제 건의 마일리지 내역 삭제
                mileageService.cancelUsage(findPay.getPayId());
                mileageService.cancelSave(findPay.getPayId());

                if (findPay.getPayStatus() == PaymentStatus.PREPAID) {
                    IamportResponse<Payment> response = client.cancelPaymentByImpUid(
                            new CancelData(findPay.getImp_uid(), true,// imp_uid 값으로 취소 진행
                                    BigDecimal.valueOf(findPay.getPayAmount() * rate)));
                    log.info("response : {}", response);
                    findPay.setPayStatus(PaymentStatus.REFUND); // 결제 데이터 상태를 환불로 변경
                    Long canceledPay = save(findPay).getPayId(); // 취소 결제 데이터 추가
                    log.info("canceledPay : {}", canceledPay);
                    count++;
                }
            }
        }
        // 해당 예약 건의 결제 상태를 미결제로 수정(paid = 0)
        Reservation findReservation = reservationRepository.findById(reserveId).get();
        findReservation.setPaid(0);

        return count;
    }

    /**
     * 후결제 취소 및 마일리지 적립 내역 취소 로직
     */
    @Transactional
    public int cancelPostPaid(Long reserveId, Integer flag) throws IamportResponseException, IOException {
        log.info("cancelPostPaid");
        IamportClient client = myIamport.getClient();
        List<PayInsertDto> findPays = payRepository.findAlByReserveId(reserveId);
        log.info("findPays.size() : {}", findPays.size());
        int count = 0;
        if (flag == 0) { // 예약 1시간 이내일 경우 보증금 취소
            for (PayInsertDto findPay : findPays) {
                if (findPay.getPayStatus().equals(PaymentStatus.DEPOSIT)) { // 보증금일 시 결제 취소 후 DB에 취소 데이터 입력
                    IamportResponse<Payment> response = client.cancelPaymentByImpUid(
                            new CancelData(findPay.getImp_uid(), true));// imp_uid 값으로 전액 취소
                    log.info("response : {}", response);
                    findPay.setPayStatus(PaymentStatus.REFUND); // 결제 데이터 상태를 환불로 변경
                    Long canceledPay = save(findPay).getPayId(); // 취소 결제 데이터 추가
                    log.info("canceledPay : {}", canceledPay);
                    count++;
                }
            }
        }
        for (PayInsertDto findPay : findPays) { // 결제 예약 취소 후 해당 데이터 DB에서 삭제
            if (findPay.getPayStatus() == PaymentStatus.POSTPAID_BOOKED) {
                // 해당 결제 건에 해당하는 마일리지 사용 내역 삭제 (후결제이기에 적립 내역 X => 적립 취소 로직 추가 X)
                mileageService.cancelUsage(findPay.getPayId());

                IamportResponse<List<Schedule>> response =
                        client.unsubscribeSchedule(new UnscheduleData(findPay.getCustomer_uid()));// customer_uid 값으로 예약 결제 취소
                log.info("response : {}", response);
                findPay.setPayStatus(PaymentStatus.REFUND);
                payRepository.deleteById(findPay.getPayId()); // 해당 결제 데이터 삭제
                log.info("canceledPay : {}", findPay.getPayId());
//                if (response.getMessage().equals("성공메세지")) {
//                    payRepository.deleteById(findPay.getPayId()); // 해당 예약 데이터 삭제
//                }
                count++;
            }
        }
        return count;
    }

    /**
     * 웹훅을 수신한 예약 결제 데이터를 저장하는 로직
     * 기존에 예약 되어있던 결제 예약 데이터를 상태/고유 번호를 수정
     */
    @Transactional(rollbackFor = {Exception.class})
    public void postpaid(PayWebhookDto dto) throws IamportResponseException, IOException {
        log.info("postpaid: {}}", dto);
        IamportClient client = myIamport.getClient();
        Payment paidPay = client.paymentByImpUid(dto.getImp_uid()).getResponse();
        String status = paidPay.getStatus();
        log.info("status: {}", status);
        if (status.equals("paid")) {
            Pay findPay = payRepository.findByCustomerUid(paidPay.getCustomerUid()).get();
            findPay.setPayStatus(PaymentStatus.POSTPAID_DONE); // 결제 상태를 POSTPAID_DONE(후결제 완료)로 설정
            findPay.setImpUid(paidPay.getImpUid()); // Imp_uid값 설정

            // 결제 성공 후 결제 상태를 결제 완료(1)로 변경
            Long reserveId = findPay.getReservation().getReserveId();
            log.info("reserveId : {}", reserveId);
            Reservation findReservation = reservationRepository.findById(reserveId).get();
            log.info("findReservation : {}", findReservation);
            findReservation.setPaid(1);
        } else { // 결제 실패 시 3일 후 예약 결제 진행
            log.info("status : {}", status);
            Pay findPay = payRepository.findByCustomerUid(paidPay.getCustomerUid()).get();
            LocalDateTime payDate = paidPay.getPaidAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().plusDays(3);
            ScheduleEntry scheduleEntry = new ScheduleEntry(paidPay.getMerchantUid(),
                    Timestamp.valueOf(payDate), // 지난 결제 시점에서 3일 후 결제 예약
                    paidPay.getAmount()); // 자동 결제 금액 설정 (총 금액의 80프로)

            ScheduleData scheduleData = new ScheduleData(findPay.getCustomerUid());
            scheduleData.addSchedule(scheduleEntry);

            IamportResponse<List<Schedule>> response = myIamport.getClient().subscribeSchedule(scheduleData); // 예약 결제
            log.info("IamportResponse<List<Schedule>> : {}", response.toString());
            Reservation findReservation = reservationRepository.findById(findPay.getReservation().getReserveId()).get();
            // 예약 결제 알림 메일 발송
            mailService.payMail(findReservation.getMember().getId(), findReservation.getReserveId(), findPay.getPayAmount(), payDate);
        }
    }

    /**
     * 결제건이 있는 방들의 이름 조회
     */
    public HashSet<String> findRooms() {
        log.info("findRooms: {}, {}");
        String name = SecurityUtil.getCurrentUsername().get();
        List<PaySimpleDto> rooms = payRepository.findRooms(name);
        HashSet<String> result = new HashSet<>();
        for (PaySimpleDto room : rooms) {
            result.add(room.getRoomName());
        }
        return result;
    }

    /**
     * 결제 내역 전체 조회 (사용자, 호스트)
     */
    public ListResult findByName(PayOrderSearch dto, PageRequest pageRequest) {
        log.info("findByName: {}, {}", dto, pageRequest);
        String name = SecurityUtil.getCurrentUsername().get();
        String role = SecurityUtil.getCurrentUserRole().get();
        if (role.equals(MemberType.ROLE_USER.toString())) {
            PageImpl<PaySimpleDto> result = payRepository.findByUser(name,dto.getReserveDate(), dto.getPayStatus(), pageRequest);
            return new ListResult<>(result.getTotalElements(), result.getContent());
        } else {
            PageImpl<PaySimpleDto> result = payRepository.findByHost(name, dto.getReserveDate(), dto.getPayStatus(), dto.getSpaceType(), dto.getRoomName() ,pageRequest);
            return new ListResult<>(result.getTotalElements(), result.getContent());
        }
    }

    /**
     * 마일리지 적립 매서드
     */
    private void saveMileage(PayInsertDto dto, Pay savedPay) {
        int mileageSave = dto.getMileageSave();
        if (mileageSave != 0) {
            MileageDto save = MileageDto.builder()
                    .payId(savedPay.getPayId())
                    .mileageAmount(mileageSave)
                    .build();
            mileageService.save(save);
        }
    }

    /**
     * 마일리지 사용 매서드
     */
    private void useMileage(PayInsertDto dto, Pay savedPay) {
        int mileageUsage = dto.getMileageUsage();
        if (mileageUsage != 0) {
            MileageDto usage = MileageDto.builder()
                    .payId(savedPay.getPayId())
                    .mileageAmount(mileageUsage)
                    .build();
            mileageService.use(usage);
        }
    }

}
