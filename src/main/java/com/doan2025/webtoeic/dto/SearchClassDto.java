package com.doan2025.webtoeic.dto;

import lombok.Data;

import java.util.List;

@Data
public class SearchClassDto {
    private String searchString; // tìm theo title, name, subject,
    private Long idTeacher;
    private List<Long> statusClass;
}
