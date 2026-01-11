package com.alaa.MoviesApp.service;

import com.alaa.MoviesApp.enums.ErrorCode;
import com.alaa.MoviesApp.exception.CustomException;
import com.alaa.MoviesApp.listener.UserRegisterEvent;
import com.alaa.MoviesApp.utils.SystemUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
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
    private final SystemUtils systemUtils;
    @Value("${app.verification.verify-url}")
    private String verifyUrl;
    private static final Logger logger = LogManager.getLogger(EmailService.class);


    @Async("executor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendAccountVerificationEmail(UserRegisterEvent userRegisterEvent) {
        String to = userRegisterEvent.getEmail();
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    "UTF-8");


            String verificationLink = verifyUrl + userRegisterEvent.getVerificationCode();
            String htmlContent = systemUtils.accountVerificationEmailBuild(verificationLink);
            message.setSubject("Fawry Movies Email Verification");
            helper.setTo(to);
            helper.setText(htmlContent, true);
            ClassPathResource res = new ClassPathResource("static/4-2-Fawry.png");
            helper.addInline("fawryLogo", res);
            mailSender.send(message);

        } catch (MailSendException e) {
            logger.error("Fail to send verification email to {}, --- thread--> {} ", to, Thread.currentThread().getName(), e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);

        } catch (MessagingException e) {
            logger.error("Error creating MimeMessage for email to {},--- thread--> {}", to,Thread.currentThread().getName(), e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);

        } catch (IOException e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        System.out.println("sending email to: "+to+"---> thread:"+Thread.currentThread().getName());
        logger.info("Successfully sent verification email to {}, --- thread--> {} ", to, Thread.currentThread().getName());
    }

}
