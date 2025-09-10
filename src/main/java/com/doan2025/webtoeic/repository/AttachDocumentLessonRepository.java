package com.doan2025.webtoeic.repository;

import com.doan2025.webtoeic.domain.AttachDocumentLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachDocumentLessonRepository extends JpaRepository<AttachDocumentLesson, Long> {

    void deleteAllByLessonId(Long id);
}
