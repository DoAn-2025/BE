package com.doan2025.webtoeic.service.impl;

import com.doan2025.webtoeic.constants.enums.EJoinStatus;
import com.doan2025.webtoeic.constants.enums.ResponseCode;
import com.doan2025.webtoeic.constants.enums.ResponseObject;
import com.doan2025.webtoeic.domain.Class;
import com.doan2025.webtoeic.domain.ClassMember;
import com.doan2025.webtoeic.domain.User;
import com.doan2025.webtoeic.dto.request.ClassRequest;
import com.doan2025.webtoeic.exception.WebToeicException;
import com.doan2025.webtoeic.repository.*;
import com.doan2025.webtoeic.service.ClassMemberService;
import com.doan2025.webtoeic.utils.ConvertUtil;
import com.doan2025.webtoeic.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = {WebToeicException.class, Exception.class})
public class ClassMemberServiceImpl implements ClassMemberService {
    private final ClassScheduleRepository classScheduleRepository;
    private final ClassRepository classRepository;
    private final ClassMemberRepository classMemberRepository;
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final ConvertUtil convertUtil;


    @Override
    public void addUserToClass(HttpServletRequest request, ClassRequest classRequest) {
        Class clazz = classRepository.findById(classRequest.getId())
                .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.CLASS));
        for (Long id : classRequest.getMemberIds()) {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.USER));
            ClassMember classMember = classMemberRepository.findByClassAndMember(classRequest.getId(), id);
            if (Objects.nonNull(classMember)) {
                if (classMember.getStatus().equals(EJoinStatus.DROPPED)) {
                    classMember.setStatus(EJoinStatus.ACTIVE);
                    classMemberRepository.save(classMember);
                }
                continue;
            }
            classMemberRepository.save(
                    ClassMember.builder()
                            .clazz(clazz)
                            .member(user)
                            .roleInClass(user.getRole())
                            .status(EJoinStatus.ACTIVE)
                            .build());
        }
    }

    @Override
    public void removeUserFromClass(HttpServletRequest request, ClassRequest classRequest) {
        List<ClassMember> classMemberList = classMemberRepository.findByClassAndUser(classRequest);
        if (classMemberList.isEmpty()) {
            throw new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.USER);
        }
        for (ClassMember item : classMemberList) {
            item.setStatus(EJoinStatus.DROPPED);
            classMemberRepository.save(item);
        }
    }
}
