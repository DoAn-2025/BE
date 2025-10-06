package com.doan2025.webtoeic.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Table(name = "question_bank")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionBank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "title", columnDefinition = "LONGTEXT")
    private String title;

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
