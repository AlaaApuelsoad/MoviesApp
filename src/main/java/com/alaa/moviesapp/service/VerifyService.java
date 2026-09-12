package com.alaa.MoviesApp.service;

import com.alaa.MoviesApp.dto.AppResponse;
import com.alaa.MoviesApp.enums.ErrorCode;
import com.alaa.MoviesApp.listener.UserRegisterEvent;
import com.alaa.MoviesApp.model.User;
import com.alaa.MoviesApp.repository.UserRepository;
import com.alaa.MoviesApp.utils.AppResponseBuilder;
import com.alaa.MoviesApp.utils.SystemUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerifyService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final SystemPropertyService systemPropertyService;
    private static final Logger logger = LoggerFactory.getLogger(VerifyService.class);

    @Transactional
    public AppResponse<String> verifyUser(String verificationCode) {

        if (verificationCode == null || verificationCode.trim().isEmpty()) {
            return AppResponseBuilder.error(ErrorCode.INVALID_VERIFICATION_CODE,"invalid.verification.code");
        }

        Optional<User> optionalUser = getUserByVerificationCode(verificationCode);
        if (optionalUser.isEmpty()) {
            return AppResponseBuilder.error(ErrorCode.VERIFICATION_USER_NOT_FOUND,
                    "verification.user.not.found");
        }

        User user = optionalUser.get();
        if (isCodeExpired(user.getVerificationCodeExpiryDate())) {
            resendVerificationEmail(user);
            return AppResponseBuilder.error(ErrorCode.CODE_EXPIRED,"code.expired");
        }
        user.setIsVerified(true);
        user.setVerificationCode(null);
        user.setVerificationCodeExpiryDate(null);
        userRepository.save(user);

        logger.info("Verification successful for user: {} ", user.getEmail()
        );

        return AppResponseBuilder.success("account.verified");

    }

    public void resendVerificationEmail(User user) {
        user.setVerificationCode(SystemUtils.generateUUIDCode());
        user.setVerificationCodeExpiryDate(Instant.now()
                .plus(Duration.ofSeconds(systemPropertyService.
                        getIntegerProperty("app.verification.code.expiration"))));
        userRepository.save(user);
        emailService.sendAccountVerificationEmail(new UserRegisterEvent(user));
    }

    private Optional<User> getUserByVerificationCode(String verificationCode) {
        return userRepository.findByVerificationCode(verificationCode);
    }

    private boolean isCodeExpired(Instant expirationDate) {
        return Instant.now().isAfter(expirationDate);
    }
}
