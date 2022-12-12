package com.golfzonTech4.worktalk.service;

import com.golfzonTech4.worktalk.config.EmailConfig;
import com.golfzonTech4.worktalk.domain.Member;
import com.golfzonTech4.worktalk.dto.reservation.ReserveSimpleDto;
import com.golfzonTech4.worktalk.repository.member.MemberRepository;
import com.golfzonTech4.worktalk.repository.reservation.ReservationSimpleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final MemberRepository memberRepository;
    private final JavaMailSender javaMailSender;
    private final ReservationSimpleRepository reservationSimpleRepository;
    private final EmailConfig emailConfig;

    public int creatingRandomCode() {
        log.info("creatingRandomCode");

        Random random = new Random();
        int validCode = random.nextInt(888888) + 111111;
        log.info("validCode : {}", validCode);

        return validCode;
    }

    public String joinMail(String email) {
        log.info("joinMail() : {}", email);
        Member member = new Member();
        member.setEmail(email);

        member.setEmail(email);

        Optional<Member> result = memberRepository.findByEmail(email);
        if (result.isPresent()) throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");

        int validCode = creatingRandomCode();

        String setFrom = emailConfig.getUserEmail(); // setting sender's own eamil that were registerd at email-config
        String toMail = email;
        String title = "WorkTalk 회원 가입 인증 이메일 입니다."; // Email Title
        String content =
                "홈페이지를 방문해주셔서 감사합니다." +    //Should be written in html
                        "<br><br>" +
                        "인증 번호는 " + String.valueOf(validCode) + "입니다." +
                        "<br>" +
                        "해당 인증번호를 인증번호 확인란에 기입하여 주세요."; //Email content
        sendMail(setFrom, toMail, title, content);
        return Integer.toString(validCode);
    }

    public void payMail(Long memberId, Long reserveID, int amount, LocalDateTime payDate) {
        log.info("payMail() : {}", memberId);

        Member member = new Member();
        member.setId(memberId);

        Member findMember = memberRepository.findById(memberId).get();
        ReserveSimpleDto dto = new ReserveSimpleDto();
        dto.setReserveId(reserveID);
        ReserveSimpleDto findReservation = reservationSimpleRepository.findRoomName(reserveID).get();

        String purchaseDate = payDate.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm:ss"));


        String setFrom = emailConfig.getUserEmail();; // setting sender's own eamil that were registerd at email-config
        String toMail = findMember.getEmail();
        String title = "WorkTalk 미결제 관련 안내 메일입니다."; // Email Title
        String content =
                "WorkTalk를 이용해주셔서 감사합니다." +    //Should be written in html
                        "<br><br>" +
                        "예약건 : [" + findReservation.getRoomName() + "]에 대한 예역 결제가 실패하였습니다." +
                        "<br>" +
                        "다음 예약 결제일은 " + purchaseDate + "이며 " + "" +
                        "<br>" +
                        "결제 예정 금액은 " + amount + "원이 되겠습니다." +
                        "<br>" +
                        "원활한 이용을 위하여 결제 전 확인 부탁드립니다 ." +
                        "<br>" +
                        "감사합니다."; //Email content
        sendMail(setFrom, toMail, title, content);
    }

    public int pwMail(String email) {
        log.info("pwMail() : {}", email);

        int validCode = creatingRandomCode();

        String setFrom = emailConfig.getUserEmail(); // setting sender's own eamil that were registerd at email-config
        String toMail = email;
        String title = "WorkTalk 회원 임시비밀번호 메일입니다."; // Email Title
        String content =
                "임시 비밀번호를 전송하였습니다." +    //Should be written in html
                        "<br><br>" +
                        "임시 비밀번호는 " + String.valueOf(validCode) + "입니다." +
                        "<br>" +
                        "해당 임시 비밀번호는 보안에 취약하니 접속 후 빠른 시일 내에 비밀번호를 재설정해주시기 바랍니다."; //Email content
        sendMail(setFrom, toMail, title, content);
        return validCode;
    }

    public void sendMail(String setFrom, String toMail, String title, String content) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(setFrom);
            helper.setTo(toMail);
            helper.setSubject(title);
            helper.setText(content, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
