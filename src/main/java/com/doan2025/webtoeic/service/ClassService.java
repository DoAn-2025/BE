package com.doan2025.webtoeic.service;

import com.doan2025.webtoeic.dto.SearchClassDto;
import com.doan2025.webtoeic.dto.request.ClassRequest;
import com.doan2025.webtoeic.dto.response.ClassResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface ClassService {

    ClassResponse get(HttpServletRequest httpServletRequest, Long classId);

    List<ClassResponse> getClasses(HttpServletRequest httpServletRequest, SearchClassDto dto);

    void deleteClass(List<Long> ids, HttpServletRequest httpServletRequest);

    ClassResponse updateClass(ClassRequest classRequest, HttpServletRequest httpServletRequest);

    ClassResponse createClass(HttpServletRequest request, ClassRequest classRequest);
}
