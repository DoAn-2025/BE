package com.doan2025.webtoeic.utils;

import com.doan2025.webtoeic.constants.enums.ERole;
import com.doan2025.webtoeic.constants.enums.ResponseCode;
import com.doan2025.webtoeic.constants.enums.ResponseObject;
import com.doan2025.webtoeic.domain.Class;
import com.doan2025.webtoeic.domain.*;
import com.doan2025.webtoeic.dto.response.*;
import com.doan2025.webtoeic.exception.WebToeicException;
import com.doan2025.webtoeic.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


@Component
@RequiredArgsConstructor
public class ConvertUtil {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public ClassResponse convertClassToDto(HttpServletRequest request, Class clazz) {
        ClassResponse classResponse = ClassResponse.builder()
                .id(clazz.getId())
                .name(clazz.getName())
                .description(clazz.getDescription())
                .title(clazz.getTitle())
                .createdAt(clazz.getCreatedAt())
                .updatedAt(clazz.getUpdatedAt())
                .createdByName(clazz.getCreatedBy().getFirstName() + " " + clazz.getCreatedBy().getLastName())
                .updatedByName(clazz.getUpdatedBy() == null ? null : clazz.getUpdatedBy().getFirstName() + " " + clazz.getUpdatedBy().getLastName())
                .teacher(modelMapper.map(clazz.getTeacher(), UserResponse.class))
                .build();

        return classResponse;
    }

    public OrderResponse convertOrderToDto(HttpServletRequest request, Orders order, OrderDetail orderDetail) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(order.getId());
        orderResponse.setStatus(order.getStatus());
        orderResponse.setPaymentMethod(order.getPaymentMethod());
        orderResponse.setCreatedAt(order.getCreatedAt());
        orderResponse.setUpdatedAt(order.getUpdatedAt());
        OrderDetailResponse detail = new OrderDetailResponse();
        detail.setId(orderDetail.getId());
        detail.setPriceAtPurchase(orderDetail.getPriceAtPurchase());
        CourseResponse courseResponse = convertCourseToDto(request, orderDetail.getCourse());
        courseResponse.setAuthor(null);
        courseResponse.setCreatedBy(null);
        courseResponse.setUpdatedBy(null);
        detail.setCourse(courseResponse);
        orderResponse.setDetail(convertOrderDetailToDto(request, orderDetail));
        return orderResponse;
    }

    public OrderDetailResponse convertOrderDetailToDto(HttpServletRequest request, OrderDetail orderDetail) {
        OrderDetailResponse detail = new OrderDetailResponse();
        detail.setId(orderDetail.getId());
        detail.setPriceAtPurchase(orderDetail.getPriceAtPurchase());
        CourseResponse courseResponse = convertCourseToDto(request, orderDetail.getCourse());
        courseResponse.setAuthor(null);
        courseResponse.setCreatedBy(null);
        courseResponse.setUpdatedBy(null);
        detail.setCourse(courseResponse);
        return detail;
    }

    public CartItemResponse convertCartItemToDto(HttpServletRequest httpServletRequest, CartItem cartItem) {
        CartItemResponse cartItemResponse = new CartItemResponse();
        cartItemResponse.setId(cartItem.getId());
        CourseResponse courseResponse = convertCourseToDto(httpServletRequest, cartItem.getCourse());
        courseResponse.setAuthor(null);
        courseResponse.setCreatedBy(null);
        courseResponse.setUpdatedBy(null);
        cartItemResponse.setCourse(courseResponse);
        return cartItemResponse;
    }

    public LessonResponse convertLessonToDto(HttpServletRequest httpServletRequest, Lesson lesson) {
        LessonResponse lessonResponse = new LessonResponse();
        lessonResponse.setId(lesson.getId());
        lessonResponse.setTitle(lesson.getTitle());
        lessonResponse.setContent(lesson.getContent());
        lessonResponse.setVideoUrl(lesson.getVideoUrl());
        lessonResponse.setOrderIndex(lesson.getOrderIndex());
        lessonResponse.setCreatedAt(lesson.getCreatedAt());
        lessonResponse.setUpdatedAt(lesson.getUpdatedAt());
        lessonResponse.setIsPreviewAble(lesson.getIsPreviewAble());
        lessonResponse.setIsDeleted(lesson.getIsDelete());
        lessonResponse.setIsActive(lesson.getIsActive());
        lessonResponse.setDuration(lesson.getDuration());
        lessonResponse.setOrderIndex(lesson.getOrderIndex());
        lessonResponse.setCreatedBy(modelMapper.map(lesson.getCreatedBy(), UserResponse.class));
        if (lesson.getUpdatedBy() != null) {
            lessonResponse.setUpdatedBy(modelMapper.map(lesson.getUpdatedBy(), UserResponse.class));
        }
        if (lesson.getUpdatedAt() != null) {
            lessonResponse.setUpdatedAt(lesson.getUpdatedAt());
        }
        String email = "";
        String bearerToken = httpServletRequest.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            email = jwtUtil.getEmailFromToken(httpServletRequest);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.USER));
            if (user.getRole().equals(ERole.MANAGER) || user.getRole().equals(ERole.CONSULTANT)) {
                lessonResponse.setIsBought(true);
            }
            if (lesson.getCourse().getEnrollments() != null && !lesson.getCourse().getEnrollments().isEmpty()) {
                for (Enrollment enrollment : lesson.getCourse().getEnrollments()) {
                    if (enrollment.getUser().equals(user)) {
                        lessonResponse.setIsBought(true);
                    }
                }
            }
        }
        return lessonResponse;
    }

    public CourseResponse convertCourseToDto(HttpServletRequest request, Course course) {
        CourseResponse courseResponse = new CourseResponse();
        courseResponse.setId(course.getId());
        courseResponse.setTitle(course.getTitle());
        courseResponse.setDescription(course.getDescription());
        courseResponse.setPrice(course.getPrice());
        courseResponse.setIsActive(course.getIsActive());
        courseResponse.setIsDelete(course.getIsDelete());
        courseResponse.setCreatedAt(course.getCreatedAt());
        courseResponse.setUpdatedAt(course.getUpdatedAt());
        courseResponse.setIsBought(false);
        courseResponse.setAuthor(course.getAuthor() != null ? modelMapper.map(course.getAuthor(), UserResponse.class) : null);
        courseResponse.setCreatedBy(course.getCreatedBy() != null ? modelMapper.map(course.getCreatedBy(), UserResponse.class) : null);
        courseResponse.setUpdatedBy(course.getUpdatedBy() != null ? modelMapper.map(course.getUpdatedBy(), UserResponse.class) : null);
        courseResponse.setAuthorName(courseResponse.getAuthor().getFirstName() + " " + courseResponse.getAuthor().getLastName());
        courseResponse.setCreatedByName(courseResponse.getCreatedBy().getFirstName() + " " + courseResponse.getCreatedBy().getLastName());
        String email = "";
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            email = jwtUtil.getEmailFromToken(request);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.USER));
            if (user.getRole().equals(ERole.MANAGER) || user.getRole().equals(ERole.CONSULTANT)) {
                courseResponse.setIsBought(true);
            }
            if (course.getEnrollments() != null && !course.getEnrollments().isEmpty()) {
                for (Enrollment enrollment : course.getEnrollments()) {
                    if (enrollment.getUser().equals(user)) {
                        courseResponse.setIsBought(true);
                    }
                }
            }
        }
        return courseResponse;
    }
}
