package com.doan2025.webtoeic.service.impl;

import com.doan2025.webtoeic.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    @Value("${payment.vn-pay.vnp_TmnCode}")
    private String VNP_TMN_CODE;
    @Value("${payment.vn-pay.vnp_HashSecret}")
    private String VNP_HASH_SECRET;
    @Value("${payment.vn-pay.vnp_Url}")
    private String VNP_URL;


    @Override
    public void createVNPayPayment(int amount, HttpServletRequest request) {

    }

    @Override
    public RedirectView handleVNPayReturn(HttpServletRequest request) {
        return null;
    }
}
