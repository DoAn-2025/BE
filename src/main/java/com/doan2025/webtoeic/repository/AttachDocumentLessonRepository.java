package com.doan2025.webtoeic.repository;

import com.doan2025.webtoeic.domain.AttachDocumentLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AttachDocumentLessonRepository extends JpaRepository<AttachDocumentLesson, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM AttachDocumentLesson a WHERE a.lesson.id = :lessonId")
    void deleteAttachDocumentLessonsByLessonId(Long id);

    List<AttachDocumentLesson> findAllByLessonId(Long lessonId);
}
