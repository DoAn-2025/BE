package com.doan2025.webtoeic.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "student")
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "education")
    private String education;

    @Column(name = "major")
    private String major;

    @JsonIgnoreProperties(allowSetters = true, allowGetters = true, value = {"user"})
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
    private User user;

    @Override
    public String toString() {
        return "Student{" +
                "major='" + major + '\'' +
                ", education='" + education + '\'' +
                ", id=" + id +
                '}';
    }
}
