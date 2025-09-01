package com.doan2025.webtoeic.service.impl;

import com.doan2025.webtoeic.constants.enums.EPaymentMethod;
import com.doan2025.webtoeic.constants.enums.EStatusOrder;
import com.doan2025.webtoeic.constants.enums.ResponseCode;
import com.doan2025.webtoeic.constants.enums.ResponseObject;
import com.doan2025.webtoeic.domain.*;
import com.doan2025.webtoeic.dto.response.OrderResponse;
import com.doan2025.webtoeic.exception.WebToeicException;
import com.doan2025.webtoeic.repository.*;
import com.doan2025.webtoeic.service.OrderService;
import com.doan2025.webtoeic.utils.ConvertUtil;
import com.doan2025.webtoeic.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackOn = {WebToeicException.class, Exception.class})
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final JwtUtil jwtUtil;
    private final CartItemRepository cartItemRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ConvertUtil convertUtil;

    @Override
    public List<OrderResponse> getOwnOrders(HttpServletRequest request) {

        String email = jwtUtil.getEmailFromToken(request);

        List<Orders> orders = orderRepository.findByEmail(email)
                .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.ORDER));

        List<OrderResponse> orderResponses = new ArrayList<>();

        for (Orders order : orders) {
            OrderResponse orderResponse = convertUtil.convertOrderToDto(request, order,
                    orderDetailRepository.findByOrderId(order.getId())
                            .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.ORDER)));
            orderResponses.add(orderResponse);
        }
        return orderResponses;
    }

    @Override
    public void cancelOrder(HttpServletRequest request, List<Long> ids) {
        User user = userRepository.findByEmail(jwtUtil.getEmailFromToken(request))
                .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.USER));
        for (Long id : ids) {
            Orders order = orderRepository.findById(id)
                    .orElseThrow(() -> new WebToeicException(ResponseCode.CANNOT_GET, ResponseObject.ORDER));
            if (!order.getUser().getEmail().equals(user.getEmail())) {
                throw new WebToeicException(ResponseCode.NOT_PERMISSION, ResponseObject.USER);
            }
            orderDetailRepository.deleteByOrders(order);
            orderRepository.delete(order);
        }
    }

    @Override
    public OrderResponse createOrderByCartItem(HttpServletRequest request, Long id) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new WebToeicException(ResponseCode.CANNOT_GET, ResponseObject.CART_ITEM));
        User user = userRepository.findByEmail(jwtUtil.getEmailFromToken(request))
                .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.USER));
        if (orderDetailRepository.existsByUserAndCourse(user.getEmail(), cartItem.getCourse().getId())) {
            throw new WebToeicException(ResponseCode.EXISTED, ResponseObject.ORDER);
        }
        if (enrollmentRepository.existsByUserAndCourse(user, cartItem.getCourse())) {
            throw new WebToeicException(ResponseCode.EXISTED, ResponseObject.ENROLLMENT);
        }
        Orders order = Orders.builder()
                .paymentMethod(EPaymentMethod.VN_PAY)
                .status(EStatusOrder.PENDING)
                .user(user)
                .totalAmount(cartItem.getCourse().getPrice())
                .build();
        Orders savedOrder = orderRepository.save(order);

        OrderDetail orderDetail = OrderDetail.builder()
                .orders(savedOrder)
                .course(cartItem.getCourse())
                .priceAtPurchase(cartItem.getCourse().getPrice())
                .build();
        OrderDetail savedOrderDetail = orderDetailRepository.save(orderDetail);
        cartItemRepository.deleteById(cartItem.getId());
        return convertUtil.convertOrderToDto(request, savedOrder, savedOrderDetail);
    }

    @Override
    public OrderResponse createOrderByCourseID(HttpServletRequest request, Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.COURSE));

        User user = userRepository.findByEmail(jwtUtil.getEmailFromToken(request))
                .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.USER));

        if (orderDetailRepository.existsByUserAndCourse(user.getEmail(), course.getId())) {
            throw new WebToeicException(ResponseCode.EXISTED, ResponseObject.ORDER);
        }
        if (enrollmentRepository.existsByUserAndCourse(user, course)) {
            throw new WebToeicException(ResponseCode.EXISTED, ResponseObject.ENROLLMENT);
        }
        Orders order = Orders.builder()
                .paymentMethod(EPaymentMethod.VN_PAY)
                .status(EStatusOrder.PENDING)
                .user(user)
                .totalAmount(course.getPrice())
                .build();
        Orders savedOrder = orderRepository.save(order);

        OrderDetail orderDetail = OrderDetail.builder()
                .orders(savedOrder)
                .course(course)
                .priceAtPurchase(course.getPrice())
                .build();
        OrderDetail savedOrderDetail = orderDetailRepository.save(orderDetail);

        return convertUtil.convertOrderToDto(request, savedOrder, savedOrderDetail);
    }
}
