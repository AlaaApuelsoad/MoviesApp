package com.alaa.moviesapp.service;

import com.alaa.moviesapp.enums.ErrorCode;
import com.alaa.moviesapp.exception.BusinessException;
import com.alaa.moviesapp.listener.UserRegisterEvent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Year;


@Component
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SystemPropertyService systemPropertyService;
    private static final Logger logger = LogManager.getLogger(EmailService.class);

    @Async("asyncNotification")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendAccountVerificationEmail(UserRegisterEvent userRegisterEvent) {
        String verifyUrl = systemPropertyService.getProperty("app.verification.verify-url");
        String to = userRegisterEvent.getEmail();
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    "UTF-8");


            String verificationLink = verifyUrl + userRegisterEvent.getVerificationCode();
            String htmlContent = this.emailBuilder(verificationLink);
            message.setSubject("Movies App Email Verification");
            helper.setTo(to);
            helper.setText(htmlContent, true);
            mailSender.send(message);

        } catch (MailSendException e) {
            logger.error("Fail to send verification email to {}, --- thread--> {} ", to, Thread.currentThread().getName(), e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);

        } catch (MessagingException e) {
            logger.error("Error creating MimeMessage for email to {},--- thread--> {}", to, Thread.currentThread().getName(), e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);

        } catch (IOException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        logger.info("Successfully sent verification email to {}, --- thread--> {} ", to, Thread.currentThread().getName());
    }

    private String emailBuilder(String verificationLink) throws IOException {
        Path path = Path.of(systemPropertyService.getProperty("app.template.Account.Verification"));
        String templateContent = Files.readString(path);
        return templateContent.replace("{{verification_link}}",verificationLink)
                .replace("{{year}}",String.valueOf(Year.now().getValue()));
    }
}
