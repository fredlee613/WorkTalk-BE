package com.golfzonTech4.worktalk.service;

import com.golfzonTech4.worktalk.domain.*;
import com.golfzonTech4.worktalk.dto.pay.PayInsertDto;
import com.golfzonTech4.worktalk.dto.pay.PayWebhookDto;
import com.golfzonTech4.worktalk.repository.pay.PayRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.*;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import com.siot.IamportRestClient.response.Schedule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PayService {
    private final PayRepository payRepository;
    private final ReservationService reservationService;
    private final MyIamport myIamport;

    private final MailService mailService;

    /**
     * 결제 데이터 검증 및 저장 로직
     */
    @Transactional
    public Long save(PayInsertDto dto) {
        log.info("save : {}", dto);
        Reservation findReservation = reservationService.findById(dto.getReserveId()).get();

        Pay pay = Pay.builder().reservation(findReservation).impUid(dto.getImp_uid())
                .merchantUid(dto.getMerchant_uid()).payStatus(dto.getPayStatus())
                .payAmount(dto.getPayAmount()).build();

        Pay savedPay = payRepository.save(pay);

        return savedPay.getPayId();
    }

    /**
     * 보증금 결제 데이터 DB 등록
     * 예약 결제 및 예약 결제 데이터 DB 등록 (추후 결제 시 수정)
     */
    @Transactional(rollbackFor = Exception.class)
    public IamportResponse<List<Schedule>> schedule(PayInsertDto dto) throws IamportResponseException, IOException {

        Reservation findReservation = reservationService.findById(dto.getReserveId()).get();

        log.info("findReservation : {}", findReservation);

        Pay deposit = Pay.builder().reservation(findReservation).impUid(dto.getImp_uid())
                .merchantUid(dto.getMerchant_uid()).payStatus(dto.getPayStatus())
                .payAmount(dto.getPayAmount()).build();

        payRepository.save(deposit); // 보증금 결제 데이터 DB 등록

        LocalDateTime endDate = BookDate.getEndTime(
                findReservation.getBookDate().getCheckOutDate(),
                findReservation.getBookDate().getCheckOutTime()).plusHours(1);
        log.info("endDate : {}", endDate);

        String merchant_uid = dto.getMerchant_uid() + "예약결제";
        int balance = (int) (findReservation.getReserveAmount() * 0.8);

        log.info("merchant_uid : {}, balance : {}", merchant_uid, balance);

        ScheduleEntry scheduleEntry = new ScheduleEntry(
                merchant_uid,
//                Timestamp.valueOf(endDate), // 자동 결제 시점 설정 (퇴실시간 + 1)
                Timestamp.valueOf(LocalDateTime.now().plusSeconds(30)), // 테스트용 자동 결제 시점 설정 (퇴실시간 + 30초)
                BigDecimal.valueOf(balance)); // 자동 결제 금액 설정 (총 금액의 80프로)

        ScheduleData scheduleData = new ScheduleData(dto.getCustomer_uid());
        scheduleData.addSchedule(scheduleEntry);

        IamportResponse<List<Schedule>> response = myIamport.getClient().subscribeSchedule(scheduleData); // 예약 결제


        Pay bookedPay = Pay.builder().reservation(findReservation).merchantUid(merchant_uid)
                .customerUid(dto.getCustomer_uid()).payStatus(PaymentStatus.POSTPAID_BOOKED).payAmount(balance).build();

        payRepository.save(bookedPay); // 예약 결제 데이터 DB 등록
        return response;
    }

    /**
     * 결제 취소 서비스 로직
     * 예약 시간에 따른 환불 정책여부를 int flag로 전달(0, 1)
     * 예약 1시간 이내 전액 환불
     * 예약 1시간 초과 선결제금액만 환불 (보증금 X)
     * 예약 결제 취소
     */
    @Transactional(rollbackFor = Exception.class)
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
                Long canceledPay = save(findPay); // 취소 결제 데이터 추가
                log.info("canceledPay : {}", canceledPay);
                count++;
            } else if (findPay.getPayStatus().equals(PaymentStatus.POSTPAID_BOOKED)) { // 예약 결제의 경우 취소
                // customer_uid 값으로 예약 결제 취소
                IamportResponse<List<Schedule>> response = client.unsubscribeSchedule(new UnscheduleData(findPay.getCustomer_uid()));
                log.info("response : {}", response);
                findPay.setPayStatus(PaymentStatus.REFUND);
                payRepository.deleteById(findPay.getPayId()); // 해당 결제 데이터 삭제
                log.info("canceledPay : {}", findPay.getPayId());
//                if (response.getMessage().equals("성공 시 메세지")) {
//                    payRepository.deleteById(findPay.getPayId()); // 해당 예약 데이터 삭제
//                }
                count++;
            }
        }
        return count;
    }

    /**
     * 선 결제 취소
     */
    @Transactional(rollbackFor = Exception.class)
    public int cancelPrepaid(Long reserveId, Integer flag) throws IamportResponseException, IOException {
        log.info("cancelPrepaid");
        IamportClient client = myIamport.getClient();
        List<PayInsertDto> findPays = payRepository.findAlByReserveId(reserveId);
        log.info("findPays.size() : {}", findPays.size());
        int count = 0;
        if (flag == 0) { // 예약 1시간 이내일 경우
            for (PayInsertDto findPay : findPays) { // 보증금, 선결제 전체 취소
                IamportResponse<Payment> response = client.cancelPaymentByImpUid(
                        new CancelData(findPay.getImp_uid(), true));// imp_uid 값으로 전액 취소
                log.info("response : {}", response);
                findPay.setPayStatus(PaymentStatus.REFUND); // 결제 데이터 상태를 환불로 변경
                Long canceledPay = save(findPay); // 취소 결제 데이터 추가
                log.info("canceledPay : {}", canceledPay);
                count++;
            }
        } else { // 예약 1시간 초과일 경우
            for (PayInsertDto findPay : findPays) { // 선결제(일괄결제)만 취소, 보증금 제외
                if (findPay.getPayStatus() == PaymentStatus.PREPAID) {
                    double rate = findPays.size() == 1 ? 1.0 : 0.8; // 일괄 계산 시 총액의 0.8만 취소, 보증금의 경우 취소 안됨
                    IamportResponse<Payment> response = client.cancelPaymentByImpUid(
                            new CancelData(findPay.getImp_uid(), true,// imp_uid 값으로 전액 취소
                                    BigDecimal.valueOf(findPay.getPayAmount() * rate)));
                    log.info("response : {}", response);
                    findPay.setPayStatus(PaymentStatus.REFUND); // 결제 데이터 상태를 환불로 변경
                    Long canceledPay = save(findPay); // 취소 결제 데이터 추가
                    log.info("canceledPay : {}", canceledPay);
                    count++;
                }
            }
        }
        return count;
    }

    /**
     *
     */
    @Transactional(rollbackFor = Exception.class)
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
                    Long canceledPay = save(findPay); // 취소 결제 데이터 추가
                    log.info("canceledPay : {}", canceledPay);
                    count++;
                }
            }
        }
        for (PayInsertDto findPay : findPays) { // 결제 예약 취소 후 해당 데이터 DB에서 삭제
            if (findPay.getPayStatus() == PaymentStatus.POSTPAID_BOOKED) {
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
    @Transactional
    public void postpaid(PayWebhookDto dto) throws IamportResponseException, IOException {
        IamportClient client = myIamport.getClient();
        Payment paidPay = client.paymentByImpUid(dto.getImp_uid()).getResponse();
        if (paidPay.getStatus().equals("paid")) {
            Pay findPay = payRepository.findByCustomerUid(paidPay.getCustomerUid()).get();
            findPay.setPayStatus(PaymentStatus.POSTPAID_DONE); // 결제 상태를 POSTPAID_DONE(후결제 완료)로 설정
            findPay.setImpUid(paidPay.getImpUid()); // Imp_uid값 설정

            // 결제 성공 후 결제 상태를 결제 완료(1)로 변경
            Long reserveId = findPay.getReservation().getReserveId();
            log.info("reserveId : {}", reserveId);
            Reservation findReservation = reservationService.findById(reserveId).get();
            log.info("findReservation : {}", findReservation);
            findReservation.setPaid(1);
        } else { // 결제 실패 시 3일 후 예약 결제 진행
            log.info("status : {}", paidPay.getStatus());
            Pay findPay = payRepository.findByCustomerUid(paidPay.getCustomerUid()).get();
            LocalDateTime payDate = paidPay.getPaidAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().plusDays(3);

            ScheduleEntry scheduleEntry = new ScheduleEntry(paidPay.getMerchantUid(),
                    Timestamp.valueOf(payDate), // 지난 결제 시점에서 3일 후 결제 예약
                    paidPay.getAmount()); // 자동 결제 금액 설정 (총 금액의 80프로)

            ScheduleData scheduleData = new ScheduleData(findPay.getCustomerUid());
            scheduleData.addSchedule(scheduleEntry);

            IamportResponse<List<Schedule>> response = myIamport.getClient().subscribeSchedule(scheduleData); // 예약 결제
            log.info("IamportResponse<List<Schedule>> : {}", response.toString());
            Reservation findReservation = reservationService.findById(findPay.getReservation().getReserveId()).get();
            // 예약 결제 알림 메일 발송
            mailService.payMail(findReservation.getMember().getId(), findReservation.getReserveId(), findPay.getPayAmount(), payDate);
        }
    }
}
