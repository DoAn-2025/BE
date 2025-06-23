package com.doan2025.webtoeic.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
public class SearchBaseDto {
    public String email;
    public Date fromDate;
    public Date toDate;
    public String title;
    public List<String> categoryPost;
}
