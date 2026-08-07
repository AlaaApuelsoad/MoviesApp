package com.alaa.moviesapp.service;

import com.alaa.moviesapp.dto.AppResponse;
import com.alaa.moviesapp.listener.UserRegisterEvent;
import com.alaa.moviesapp.model.User;
import com.alaa.moviesapp.repository.UserRepository;
import com.alaa.moviesapp.utils.AppResponseBuilder;
import com.alaa.moviesapp.utils.SystemUtils;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerifyService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private static final Logger logger = LogManager.getLogger(VerifyService.class);

    public AppResponse<String> verifyUser(String verificationCode) {

        long startTime = System.currentTimeMillis();

        if (verificationCode == null || verificationCode.trim().isEmpty()) {
            return AppResponseBuilder.buildResponse(false,null,"Invalid Verification Code",
                    HttpStatus.BAD_REQUEST,null,null);
        }

        Optional<User> optionalUser = getUserByVerificationCode(verificationCode);
        if (optionalUser.isEmpty()) {
            return AppResponseBuilder.buildResponse(false,null,"No user found with this verification code",
                    HttpStatus.BAD_REQUEST,null,null);
        }

        User user = optionalUser.get();
        if (isCodeExpired(user.getVerificationCodeExpiryDate())) {
            resendVerificationEmail(user);
            return AppResponseBuilder.buildResponse(false,null,"Code Expired",
                    HttpStatus.BAD_REQUEST,null,null);
        }
        user.setIsVerified(true);
        user.setVerificationCode(null);
        user.setVerificationCodeExpiryDate(null);
        userRepository.save(user);

        long totalTime = System.currentTimeMillis() - startTime;

        logger.info("Verification successful for user: {} Total time: {}ms", user.getEmail(), totalTime
        );

        return AppResponseBuilder.buildResponse(true,null, "Account verified successfully",
                HttpStatus.OK,null,null);
    }

    public void resendVerificationEmail(User user) {
        user.setVerificationCode(SystemUtils.generateUUIDCode());
        user.setVerificationCodeExpiryDate(LocalDateTime.now().plusSeconds(10));
        userRepository.save(user);
        emailService.sendAccountVerificationEmail(new UserRegisterEvent(user));
    }

    private Optional<User> getUserByVerificationCode(String verificationCode) {
        return userRepository.findByVerificationCode(verificationCode);
    }

    private boolean isCodeExpired(LocalDateTime codeExpireDate) {
        return LocalDateTime.now().isAfter(codeExpireDate);
    }
}
