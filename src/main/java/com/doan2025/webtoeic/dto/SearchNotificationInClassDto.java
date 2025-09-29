package com.doan2025.webtoeic.dto;

import lombok.Data;

import java.util.Date;

@Data
public class SearchNotificationInClassDto {
    private Long classId;
    private String searchString;
    private Date fromDate;
    private Date toDate;
}
