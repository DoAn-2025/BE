package com.doan2025.webtoeic.repository;

import com.doan2025.webtoeic.domain.Course;
import com.doan2025.webtoeic.dto.SearchBaseDto;
import com.doan2025.webtoeic.dto.response.CourseResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("""
        SELECT new com.doan2025.webtoeic.dto.response.CourseResponse(
        c.id, c.title, c.description, c.price, c.thumbnailUrl, c.categoryCourse,
        c.updatedAt, c.createdAt, c.isDelete, c.isActive, CONCAT(a.firstName, ' ', a.lastName ) ,
        CONCAT(cb.firstName, ' ', cb.lastName ) , CONCAT(ub.firstName, ' ', ub.lastName ) )
        FROM Course c
        LEFT JOIN c.author a
        left JOIN c.lessons l
        left JOIN c.createdBy cb
        left JOIN c.updatedBy ub
        WHERE c.isDelete = false AND c.isActive = TRUE
            AND ( COALESCE(:#{#dto.title}, null) is null OR  c.title LIKE CONCAT('%', :#{#dto.title}, '%') )
            AND ( (COALESCE(:#{#dto.fromDate}, null ) is null AND COALESCE(:#{#dto.toDate}, null ) is null )
                    OR c.createdAt between :#{#dto.fromDate} and :#{#dto.toDate})
            AND (COALESCE(:#{#dto.categories}, null) is null OR c.categoryCourse IN (:#{#dto.categories}) ) 
""")
    List<CourseResponse> findCourses(SearchBaseDto dto, Pageable pageable);

    @Query("""
        SELECT new com.doan2025.webtoeic.dto.response.CourseResponse(
        c.id, c.title, c.description, c.price, c.thumbnailUrl, c.categoryCourse,
        c.updatedAt, c.createdAt, c.isDelete, c.isActive, CONCAT(a.firstName, ' ', a.lastName ) ,
        CONCAT(cb.firstName, ' ', cb.lastName ) , CONCAT(ub.firstName, ' ', ub.lastName ) )
        FROM Course c
        LEFT JOIN c.author a
        left JOIN c.lessons l
        left JOIN c.createdBy cb
        left JOIN c.updatedBy ub
        WHERE ( COALESCE(:#{#dto.title}, null) is null OR  c.title LIKE CONCAT('%', :#{#dto.title}, '%') )
            AND ( (COALESCE(:#{#dto.fromDate}, null ) is null AND COALESCE(:#{#dto.toDate}, null ) is null )
                    OR c.createdAt between :#{#dto.fromDate} and :#{#dto.toDate})
            AND (COALESCE(:#{#dto.categories}, null) is null OR c.categoryCourse IN (:#{#dto.categories}) )
        
""")
    List<CourseResponse> findAllCourses(SearchBaseDto dto, Pageable pageable);

    @Query("""
        SELECT new com.doan2025.webtoeic.dto.response.CourseResponse(
        c.id, c.title, c.description, c.price, c.thumbnailUrl, c.categoryCourse,
        c.updatedAt, c.createdAt, c.isDelete, c.isActive, CONCAT(a.firstName, ' ', a.lastName ) ,
        CONCAT(cb.firstName, ' ', cb.lastName ) , CONCAT(ub.firstName, ' ', ub.lastName ) )
        FROM Course c
        LEFT JOIN c.author a
        left JOIN c.lessons l
        left JOIN c.createdBy cb
        left JOIN c.updatedBy ub
        WHERE c.isDelete = false AND c.isActive = TRUE
            AND c.createdBy.email = :email
            AND ( COALESCE(:#{#dto.title}, null) is null OR  c.title LIKE CONCAT('%', :#{#dto.title}, '%') )
            AND ( (COALESCE(:#{#dto.fromDate}, null ) is null AND COALESCE(:#{#dto.toDate}, null ) is null )
                    OR c.createdAt between :#{#dto.fromDate} and :#{#dto.toDate})
            AND (COALESCE(:#{#dto.categories}, null) is null OR c.categoryCourse IN (:#{#dto.categories}) )
""")
    List<CourseResponse> findOwnCourses(SearchBaseDto dto, String email, Pageable pageable);

}
