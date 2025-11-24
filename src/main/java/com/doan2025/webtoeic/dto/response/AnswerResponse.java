package com.doan2025.webtoeic.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AnswerResponse {
    private Long id;
    private String content; // Nội dung câu trả lời
    private boolean isCorrect;
    private UserResponse createdBy;
    private UserResponse updatedBy;
    private Boolean isActive;
    private Boolean isDeleted;
    private Date createdAt;
    private Date updatedAt;
}
