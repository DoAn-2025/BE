package com.doan2025.webtoeic.service.impl;

import com.doan2025.webtoeic.constants.enums.EClassStatus;
import com.doan2025.webtoeic.constants.enums.ERole;
import com.doan2025.webtoeic.constants.enums.ResponseCode;
import com.doan2025.webtoeic.constants.enums.ResponseObject;
import com.doan2025.webtoeic.domain.Class;
import com.doan2025.webtoeic.domain.User;
import com.doan2025.webtoeic.dto.SearchClassDto;
import com.doan2025.webtoeic.dto.request.ClassRequest;
import com.doan2025.webtoeic.dto.response.ClassResponse;
import com.doan2025.webtoeic.exception.WebToeicException;
import com.doan2025.webtoeic.repository.*;
import com.doan2025.webtoeic.service.ClassService;
import com.doan2025.webtoeic.utils.ConvertUtil;
import com.doan2025.webtoeic.utils.FieldUpdateUtil;
import com.doan2025.webtoeic.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = {WebToeicException.class, Exception.class})
public class ClassServiceImpl implements ClassService {
    private final ClassScheduleRepository classScheduleRepository;
    private final ClassRepository classRepository;
    private final ClassMemberRepository classMemberRepository;
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final ConvertUtil convertUtil;


    @Override
    public List<ClassResponse> getClasses(HttpServletRequest httpServletRequest, SearchClassDto dto) {
        return List.of();
    }

    @Override
    public void deleteClass(List<Long> ids, HttpServletRequest httpServletRequest) {
        User user = userRepository.findByEmail(jwtUtil.getEmailFromToken(httpServletRequest))
                .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.USER));
        for (Long id : ids) {
            Class clazz = classRepository.findById(id)
                    .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.CLASS));
            if (clazz.getTeacher().getCode().equals(user.getCode()) || ERole.CONSULTANT.getCode().equals(user.getRole().getCode())) {
                classRepository.delete(clazz);
                continue;
            }
            throw new WebToeicException(ResponseCode.NOT_PERMISSION, ResponseObject.USER);
        }
    }

    @Override
    public ClassResponse updateClass(ClassRequest classRequest, HttpServletRequest httpServletRequest) {
        User user = userRepository.findByEmail(jwtUtil.getEmailFromToken(httpServletRequest))
                .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.USER));
        Class clazz = classRepository.findById(classRequest.getId())
                .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.CLASS));
        List.of(
                new FieldUpdateUtil<>(clazz::getName, clazz::setName, classRequest.getName()),
                new FieldUpdateUtil<>(clazz::getDescription, clazz::setDescription, classRequest.getDescription()),
                new FieldUpdateUtil<>(clazz::getSubject, clazz::setSubject, classRequest.getSubject()),
                new FieldUpdateUtil<>(clazz::getTitle, clazz::setTitle, classRequest.getTitle()),
                new FieldUpdateUtil<>(clazz::getTeacher, clazz::setTeacher,
                        userRepository.findById(classRequest.getTeacher())
                                .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.USER))),
                new FieldUpdateUtil<>(clazz::getStatus, clazz::setStatus, EClassStatus.fromValue(classRequest.getStatus()))
        ).forEach(FieldUpdateUtil::updateIfNeeded);
        clazz.setUpdatedBy(user);
        return convertUtil.convertClassToDto(httpServletRequest, classRepository.save(clazz));
    }

    @Override
    public ClassResponse createClass(HttpServletRequest request, ClassRequest classRequest) {
        User user = userRepository.findByEmail(jwtUtil.getEmailFromToken(request))
                .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.USER));
        User teacher = userRepository.findById(classRequest.getTeacher())
                .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.USER));
        Class createClass = Class.builder()
                .description(classRequest.getDescription())
                .title(classRequest.getTitle())
                .name(classRequest.getName())
                .subject(classRequest.getSubject())
                .createdBy(user)
                .status(EClassStatus.PLANNING)
                .teacher(teacher)
                .build();
        return convertUtil.convertClassToDto(request, classRepository.save(createClass));
    }
}
