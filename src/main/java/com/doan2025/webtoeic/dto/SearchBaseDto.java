package com.doan2025.webtoeic.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
public class SearchBaseDto {
    public String searchString;
    public String email;
    public String name;
    public String phone;
    public Boolean isActive;
    public Boolean isDelete;
    public List<String> userRoles;
    public Date fromDate;
    public Date toDate;
    public String title;
    public List<String> categoryPost;
}
