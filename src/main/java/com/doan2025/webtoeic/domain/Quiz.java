package com.doan2025.webtoeic.domain;

import com.doan2025.webtoeic.constants.enums.EQuizStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Table(name = "quiz")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "title", columnDefinition = "LONGTEXT")
    private String title;

    @Lob
    @Column(name = "description", columnDefinition = "LONGTEXT")
    private String description;

    @Column(name = "total_questions")
    private Long totalQuestions;

    @Column(name = "is_student_created")
    private Boolean isStudentCreated;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EQuizStatus status;

    @Column(name = "create_at")
    private Date createAt;

    @Column(name = "update_at")
    private Date updateAt;

    @ManyToOne
    @JoinColumn(name = "create_by")
    private User createBy;

    @ManyToOne
    @JoinColumn(name = "update_by")
    private User updateBy;
}
