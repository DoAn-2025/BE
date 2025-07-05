package com.doan2025.webtoeic.dto.response;

import lombok.Data;

@Data
public class LessonResponse {
    private Long id;
    private String title;
    private String videoUrl;
    private Integer durationSeconds;
    private boolean isPreviewable;
}
