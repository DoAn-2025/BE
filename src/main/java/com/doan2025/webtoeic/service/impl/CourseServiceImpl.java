package com.doan2025.webtoeic.service.impl;

import com.doan2025.webtoeic.constants.enums.ECategoryCourse;
import com.doan2025.webtoeic.constants.enums.ERole;
import com.doan2025.webtoeic.constants.enums.ResponseCode;
import com.doan2025.webtoeic.constants.enums.ResponseObject;
import com.doan2025.webtoeic.domain.Course;
import com.doan2025.webtoeic.domain.Enrollment;
import com.doan2025.webtoeic.domain.User;
import com.doan2025.webtoeic.dto.SearchBaseDto;
import com.doan2025.webtoeic.dto.request.CourseRequest;
import com.doan2025.webtoeic.dto.response.CourseResponse;
import com.doan2025.webtoeic.dto.response.UserResponse;
import com.doan2025.webtoeic.exception.WebToeicException;
import com.doan2025.webtoeic.repository.CourseRepository;
import com.doan2025.webtoeic.repository.UserRepository;
import com.doan2025.webtoeic.service.CourseService;
import com.doan2025.webtoeic.utils.FieldUpdateUtil;
import com.doan2025.webtoeic.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = { WebToeicException.class, Exception.class })
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;

    @Override
    public CourseResponse getCourseDetail(HttpServletRequest httpServletRequest, Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.COURSE));
        return convertCourseToDto(httpServletRequest, course);
    }

    @Override
    public List<CourseResponse> getCourses(SearchBaseDto dto, Pageable pageable) {
        if(dto.getCategories() == null || dto.getCategories().isEmpty()) {
            dto.setCategories(null);
        }
        return courseRepository.findCourses(dto, pageable);
    }

    @Override
    public List<CourseResponse> getAllCourses(HttpServletRequest request, SearchBaseDto dto, Pageable pageable) {
        if(dto.getCategories() == null || dto.getCategories().isEmpty()) {
            dto.setCategories(null);
        }
        return courseRepository.findAllCourses(dto, pageable);
    }

    @Override
    public List<CourseResponse> getOwnCourses(HttpServletRequest request, SearchBaseDto dto,Pageable pageable) {
        String email = jwtUtil.getEmailFromToken(request);
        dto.setEmail(email);
        if(dto.getCategories() == null || dto.getCategories().isEmpty()) {
            dto.setCategories(null);
        }
        return courseRepository.findOwnCourses(dto, email, pageable);
    }

    @Override
    public CourseResponse createCourse(HttpServletRequest httpServletRequest, CourseRequest request) {
        if(request.getCategoryId() == null){
            throw new WebToeicException(ResponseCode.IS_NULL, ResponseObject.CATEGORY);
        }
        if(request.getAuthorId() == null){
            throw new WebToeicException(ResponseCode.IS_NULL, ResponseObject.USER);
        }
        if (request.getTitle() == null || request.getTitle().isEmpty()) {
            throw new WebToeicException(ResponseCode.IS_NULL, ResponseObject.TITLE);
        }
        if(request.getPrice() == null || request.getPrice().compareTo(BigDecimal.ZERO) <= 0){
            throw new WebToeicException(ResponseCode.NOT_AVAILABLE, ResponseObject.PRICE);
        }
        User author = userRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.USER));
        ECategoryCourse categoryCourse = ECategoryCourse.fromValue(request.getCategoryId());
        User createdBy = userRepository.findByEmail(jwtUtil.getEmailFromToken(httpServletRequest))
                .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.USER));
        Course course = Course.builder()
                .author(author)
                .categoryCourse(categoryCourse)
                .price(request.getPrice())
                .description(request.getDescription())
                .title(request.getTitle())
                .createdBy(createdBy)
                .build();
        Course savedCourse = courseRepository.save(course);
        return convertCourseToDto(httpServletRequest, savedCourse);
    }

    @Override
    public CourseResponse updateCourse(HttpServletRequest httpServletRequest, CourseRequest request) {
        User updatedBy = userRepository.findByEmail(jwtUtil.getEmailFromToken(httpServletRequest))
                .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.USER));
        Course course = courseRepository.findById(request.getId())
                .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.COURSE));
        List.of(
                new FieldUpdateUtil<>(course::getTitle, course::setTitle, request.getTitle()),
                new FieldUpdateUtil<>(course::getDescription, course::setDescription, request.getDescription()),
                new FieldUpdateUtil<>(course::getPrice, course::setPrice, request.getPrice()),
                new FieldUpdateUtil<>(course::getAuthor, course::setAuthor,
                        userRepository.findById(request.getAuthorId())
                                .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.USER))),
                new FieldUpdateUtil<>(course::getThumbnailUrl, course::setThumbnailUrl, request.getThumbnailUrl()),
                new FieldUpdateUtil<>(course::getCategoryCourse, course::setCategoryCourse, ECategoryCourse.fromValue(request.getCategoryId()))
        ).forEach(FieldUpdateUtil::updateIfNeeded);
        course.setUpdatedBy(updatedBy);
        return modelMapper.map(courseRepository.save(course), CourseResponse.class);
    }

    @Override
    public CourseResponse disableOrDeleteCourse(HttpServletRequest httpServletRequest, CourseRequest request) {
        User user = userRepository.findByEmail(jwtUtil.getEmailFromToken(httpServletRequest))
                .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.USER));
        Course course = courseRepository.findById(request.getId())
                .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.COURSE));
        if(user.getRole().equals(ERole.MANAGER)){
            // function: disable course
            if(request.getIsActive() != null && !request.getIsActive().equals(course.getIsActive())) {
                course.setIsActive(request.getIsActive());
            }
            // function: delete course
            if (request.getIsDelete() != null && !course.getIsDelete().equals(request.getIsDelete())) {
                course.setIsDelete(request.getIsDelete());
            }
            return modelMapper.map(courseRepository.save(course), CourseResponse.class);
        }
        throw new WebToeicException(ResponseCode.NOT_PERMISSION, ResponseObject.USER);
    }

    private CourseResponse convertCourseToDto(HttpServletRequest request, Course course){
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
        courseResponse.setAuthor(course.getAuthor() != null ? modelMapper.map(course.getAuthor(), UserResponse.class)  : null);
        courseResponse.setCreatedBy(course.getCreatedBy() != null ? modelMapper.map(course.getCreatedBy(), UserResponse.class) : null);
        courseResponse.setUpdatedBy(course.getUpdatedBy() != null ? modelMapper.map(course.getUpdatedBy(), UserResponse.class) : null);
        String email = "" ;
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            email = jwtUtil.getEmailFromToken(request);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.USER));
            if(user.getRole().equals(ERole.MANAGER) || user.getRole().equals(ERole.CONSULTANT)){
                courseResponse.setIsBought(true);
            }
            if(course.getEnrollments() != null && !course.getEnrollments().isEmpty()){
                for(Enrollment enrollment : course.getEnrollments()){
                    if (enrollment.getUser().equals(user)){
                        courseResponse.setIsBought(true);
                    }
                }
            }
        }
        return courseResponse;
    }
}
