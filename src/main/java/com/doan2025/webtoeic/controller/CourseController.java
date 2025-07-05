package com.doan2025.webtoeic.controller;

import com.doan2025.webtoeic.constants.enums.ResponseCode;
import com.doan2025.webtoeic.constants.enums.ResponseObject;
import com.doan2025.webtoeic.dto.request.CourseRequest;
import com.doan2025.webtoeic.dto.response.ApiResponse;
import com.doan2025.webtoeic.service.CourseService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @PostMapping("/update-status")
    @PreAuthorize("hasRole('CONSULTANT') OR hasRole('MANAGER')")
    public ApiResponse<?> updateStatusCourse(HttpServletRequest request, @RequestBody CourseRequest course) {
        return ApiResponse.of(ResponseCode.CREATE_SUCCESS, ResponseObject.COURSE, courseService.disableOrDeleteCourse(request,course));
    }

    @PostMapping("/update-info")
    @PreAuthorize("hasRole('CONSULTANT') OR hasRole('MANAGER')")
    public ApiResponse<?> updateInfoCourse(HttpServletRequest request, @RequestBody CourseRequest course) {
        return ApiResponse.of(ResponseCode.CREATE_SUCCESS, ResponseObject.COURSE, courseService.updateCourse(request,course));
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('CONSULTANT') OR hasRole('MANAGER')")
    public ApiResponse<?> createCourse(HttpServletRequest request, @RequestBody CourseRequest course) {
        return ApiResponse.of(ResponseCode.CREATE_SUCCESS, ResponseObject.COURSE, courseService.createCourse(request,course));
    }

}
