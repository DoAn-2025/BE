package com.doan2025.webtoeic.controller;

import com.doan2025.webtoeic.constants.enums.ResponseCode;
import com.doan2025.webtoeic.constants.enums.ResponseObject;
import com.doan2025.webtoeic.dto.request.AuthenticationRequest;
import com.doan2025.webtoeic.dto.request.RegisterRequest;
import com.doan2025.webtoeic.dto.response.ApiResponse;
import com.doan2025.webtoeic.dto.response.AuthenticationResponse;
import com.doan2025.webtoeic.security.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ApiResponse.of(ResponseCode.SUCCESS, ResponseObject.LOGIN, authenticationService.authenticate(request));
    }

    @PostMapping("/register")
    public ApiResponse<Void> register(@RequestBody RegisterRequest request){
        authenticationService.register(request);
        return ApiResponse.of(ResponseCode.SUCCESS, ResponseObject.REGISTER, null);
    }

    @GetMapping("/logout")
    public ApiResponse<String> logout(HttpServletRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.of(ResponseCode.SUCCESS, ResponseObject.LOGOUT, null);
    }

    @GetMapping("/refresh")
    public ApiResponse<AuthenticationResponse> refresh(HttpServletRequest request)
            throws ParseException, JOSEException {
        return ApiResponse.of(ResponseCode.SUCCESS, ResponseObject.REFRESH_TOKEN, authenticationService.refreshToken(request));
    }
}
