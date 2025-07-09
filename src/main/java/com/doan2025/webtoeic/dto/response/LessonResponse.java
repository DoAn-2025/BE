package com.doan2025.webtoeic.dto.response;

import lombok.Data;

@Data
public class LessonResponse {
    private Long id;
    private String title;
    private String content;
    private String videoUrl;
    private Integer duration;
    private Integer orderIndex;
    private Boolean isPreviewable;
    private CourseResponse courseId;
}
