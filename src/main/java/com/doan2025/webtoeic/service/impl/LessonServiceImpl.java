package com.doan2025.webtoeic.service.impl;

import com.doan2025.webtoeic.constants.enums.ERole;
import com.doan2025.webtoeic.constants.enums.ResponseCode;
import com.doan2025.webtoeic.constants.enums.ResponseObject;
import com.doan2025.webtoeic.domain.Course;
import com.doan2025.webtoeic.domain.Enrollment;
import com.doan2025.webtoeic.domain.Lesson;
import com.doan2025.webtoeic.domain.User;
import com.doan2025.webtoeic.dto.SearchBaseDto;
import com.doan2025.webtoeic.dto.request.LessonRequest;
import com.doan2025.webtoeic.dto.response.LessonResponse;
import com.doan2025.webtoeic.dto.response.UserResponse;
import com.doan2025.webtoeic.exception.WebToeicException;
import com.doan2025.webtoeic.repository.CourseRepository;
import com.doan2025.webtoeic.repository.LessonRepository;
import com.doan2025.webtoeic.repository.UserRepository;
import com.doan2025.webtoeic.service.CloudService;
import com.doan2025.webtoeic.service.LessonService;
import com.doan2025.webtoeic.utils.FieldUpdateUtil;
import com.doan2025.webtoeic.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final CloudService cloudService;


    @Override
    public LessonResponse getDetail(HttpServletRequest httpServletRequest, Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.LESSON));
        return convertLessonToDto(httpServletRequest, lesson);
    }

    @Override
    public Page<LessonResponse> getLessons(HttpServletRequest request, SearchBaseDto dto, Pageable pageable) {
        String email = "";
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            email = jwtUtil.getEmailFromToken(request);
        }
        if (dto.getCategories() == null || dto.getCategories().isEmpty()) {
            dto.setCategories(null);
        }
        return lessonRepository.findLessons(dto, email, pageable);
    }

    @Override
    public Page<LessonResponse> getOwnLessons(HttpServletRequest request, SearchBaseDto dto, Pageable pageable) {
        if (dto.getCategories() == null || dto.getCategories().isEmpty()) {
            dto.setCategories(null);
        }
        String email = jwtUtil.getEmailFromToken(request);
        return lessonRepository.findOwnLessons(dto, email, pageable);
    }

    @Override
    public Page<LessonResponse> getAllLessons(HttpServletRequest request, SearchBaseDto dto, Pageable pageable) {
        if (dto.getCategories() == null || dto.getCategories().isEmpty()) {
            dto.setCategories(null);
        }
        return lessonRepository.findAllLessons(dto, pageable);
    }

    @Override
    public LessonResponse disableOrDelete(HttpServletRequest request, LessonRequest lessonRequest) {
        String email = jwtUtil.getEmailFromToken(request);

        User updatedBy = userRepository.findByEmail(email)
                .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.USER));

        Lesson lesson = lessonRepository.findById(lessonRequest.getId())
                .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.LESSON));

        if (updatedBy.getRole().equals(ERole.MANAGER)) {
            // function: disable
            if (lessonRequest.getIsActive() != null && !lesson.getIsActive().equals(lessonRequest.getIsActive())) {
                lesson.setIsActive(lessonRequest.getIsActive());
            }
            // function: delete
            if (lessonRequest.getIsDelete() != null && !lesson.getIsDelete().equals(lessonRequest.getIsDelete())) {
                lesson.setIsDelete(lessonRequest.getIsDelete());
            }
            return modelMapper.map(lessonRepository.save(lesson), LessonResponse.class);
        }
        throw new WebToeicException(ResponseCode.NOT_PERMISSION, ResponseObject.LESSON);
    }

    @Override
    public LessonResponse updateLesson(HttpServletRequest request, LessonRequest lessonRequest) {
        String email = jwtUtil.getEmailFromToken(request);

        User updatedBy = userRepository.findByEmail(email)
                .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.USER));

        Lesson lesson = lessonRepository.findById(lessonRequest.getId())
                .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.LESSON));
        if (updatedBy.getRole().equals(ERole.CONSULTANT) && lesson.getCreatedBy().getEmail().equals(email)) {
            List.of(
                    new FieldUpdateUtil<>(lesson::getTitle, lesson::setTitle, lessonRequest.getTitle()),
                    new FieldUpdateUtil<>(lesson::getContent, lesson::setContent, lessonRequest.getContent()),
                    new FieldUpdateUtil<>(lesson::getVideoUrl, lesson::setVideoUrl, lessonRequest.getVideoUrl()),
                    new FieldUpdateUtil<>(lesson::getIsPreviewAble, lesson::setIsPreviewAble, lessonRequest.getIsPreviewAble()),
                    new FieldUpdateUtil<>(lesson::getOrderIndex, lesson::setOrderIndex, lessonRequest.getOrderIndex())
            ).forEach(FieldUpdateUtil::updateIfNeeded);
            return modelMapper.map(lessonRepository.save(lesson), LessonResponse.class);
        }
        throw new WebToeicException(ResponseCode.NOT_PERMISSION, ResponseObject.USER);

    }


    @Override
    public LessonResponse createLesson(HttpServletRequest request, LessonRequest lesson) {
        User createdBy = userRepository.findByEmail(jwtUtil.getEmailFromToken(request))
                .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.USER));
        Course course = courseRepository.findById(lesson.getCourseId())
                .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.COURSE));
        if (lesson.getContent() == null || lesson.getContent().trim().isEmpty()) {
            throw new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.CONTENT);
        }
        if (lesson.getTitle() == null || lesson.getTitle().trim().isEmpty()) {
            throw new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.TITLE);
        }
        if (lesson.getVideoUrl() == null || lesson.getVideoUrl().trim().isEmpty()) {
            throw new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.URL);
        }

        Lesson saveLesson = modelMapper.map(lesson, Lesson.class);
        saveLesson.setCourse(course);
        saveLesson.setOrderIndex(course.getLessons().size() + 1);
        saveLesson.setCreatedBy(createdBy);
        Double duration = cloudService.getVideoDuration(lesson.getVideoUrl());
        saveLesson.setDuration(duration);

        return convertLessonToDto(request, lessonRepository.save(saveLesson));
    }

    private LessonResponse convertLessonToDto(HttpServletRequest httpServletRequest, Lesson lesson) {
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
}
