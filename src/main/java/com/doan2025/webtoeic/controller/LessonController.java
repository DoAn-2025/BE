package com.doan2025.webtoeic.controller;

import com.doan2025.webtoeic.constants.enums.ResponseCode;
import com.doan2025.webtoeic.constants.enums.ResponseObject;
import com.doan2025.webtoeic.domain.Lesson;
import com.doan2025.webtoeic.dto.request.LessonRequest;
import com.doan2025.webtoeic.dto.response.ApiResponse;
import com.doan2025.webtoeic.service.LessonService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/lesson")
@RequiredArgsConstructor
public class LessonController {
    private final LessonService lessonService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('CONSULTANT') OR hasRole('MANAGER')")
    public ApiResponse<?> createLesson(HttpServletRequest request, @RequestBody LessonRequest lesson) {
        return ApiResponse.of(ResponseCode.CREATE_SUCCESS, ResponseObject.LESSON, lessonService.createLesson(request, lesson) );
    }
}
