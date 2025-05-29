package com.doan2025.webtoeic.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "teacher_shift")
@AllArgsConstructor
@NoArgsConstructor
public class TeacherShift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
