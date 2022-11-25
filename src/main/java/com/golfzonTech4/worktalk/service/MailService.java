package com.golfzonTech4.worktalk.service;

import com.golfzonTech4.worktalk.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final MemberService memberService;
    private final JavaMailSender javaMailSender;

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

        memberService.findDuplicatesEmail(member);

        int validCode = creatingRandomCode();
        String setFrom = "brownenvelope613@gmail.com"; // setting sender's own eamil that were registerd at email-config
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
