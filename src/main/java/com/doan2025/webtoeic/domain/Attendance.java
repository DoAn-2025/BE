package com.doan2025.webtoeic.domain;

import com.doan2025.webtoeic.constants.enums.EAttendanceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Entity đại diện cho bảng điểm danh (attendance) trong hệ thống.
 * Được sử dụng để lưu trữ thông tin điểm danh của học sinh trong một ca học (schedule),
 * bao gồm thời gian check-in, trạng thái điểm danh và liên kết với học sinh, lịch học.
 */

@Entity
@Data
@Table(name = "attendance")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Attendance {
    /**
     * ID duy nhất của bản ghi điểm danh.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Thời gian học sinh check-in vào ca học.
     */
    @Column(name = "check_in")
    private Date checkIn;

    /**
     * Trạng thái điểm danh của học sinh (ví dụ: PRESENT, ABSENT).
     */
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EAttendanceStatus status;

    /**
     * Học sinh được điểm danh, liên kết với bảng User.
     */
    @ManyToOne
    @JoinColumn(name = "student")
    private User student;

    /**
     * Lịch học mà bản ghi điểm danh thuộc về, liên kết với bảng ClassSchedule.
     */
    @ManyToOne
    @JoinColumn(name = "schedule")
    private ClassSchedule schedule;
}
