package com.doan2025.webtoeic.service.impl;

import com.doan2025.webtoeic.constants.enums.ResponseCode;
import com.doan2025.webtoeic.constants.enums.ResponseObject;
import com.doan2025.webtoeic.domain.Course;
import com.doan2025.webtoeic.domain.Lesson;
import com.doan2025.webtoeic.domain.User;
import com.doan2025.webtoeic.dto.request.LessonRequest;
import com.doan2025.webtoeic.dto.response.LessonResponse;
import com.doan2025.webtoeic.exception.WebToeicException;
import com.doan2025.webtoeic.repository.CourseRepository;
import com.doan2025.webtoeic.repository.LessonRepository;
import com.doan2025.webtoeic.repository.UserRepository;
import com.doan2025.webtoeic.service.CloudService;
import com.doan2025.webtoeic.service.LessonService;
import com.doan2025.webtoeic.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
    public LessonResponse createLesson(HttpServletRequest request, LessonRequest lesson) {
        User createdBy = userRepository.findByEmail(jwtUtil.getEmailFromToken(request))
                .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.USER));
        Course course = courseRepository.findById(lesson.getCourseId())
                .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.COURSE));
        if(lesson.getContent() == null || lesson.getContent().trim().isEmpty()){
            throw new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.CONTENT);
        }
        if (lesson.getTitle() == null || lesson.getTitle().trim().isEmpty()){
            throw new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.TITLE);
        }
        if(lesson.getVideoUrl() == null || lesson.getVideoUrl().trim().isEmpty()){
            throw new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.URL);
        }

        Lesson saveLesson = modelMapper.map(lesson, Lesson.class);
        saveLesson.setCourse(course);
        saveLesson.setCreatedBy(createdBy);
        saveLesson.setDuration(cloudService.getVideoDuration(lesson.getVideoUrl()));

        return modelMapper.map(lessonRepository.save(saveLesson), LessonResponse.class);
    }
}
