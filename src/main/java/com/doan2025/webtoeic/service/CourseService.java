package com.doan2025.webtoeic.service;

import com.doan2025.webtoeic.dto.SearchBaseDto;
import com.doan2025.webtoeic.dto.request.CourseRequest;
import com.doan2025.webtoeic.dto.response.CourseResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

public interface CourseService {

    CourseResponse getCourseDetail(HttpServletRequest httpServletRequest, Long id);
    List<CourseResponse> getCourses(SearchBaseDto dto, Pageable pageable);
    List<CourseResponse> getAllCourses(HttpServletRequest request, SearchBaseDto dto, Pageable pageable);
    List<CourseResponse> getOwnCourses(HttpServletRequest request, SearchBaseDto dto, Pageable pageable);

    CourseResponse createCourse(HttpServletRequest httpServletRequest,CourseRequest request);
    CourseResponse updateCourse(HttpServletRequest httpServletRequest,CourseRequest request);
    CourseResponse disableOrDeleteCourse(HttpServletRequest httpServletRequest,CourseRequest request);
}
