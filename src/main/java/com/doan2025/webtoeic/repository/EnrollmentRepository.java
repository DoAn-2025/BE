package com.doan2025.webtoeic.repository;

import com.doan2025.webtoeic.domain.Course;
import com.doan2025.webtoeic.domain.Enrollment;
import com.doan2025.webtoeic.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    boolean existsByUserAndCourse(User user, Course course);
}
