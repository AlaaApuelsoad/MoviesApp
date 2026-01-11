package com.alaa.MoviesApp.service;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.IOException;
import java.io.StringWriter;
import java.io.StringReader;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Template;
import org.springframework.stereotype.Service;

@Service
public class EmailService2 {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private Configuration freeMarkerConfigurer;

    private String prepareEmailBody() throws IOException, TemplateException {
        // Layout template
        Template emailLayoutTemplate = freeMarkerConfigurer.getTemplate("email-layout.ftl");
        Map<String, String> emailLayoutInput = new HashMap<>();


        // Static English & Arabic content
        emailLayoutInput.put("englishContent",
                "<h2>Welcome to Fawry Movies!</h2>" +
                        "<p>Thank you for joining us! Please verify your account to start enjoying our services.</p>"
        );

        emailLayoutInput.put("arabicContent",
                "<h2>مرحباً بك في فوري موفيز!</h2>" +
                        "<p>شكراً لانضمامك إلينا! يرجى تفعيل حسابك للبدء في استخدام خدماتنا.</p>"
        );

        StringWriter stringWriter = new StringWriter();
        emailLayoutTemplate.process(emailLayoutInput, stringWriter);

        return stringWriter.toString();
    }

    public void sendEmail() throws MessagingException, IOException, TemplateException {
        String emailBody = prepareEmailBody();

        // Send email
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo("alaaapu135@gmail.com");
        helper.setSubject("Verify Your Account - Fawry Movies");
        helper.setText(emailBody, true);
        ClassPathResource logo = new ClassPathResource("static/4-2-Fawry.png");
        helper.addInline("logoImage", logo);

        mailSender.send(message);
    }
}
