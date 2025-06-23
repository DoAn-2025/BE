package com.doan2025.webtoeic.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String themeUrl;
    private Date createdAt;
    private Date updatedAt;
    private Boolean isActive;
    private Boolean isDelete;
    private Boolean isAuthor;
    private UserResponse author;

    public PostResponse(Long id, String title, String content, String themeUrl, Date createdAt, Date updatedAt, Boolean isActive, Boolean isDelete, Boolean isAuthor) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.themeUrl = themeUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isActive = isActive;
        this.isDelete = isDelete;
        this.isAuthor = isAuthor;
    }

    public PostResponse(Long id, String title, String content, String themeUrl, Date createdAt, Date updatedAt, Boolean isActive, Boolean isDelete) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.themeUrl = themeUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isActive = isActive;
        this.isDelete = isDelete;
    }

    public PostResponse(Long id, String title, String content, String themeUrl, Date createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.themeUrl = themeUrl;
        this.createdAt = createdAt;
    }
}
