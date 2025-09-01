package com.doan2025.webtoeic.domain;

import com.doan2025.webtoeic.constants.enums.EJoinStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Table(name = "class_member")
@AllArgsConstructor
@NoArgsConstructor
public class ClassMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EJoinStatus status;

    @ManyToOne
    @JoinColumn(name = "class")
    private Class clazz;

    @ManyToOne
    @JoinColumn(name = "student")
    private User student;

    @Column
    private Date joinDate;
}
