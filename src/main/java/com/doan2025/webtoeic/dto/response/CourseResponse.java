package com.doan2025.webtoeic.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class CourseResponse {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private String thumbnailUrl;
    private String categoryName;
    private String status;
    private Date updatedAt;
    private Date createdAt;
    private Boolean isDelete;
    private Boolean isActive;
    private UserResponse author;
    private UserResponse createdBy;
    private UserResponse updatedBy;
    private List<LessonResponse> lessons;
    private Boolean isBought;
}
