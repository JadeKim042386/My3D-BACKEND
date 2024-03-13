package joo.project.my3dbackend.service.impl;

import joo.project.my3dbackend.exception.MailException;
import joo.project.my3dbackend.exception.constants.ErrorCode;
import joo.project.my3dbackend.service.EmailServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService implements EmailServiceInterface {

    private final JavaMailSender javaMailSender;

    @Async
    @Override
    public void sendEmail(String to, String subject, String text) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(text);
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new MailException(ErrorCode.MAIL_SEND_FAIL, e);
        }
    }
}
