package com.doan2025.webtoeic.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExplanationQuestionResponse {
    private String explanationVietnamese;
    private String explanationEnglish;
}
