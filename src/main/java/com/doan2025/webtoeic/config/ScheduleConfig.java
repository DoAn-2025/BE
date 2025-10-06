package com.doan2025.webtoeic.config;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduleConfig {

    @Scheduled(cron = "0 0 0 * * *") // Chạy vào lúc 12h đêm mỗi ngày
    public void schedule() {

    }
}
