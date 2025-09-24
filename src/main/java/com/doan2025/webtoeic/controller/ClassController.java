package com.doan2025.webtoeic.controller;

import com.doan2025.webtoeic.constants.enums.ResponseCode;
import com.doan2025.webtoeic.constants.enums.ResponseObject;
import com.doan2025.webtoeic.dto.SearchClassDto;
import com.doan2025.webtoeic.dto.SearchMemberInClassDto;
import com.doan2025.webtoeic.dto.SearchScheduleSto;
import com.doan2025.webtoeic.dto.request.AttendanceRequest;
import com.doan2025.webtoeic.dto.request.ClassRequest;
import com.doan2025.webtoeic.dto.request.ClassScheduleRequest;
import com.doan2025.webtoeic.dto.response.ApiResponse;
import com.doan2025.webtoeic.service.AttendanceService;
import com.doan2025.webtoeic.service.ClassMemberService;
import com.doan2025.webtoeic.service.ClassScheduleService;
import com.doan2025.webtoeic.service.ClassService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/class")
public class ClassController {
    private final ClassService classService;
    private final ClassMemberService classMemberService;
    private final ClassScheduleService classScheduleService;
    private final AttendanceService attendanceService;

    @GetMapping("/detail-statistic-attendance")
    @PreAuthorize("hasRole('TEACHER') or hasRole('MANAGER') or  hasRole('CONSULTANT')")
    public ApiResponse<?> detailStatisticAttendance(HttpServletRequest httpServletRequest,
                                                    @RequestParam("scheduleId") Long id,
                                                    Pageable pageable) {
        return ApiResponse.of(ResponseCode.GET_SUCCESS, ResponseObject.ATTENDANCE, classScheduleService.detailStatisticAttendance(httpServletRequest, id, pageable));
    }

    @GetMapping("/overview-statistic-attendance")
    @PreAuthorize("hasRole('TEACHER') or hasRole('MANAGER') or  hasRole('CONSULTANT')")
    public ApiResponse<?> overviewStatisticAttendance(HttpServletRequest httpServletRequest,
                                                      @RequestParam("classId") Long id,
                                                      Pageable pageable) {
        return ApiResponse.of(ResponseCode.GET_SUCCESS, ResponseObject.ATTENDANCE, classScheduleService.overviewStatisticAttendance(httpServletRequest, id, pageable));
    }

    @PostMapping("/attendance")
    @PreAuthorize("hasRole('TEACHER')")
    public ApiResponse<Void> attendance(HttpServletRequest httpServletRequest, @RequestBody List<AttendanceRequest> requests) {
        attendanceService.attendance(httpServletRequest, requests);
        return ApiResponse.of(ResponseCode.CREATE_SUCCESS, ResponseObject.ATTENDANCE, null);
    }

    @PostMapping("/update-attendance")
    @PreAuthorize("hasRole('TEACHER')")
    public ApiResponse<Void> updateAttendance(HttpServletRequest httpServletRequest, @RequestBody List<AttendanceRequest> requests) {
        attendanceService.updateAttendance(httpServletRequest, requests);
        return ApiResponse.of(ResponseCode.UPDATE_SUCCESS, ResponseObject.ATTENDANCE, null);
    }

    @PostMapping("/get-members-in-class")
    public ApiResponse<?> getStudentInClass(HttpServletRequest httpServletRequest,
                                            @RequestBody SearchMemberInClassDto request,
                                            Pageable pageable) {
        return ApiResponse.of(ResponseCode.GET_SUCCESS, ResponseObject.USER, classMemberService.getMemberInClass(httpServletRequest, request, pageable));
    }

    @GetMapping("/get-schedule-detail-in-class")
    public ApiResponse<?> getScheduleDetailInClass(HttpServletRequest request, @RequestParam("scheduleId") Long scheduleId) {
        return ApiResponse.of(ResponseCode.GET_SUCCESS, ResponseObject.SCHEDULE, classScheduleService.getScheduleDetail(request, scheduleId));
    }

    @PostMapping("/get-schedules-in-class")
    public ApiResponse<?> getScheduleInClass(HttpServletRequest request, @RequestBody SearchScheduleSto dto, Pageable pageable) {
        return ApiResponse.of(ResponseCode.GET_SUCCESS, ResponseObject.SCHEDULE, classScheduleService.getClassSchedule(request, dto, pageable));
    }

    @PostMapping("/cancelled-schedule-in-class")
    @PreAuthorize("hasRole('CONSULTANT') OR hasRole('TEACHER')")
    public ApiResponse<?> cancelledScheduleInClass(HttpServletRequest request, @RequestParam("ids") List<Long> ids) {
        classScheduleService.cancelledScheduleInClass(request, ids);
        return ApiResponse.of(ResponseCode.CANCELLED_SUCCESS, ResponseObject.SCHEDULE, null);
    }

    @PostMapping("/update-schedule-in-class")
    @PreAuthorize("hasRole('CONSULTANT') OR hasRole('TEACHER')")
    public ApiResponse<?> updateScheduleInClass(HttpServletRequest request, @RequestBody ClassScheduleRequest dto) {
        return ApiResponse.of(ResponseCode.UPDATE_SUCCESS, ResponseObject.SCHEDULE, classScheduleService.updateScheduleInClass(request, dto));
    }

    @PostMapping("/create-schedule-in-class")
    @PreAuthorize("hasRole('CONSULTANT') OR hasRole('TEACHER')")
    public ApiResponse<?> createScheduleInClass(HttpServletRequest request, @RequestBody List<ClassScheduleRequest> dtos) {
        return ApiResponse.of(ResponseCode.CREATE_SUCCESS, ResponseObject.SCHEDULE, classScheduleService.createScheduleInClass(request, dtos));
    }

    @PostMapping("/remove-user-from-class")
    @PreAuthorize("hasRole('CONSULTANT') OR hasRole('TEACHER')")
    public ApiResponse<Void> removeUserFromClass(HttpServletRequest request, @RequestBody ClassRequest classRequest) {
        classMemberService.removeUserFromClass(request, classRequest);
        return ApiResponse.of(ResponseCode.DELETE_SUCCESS, ResponseObject.USER, null);
    }

    @PostMapping("/add-user-to-class")
    @PreAuthorize("hasRole('CONSULTANT') OR hasRole('TEACHER')")
    public ApiResponse<Void> addUserToClass(HttpServletRequest request, @RequestBody ClassRequest classRequest) {
        classMemberService.addUserToClass(request, classRequest);
        return ApiResponse.of(ResponseCode.UPDATE_SUCCESS, ResponseObject.CLASS, null);
    }

    @GetMapping("/detail")
    public ApiResponse<?> getDetail(HttpServletRequest request, @RequestParam("id") Long id) {
        return ApiResponse.of(ResponseCode.GET_SUCCESS, ResponseObject.CLASS, classService.get(request, id));
    }

    @PostMapping("/filter")
    public ApiResponse<?> filterClass(@RequestBody SearchClassDto dto, HttpServletRequest httpServletRequest) {
        return ApiResponse.of(ResponseCode.GET_SUCCESS, ResponseObject.CLASS, classService.getClasses(httpServletRequest, dto));
    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('CONSULTANT') OR hasRole('TEACHER')")
    public ApiResponse<Void> deleteClass(HttpServletRequest request, @RequestParam("ids") List<Long> ids) {
        classService.deleteClass(ids, request);
        return ApiResponse.of(ResponseCode.DELETE_SUCCESS, ResponseObject.CLASS, null);
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('CONSULTANT') OR hasRole('TEACHER')")
    public ApiResponse<?> updateClass(HttpServletRequest request, @RequestBody ClassRequest classRequest) {
        return ApiResponse.of(ResponseCode.UPDATE_SUCCESS, ResponseObject.CLASS, classService.updateClass(classRequest, request));
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('CONSULTANT') OR hasRole('TEACHER')")
    public ApiResponse<?> createClass(HttpServletRequest request, @RequestBody ClassRequest classRequest) {
        return ApiResponse.of(ResponseCode.CREATE_SUCCESS, ResponseObject.CLASS, classService.createClass(request, classRequest));
    }
}
