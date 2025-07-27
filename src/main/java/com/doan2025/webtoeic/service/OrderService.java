package com.doan2025.webtoeic.service;

import com.doan2025.webtoeic.dto.response.OrderResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface OrderService {

    List<OrderResponse> getOwnOrders(HttpServletRequest request);

    void cancelOrder(HttpServletRequest request, Long id);

    OrderResponse createOrderByCartItem(HttpServletRequest request, Long id);

    OrderResponse createOrderByCourseID(HttpServletRequest request, Long id);
}
