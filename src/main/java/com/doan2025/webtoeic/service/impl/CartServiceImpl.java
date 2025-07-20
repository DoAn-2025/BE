package com.doan2025.webtoeic.service.impl;

import com.doan2025.webtoeic.constants.enums.ResponseCode;
import com.doan2025.webtoeic.constants.enums.ResponseObject;
import com.doan2025.webtoeic.domain.CartItem;
import com.doan2025.webtoeic.domain.Course;
import com.doan2025.webtoeic.domain.User;
import com.doan2025.webtoeic.dto.response.CartItemResponse;
import com.doan2025.webtoeic.dto.response.CourseResponse;
import com.doan2025.webtoeic.exception.WebToeicException;
import com.doan2025.webtoeic.repository.CartRepository;
import com.doan2025.webtoeic.repository.CourseRepository;
import com.doan2025.webtoeic.repository.UserRepository;
import com.doan2025.webtoeic.service.CartService;
import com.doan2025.webtoeic.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackOn = {WebToeicException.class, Exception.class})
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;
    private final JwtUtil jwtUtil;

    @Override
    public void addToCart(HttpServletRequest httpServletRequest, Long idCourse) {
        User user = userRepository.findByEmail(jwtUtil.getEmailFromToken(httpServletRequest))
                .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.USER));
        Course course = courseRepository.findById(idCourse)
                .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.COURSE));
        CartItem cartItem = new CartItem();
        cartItem.setCourse(course);
        cartItem.setUser(user);
        cartRepository.save(cartItem);
    }

    @Override
    public void removeFromCart(HttpServletRequest httpServletRequest, Long idCartItem) {
        User user = userRepository.findByEmail(jwtUtil.getEmailFromToken(httpServletRequest))
                .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.USER));
        CartItem cartItem = cartRepository.findById(idCartItem)
                .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.CART_ITEM));
        if (!cartItem.getUser().getId().equals(user.getId())) {
            throw new WebToeicException(ResponseCode.NOT_PERMISSION, ResponseObject.USER);
        }
        cartRepository.deleteById(idCartItem);
    }

    @Override
    public List<CartItemResponse> getInCart(HttpServletRequest httpServletRequest) {
        String email = jwtUtil.getEmailFromToken(httpServletRequest);
        List<CartItemResponse> result = new ArrayList<>();
        for (CartItem cartItem : cartRepository.findByEmailUser(email)) {
            result.add(convertToCartItemResponse(cartItem));
        }
        return result;
    }

    private CartItemResponse convertToCartItemResponse(CartItem cartItem) {
        CartItemResponse cartItemResponse = new CartItemResponse();
        cartItemResponse.setId(cartItem.getId());
        cartItemResponse.setCourse(modelMapper.map(cartItem.getCourse(), CourseResponse.class));
        return cartItemResponse;
    }

}
