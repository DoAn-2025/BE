package com.doan2025.webtoeic.domain;

import com.doan2025.webtoeic.constants.enums.EAttendanceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Table(name = "attendance")
@AllArgsConstructor
@NoArgsConstructor
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "check_in")
    private Date checkIn;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EAttendanceStatus status;

    @ManyToOne
    @JoinColumn(name = "student")
    private User student;

    @ManyToOne
    @JoinColumn(name = "schedule")
    private ClassSchedule schedule;
}
