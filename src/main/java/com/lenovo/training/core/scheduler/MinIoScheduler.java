package com.lenovo.training.core.scheduler;

import com.lenovo.training.core.service.MinIoService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@EnableScheduling
public class MinIoScheduler {

    private MinIoService minIoService;

    @Scheduled(cron = "0 59 23 ? * *")
    public void execute() {
        minIoService.uploadFile();
    }
}
