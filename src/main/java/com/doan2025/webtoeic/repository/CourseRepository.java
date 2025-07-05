package com.doan2025.webtoeic.repository;

import com.doan2025.webtoeic.domain.Course;
import com.doan2025.webtoeic.dto.response.CourseResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> getDetailCourseById(Long id);
}
