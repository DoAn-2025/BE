package com.doan2025.webtoeic.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {
    @JsonProperty("id")
    private Long id;
    private String email;
    private String password;
    private String oldPassword;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    private String dob;
    private Integer gender;
    private String avatarUrl;
    private Boolean isActive;
    private Boolean isDelete;
    private String education;
    private String major;
}
