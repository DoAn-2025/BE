package com.doan2025.webtoeic.service.impl;

import com.doan2025.webtoeic.exception.WebToeicException;
import com.doan2025.webtoeic.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional(rollbackOn = {WebToeicException.class, Exception.class})
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
}
