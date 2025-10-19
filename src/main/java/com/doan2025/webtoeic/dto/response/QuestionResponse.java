package com.doan2025.webtoeic.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestionResponse {
    private String questionContent;
    private String category;
    private String difficulty;
    private List<AnswerResponse> answers;
    private ExplanationQuestionResponse explanation;
}
