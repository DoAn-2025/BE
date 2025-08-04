package com.doan2025.webtoeic.domain;

import com.doan2025.webtoeic.constants.enums.ECategoryCourse;
import com.doan2025.webtoeic.utils.TimeUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "course")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Long price;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_delete")
    private Boolean isDelete;

    @Column(name = "category_course")
    @Enumerated(EnumType.STRING)
    private ECategoryCourse categoryCourse;

    @ManyToOne
    @JoinColumn(name = "author")
    private User author;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<Enrollment> enrollments;

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER)
    private List<Lesson> lessons;

    public Course(String title, String description, Long price, String thumbnailUrl, ECategoryCourse categoryCourse, User author, User createdBy) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.thumbnailUrl = thumbnailUrl;
        this.categoryCourse = categoryCourse;
        this.author = author;
        this.createdBy = createdBy;
    }

    @PrePersist
    protected void onCreate() {
        this.isActive = false;
        this.isDelete = false;
        this.createdAt = TimeUtil.getCurrentTimestamp();
        this.updatedAt = null;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = TimeUtil.getCurrentTimestamp();
    }

    @Override
    public String toString() {
        return "Course{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", isActive=" + isActive +
                ", isDelete=" + isDelete +
                ", categoryCourse=" + categoryCourse +
                '}';
    }
}
