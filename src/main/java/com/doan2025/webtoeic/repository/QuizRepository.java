package com.doan2025.webtoeic.repository;

import com.doan2025.webtoeic.domain.Quiz;
import com.doan2025.webtoeic.dto.SearchQuizDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    @Query("""
                    SELECT quiz from Quiz quiz
                    WHERE (COALESCE(:#{#dto.searchString}, null) IS NULL OR (
                            LOWER(CAST(quiz.title as string)) like LOWER(CONCAT('%', :#{#dto.searchString}, '%'))
                            OR LOWER(CAST(quiz.description as string)) like LOWER(CONCAT('%', :#{#dto.searchString}, '%'))
                    ) )
            """)
    List<Quiz> filter(SearchQuizDto dto);
}
