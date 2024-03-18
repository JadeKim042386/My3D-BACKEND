package joo.project.my3dbackend.service.impl;

import joo.project.my3dbackend.dto.properties.Pop3Properties;
import joo.project.my3dbackend.exception.MailException;
import joo.project.my3dbackend.exception.constants.ErrorCode;
import joo.project.my3dbackend.service.EmailServiceInterface;
import joo.project.my3dbackend.service.UserAccountServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.time.Instant;
import java.util.Optional;
import java.util.Properties;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService implements EmailServiceInterface {

    private final JavaMailSender javaMailSender;
    private final UserAccountServiceInterface userAccountService;
    private final Pop3Properties pop3Properties;

    private static final String FAILED_RECIPIENTS_HEADER = "X-Failed-Recipients";

    @Async
    @Override
    public void sendAsyncEmail(String to, String subject, String text) {
        sendEmail(to, subject, text);
    }

    @Transactional
    @Override
    public void sendRandomPassword(String toEmail, String subject, String password) {
        userAccountService.updatePassword(toEmail, password);
        sendEmail(toEmail, subject, password);
    }

    @Override
    public boolean isCompleteSentEmail(String email) {
        try {
            Thread.sleep(5000); // 메일이 반송되기까지 기다릴 시간
            Folder emailFolder = getEmailFolder();
            Instant untilTime = Instant.now().minusSeconds(pop3Properties.untilTime());
            for (Message message : emailFolder.getMessages()) {
                // 지정한 시간(limitedTime) 안에 수신한 메일만 확인
                if (untilTime.isBefore(message.getSentDate().toInstant())) {
                    log.debug("Subject: {}", message.getSubject());
                    // 받는 사람 이메일 확인
                    Optional.ofNullable(message.getHeader(FAILED_RECIPIENTS_HEADER))
                        .ifPresent(recipients -> {
                            if (recipients[0].equals(email)) {
                                try {
                                    // 반송된 이메일 삭제
                                    message.setFlag(Flags.Flag.DELETED, true);
                                    emailFolder.close(true);
                                } catch (MessagingException e) {
                                    throw new MailException(ErrorCode.CANT_GET_MAIL, e);
                                }
                            }
                        });
                    if (!emailFolder.isOpen()) {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            throw new MailException(ErrorCode.CANT_GET_MAIL, e);
        }
        return true;
    }

    private void sendEmail(String to, String subject, String text) {
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

    private Folder getEmailFolder() {
        try {
            Properties properties = new Properties();
            properties.put("mail.pop3.host", pop3Properties.host());
            properties.put("mail.pop3.port", pop3Properties.port());
            properties.put("mail.pop3.starttls.enable", "true");
            Session emailSession = Session.getDefaultInstance(properties);

            Store store = emailSession.getStore(pop3Properties.protocol());
            store.connect(pop3Properties.host(), pop3Properties.username(), pop3Properties.password());

            Folder emailFolder = store.getFolder(pop3Properties.folder());
            emailFolder.open(Folder.READ_WRITE);
            return emailFolder;
        } catch (Exception e) {
            throw new MailException(ErrorCode.CANT_GET_FOLDER, e);
        }
    }
}
