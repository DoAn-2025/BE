package com.doan2025.webtoeic.controller;

import com.doan2025.webtoeic.constants.enums.ResponseCode;
import com.doan2025.webtoeic.constants.enums.ResponseObject;
import com.doan2025.webtoeic.dto.response.ApiResponse;
import com.doan2025.webtoeic.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping()
    @PreAuthorize("hasRole('STUDENT')")
    public ApiResponse<?> getCart(HttpServletRequest request) {
        return ApiResponse.of(ResponseCode.GET_SUCCESS, ResponseObject.USER, cartService.getInCart(request));
    }

    @PostMapping("/add-to-cart")
    @PreAuthorize("hasRole('STUDENT')")
    public ApiResponse<Void> addToCart(HttpServletRequest request, @RequestParam("id") Long id) {
        cartService.addToCart(request, id);
        return ApiResponse.of(ResponseCode.CREATE_SUCCESS, ResponseObject.USER, null);
    }

    @DeleteMapping("/remove-from-cart")
    @PreAuthorize("hasRole('STUDENT')")
    public ApiResponse<Void> removeFromCart(HttpServletRequest request, @RequestParam("id") Long id) {
        cartService.removeFromCart(request, id);
        return ApiResponse.of(ResponseCode.CREATE_SUCCESS, ResponseObject.USER, null);
    }
}
