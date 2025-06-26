package com.doan2025.webtoeic.service.impl;

import com.doan2025.webtoeic.constants.enums.ECategoryPost;
import com.doan2025.webtoeic.constants.enums.EGender;
import com.doan2025.webtoeic.constants.enums.ERole;
import com.doan2025.webtoeic.dto.response.CategoryResponse;
import com.doan2025.webtoeic.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    public List<CategoryResponse> getCategoryPost() {
        List<CategoryResponse> responses = new ArrayList<>();
        for(ECategoryPost e : ECategoryPost.values()) {
            CategoryResponse categoryResponse = new CategoryResponse();
            categoryResponse.setId(e.getValue());
            categoryResponse.setName(e.getName().toUpperCase());
            responses.add(categoryResponse);
        }
        return responses;
    }

    @Override
    public List<CategoryResponse> getCategoryGender() {
        List<CategoryResponse> responses = new ArrayList<>();
        for(EGender e : EGender.values()) {
            CategoryResponse categoryResponse = new CategoryResponse();
            categoryResponse.setId(e.getValue());
            categoryResponse.setName(e.getName().toUpperCase());
            responses.add(categoryResponse);
        }
        return responses;
    }

    @Override
    public List<CategoryResponse> getCategoryRole() {
        List<CategoryResponse> responses = new ArrayList<>();
        for(ERole e : ERole.values()) {
            CategoryResponse categoryResponse = new CategoryResponse();
            categoryResponse.setId(e.getValue());
            categoryResponse.setName(e.getCode().toUpperCase());
            responses.add(categoryResponse);
        }
        return responses;
    }
}
