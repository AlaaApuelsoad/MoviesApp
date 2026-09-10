package com.alaa.MoviesApp.utils;

import com.alaa.MoviesApp.service.MessageService;
import org.springframework.stereotype.Component;

@Component
public class AppResponseBuilderInitializer {

    public AppResponseBuilderInitializer(MessageService messageService) {
        AppResponseBuilder.setMessageService(messageService);
    }
}
