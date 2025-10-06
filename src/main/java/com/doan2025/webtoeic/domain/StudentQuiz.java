package com.doan2025.webtoeic.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Table(name = "student_quiz")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentQuiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "quiz")
    private Quiz quiz;

    @Column(name = "score")
    private Long score;

    @Column(name = "start_at")
    private Date startAt;

    @Column(name = "end_at")
    private Date endAt;
}
