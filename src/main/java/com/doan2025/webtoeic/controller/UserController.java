package com.doan2025.webtoeic.controller;

import com.doan2025.webtoeic.constants.enums.ResponseCode;
import com.doan2025.webtoeic.constants.enums.ResponseObject;
import com.doan2025.webtoeic.dto.request.UserRequest;
import com.doan2025.webtoeic.dto.response.ApiResponse;
import com.doan2025.webtoeic.dto.response.UserResponse;
import com.doan2025.webtoeic.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @GetMapping()
    public ApiResponse<UserResponse> getUserCurrent(HttpServletRequest request) {
        return ApiResponse.of(ResponseCode.GET_SUCCESS, ResponseObject.USER, userService.getUserCurrent(request));
    }

    @PostMapping()
    public ApiResponse<UserResponse> getUserDetails(HttpServletRequest request,@RequestBody UserRequest userRequest) {
        return ApiResponse.of(ResponseCode.SUCCESS, ResponseObject.USER, userService.getUserDetails(userRequest) );
    }

    @PostMapping(value = "/update")
    public ApiResponse<UserResponse> updateUserDetails(HttpServletRequest request,@RequestBody UserRequest userRequest) {
        return ApiResponse.of(ResponseCode.SUCCESS, ResponseObject.USER, userService.updateUserDetails(request,userRequest));
    }
}
