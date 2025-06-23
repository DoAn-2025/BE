package com.doan2025.webtoeic.dto.request;

import lombok.Data;

import java.util.Date;

@Data
public class PostRequest {
    private Long id;
    private String title;
    private String content;
    private String themeUrl;
    private Date createdAt;
    private Date updatedAt;
    private Boolean isActive;
    private Boolean isDelete;
    private Long categoryId;
}
