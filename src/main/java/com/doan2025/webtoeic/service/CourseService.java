package com.doan2025.webtoeic.service;

import com.doan2025.webtoeic.dto.request.CourseRequest;
import com.doan2025.webtoeic.dto.response.CourseResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseService {

    CourseResponse createCourse(HttpServletRequest httpServletRequest,CourseRequest request);
    CourseResponse updateCourse(HttpServletRequest httpServletRequest,CourseRequest request);
    CourseResponse disableOrDeleteCourse(HttpServletRequest httpServletRequest,CourseRequest request);
    CourseResponse getCourseDetail(HttpServletRequest httpServletRequest, Long id);
    Page<CourseResponse> filterCourses(HttpServletRequest httpServletRequest,CourseRequest request, Pageable pageable);
}
