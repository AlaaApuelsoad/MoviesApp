package com.fawry.MoviesApp.service;

import com.fawry.MoviesApp.enums.VerificationStatus;
import com.fawry.MoviesApp.listener.UserRegisterEvent;
import com.fawry.MoviesApp.model.User;
import com.fawry.MoviesApp.repository.UserRepository;
import com.fawry.MoviesApp.utils.SystemUtils;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerifyService {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final SystemUtils systemUtils;
    private static final Logger logger = LogManager.getLogger(VerifyService.class);
    private final EmailService emailService;


    public VerificationStatus verifyUser(String verificationCode) {

        long startTime = System.currentTimeMillis();

        if (verificationCode == null || verificationCode.trim().isEmpty()) {
            return VerificationStatus.INVALID_CODE;
        }

        Optional<User> optionalUser = getUserByVerificationCode(verificationCode);
        if (optionalUser.isEmpty()) {
            return VerificationStatus.CODE_NOT_FOUND;
        }

        User user = optionalUser.get();
        if (isCodeExpired(user.getVerificationCodeExpiryDate())) {
            resendVerificationEmail(user);
            return VerificationStatus.EXPIRED;

        } else {
            user.setVerified(true);
            user.setVerificationCode(null);
            user.setVerificationCodeExpiryDate(null);
            userRepository.save(user);
            logger.info("Verification successful for user: {} Total time: {}ms", user.getEmail(), System.currentTimeMillis() - startTime);
            return VerificationStatus.SUCCESS;
        }
    }

    public void resendVerificationEmail(User user) {
        user.setVerificationCode(systemUtils.generateUUIDCode());
        user.setVerificationCodeExpiryDate(LocalDateTime.now().plusSeconds(10));
        userRepository.save(user);
        emailService.sendAccountVerificationEmail(new UserRegisterEvent(user));
//        eventPublisher.publishEvent(new UserRegisterEvent(user));
    }


    private Optional<User> getUserByVerificationCode(String verificationCode) {
        return userRepository.findByVerificationCode(verificationCode);
    }

    private boolean isCodeExpired(LocalDateTime codeExpireDate) {
        return LocalDateTime.now().isAfter(codeExpireDate);
    }
}
