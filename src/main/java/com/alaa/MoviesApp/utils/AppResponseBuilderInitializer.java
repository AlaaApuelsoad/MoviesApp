package com.alaa.moviesapp.utils;

import com.alaa.moviesapp.service.MessageService;
import org.springframework.stereotype.Component;

@Component
public class AppResponseBuilderInitializer {

    public AppResponseBuilderInitializer(MessageService messageService) {
        AppResponseBuilder.setMessageService(messageService);
    }
}
