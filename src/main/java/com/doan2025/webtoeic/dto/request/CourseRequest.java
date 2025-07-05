package com.doan2025.webtoeic.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourseRequest {
    private Long id;
    @NotBlank(message = "Tên khóa học không được để trống")
    private String title;
    private String description;
    @NotNull(message = "Giá không được để trống")
    @PositiveOrZero(message = "Giá phải lớn hơn hoặc bằng 0")
    private BigDecimal price;
    private Long authorId;
    @NotNull(message = "Danh mục không được để trống")
    private Integer categoryId;
    private String thumbnailUrl;
    private Boolean isActive;
    private Boolean isDelete;
}
