package com.alaa.MoviesApp.configuration;

import org.jspecify.annotations.NullMarked;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

/**
 * MDC is a TaskDecorator is the standard way to propagate MDC values from the submitting thread to a worker thread
 * when use @Async, ThreadPoolTaskExecutor, CompletableFuture.
 */
public class MDCTaskDecorator implements TaskDecorator {
    @Override
    @NullMarked
    public Runnable decorate(Runnable runnable) {

        Map<String,String> contextMap = MDC.getCopyOfContextMap();

        return () -> {
            try {
                if (contextMap != null) {
                    MDC.setContextMap(contextMap);
                } else {
                    MDC.clear();
                }
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}
