package com.doan2025.webtoeic.controller;

import com.doan2025.webtoeic.constants.enums.ResponseCode;
import com.doan2025.webtoeic.constants.enums.ResponseObject;
import com.doan2025.webtoeic.dto.response.ApiResponse;
import com.doan2025.webtoeic.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ApiResponse<?> getOwnOrders(HttpServletRequest request) {
        return ApiResponse.of(ResponseCode.GET_SUCCESS, ResponseObject.ORDER, orderService.getOwnOrders(request));
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('STUDENT')")
    public ApiResponse<Void> deleteOrder(HttpServletRequest request, @RequestParam List<Long> id) {
        orderService.cancelOrder(request, id);
        return ApiResponse.of(ResponseCode.DELETE_SUCCESS, ResponseObject.ORDER, null);
    }


    @PostMapping("/create-order-by-cart-item")
    @PreAuthorize("hasRole('STUDENT')")
    public ApiResponse<?> createOrderByCartItem(HttpServletRequest request, @RequestParam("id") Long id) {
        return ApiResponse.of(ResponseCode.CREATE_SUCCESS, ResponseObject.ORDER, orderService.createOrderByCartItem(request, id));
    }

    @PostMapping("/create-order-by-course")
    @PreAuthorize("hasRole('STUDENT')")
    public ApiResponse<?> createOrderByCourseID(HttpServletRequest request, @RequestParam("id") Long id) {
        return ApiResponse.of(ResponseCode.CREATE_SUCCESS, ResponseObject.ORDER, orderService.createOrderByCourseID(request, id));

    }
}
