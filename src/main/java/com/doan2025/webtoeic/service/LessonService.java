package com.doan2025.webtoeic.service;

import com.doan2025.webtoeic.domain.Lesson;
import com.doan2025.webtoeic.dto.request.LessonRequest;
import com.doan2025.webtoeic.dto.response.LessonResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface LessonService {
    LessonResponse createLesson(HttpServletRequest request, LessonRequest lesson);
}
