package com.fawry.MoviesApp.service;

import com.fawry.MoviesApp.enums.ErrorCode;
import com.fawry.MoviesApp.exception.CustomException;
import com.fawry.MoviesApp.listener.UserRegisterEvent;
import com.fawry.MoviesApp.utils.EmailTemplateUtility;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    @Value("${app.verification.verify-url}")
    private String verifyUrl;
    private final EmailTemplateUtility emailTemplateUtility;
    private static final Logger logger = LogManager.getLogger(EmailService.class);


    @Async("asyncExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendAccountVerificationEmail(UserRegisterEvent userRegisterEvent) {
        String to = userRegisterEvent.getEmail();
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    "UTF-8");


            String verificationLink = verifyUrl + userRegisterEvent.getVerificationCode();
            String htmlContent = emailTemplateUtility.accountVerificationEmailBuild(verificationLink);
            message.setSubject("Fawry Movies Email Verification");
            helper.setTo(to);
            helper.setText(htmlContent, true);
            mailSender.send(message);

        } catch (MailSendException e) {
            logger.error("Fail to send verification email to {} ", to + e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);

        } catch (MessagingException e) {
            logger.error("Error creating MimeMessage for email to {}", to, e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);

        } catch (IOException e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        logger.info("Successfully sent verification email to {}", to);
    }

}
