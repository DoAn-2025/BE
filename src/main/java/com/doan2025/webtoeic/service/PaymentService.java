package com.doan2025.webtoeic.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.view.RedirectView;

public interface PaymentService {
    void createVNPayPayment(int amount, HttpServletRequest request);

    RedirectView handleVNPayReturn(HttpServletRequest request);
}
