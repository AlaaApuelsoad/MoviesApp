package com.fawry.MoviesApp.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class EmailTemplateUtility {

    @Value("${app.template.Account.Verification}")
    private String accountVerificationTemplate;

    public String accountVerificationEmailBuild(String verificationLink) throws IOException {
        Path path = Paths.get(accountVerificationTemplate);
        String templateContent = Files.readString(path);
        return templateContent.replace("{{verification_link}}",verificationLink);
    }



}
