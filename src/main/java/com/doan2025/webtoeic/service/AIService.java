package com.doan2025.webtoeic.service;

import com.doan2025.webtoeic.dto.response.QuestionResponse;

import java.util.List;

public interface AIService {
    String checkCallAI();

    List<QuestionResponse> analysisWithAI(String url);
}
