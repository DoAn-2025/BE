package com.doan2025.webtoeic.dto.response;

import com.doan2025.webtoeic.constants.enums.EGender;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
//@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dob;
    private String gender;
    private String avatarUrl;
    private Boolean isActive;
    private Boolean isDelete;
    private String education;
    private String major;
    private StudentResponse student;

    public UserResponse(Long id, String firstName, String lastName, String phone, String address, Date dob, EGender gender, String avatarUrl, Boolean isActive, Boolean isDelete, String education, String major) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
        this.dob = dob;
        this.gender = gender != null ? gender.name() : null;
        this.avatarUrl = avatarUrl;
        this.isActive = isActive;
        this.isDelete = isDelete;
        this.education = education;
        this.major = major;
    }

    public UserResponse(String firstName, String lastName, String phone, String address, Date dob, EGender gender, String avatarUrl, Boolean isActive, Boolean isDelete, String education, String major) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
        this.dob = dob;
        this.gender = gender != null ? gender.name() : null;
        this.avatarUrl = avatarUrl;
        this.isActive = isActive;
        this.isDelete = isDelete;
        this.education = education;
        this.major = major;
    }
}
