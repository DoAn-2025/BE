package com.doan2025.webtoeic.repository;

import com.doan2025.webtoeic.domain.ClassSchedule;
import com.doan2025.webtoeic.dto.SearchScheduleSto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ClassScheduleRepository extends JpaRepository<ClassSchedule, Long> {
    @Query("""
            SELECT sc FROM ClassSchedule sc
            WHERE
                ( (COALESCE(:#{#dto.fromDate}, NULL) IS NULL AND COALESCE(:#{#dto.toDate}, NULL) IS NULL)
                           OR (COALESCE(:#{#dto.fromDate}, NULL) IS NULL AND COALESCE(:#{#dto.toDate}, NULL) IS NOT NULL AND sc.endAt <= :#{#dto.toDate})
                           OR (COALESCE(:#{#dto.fromDate}, NULL) IS NOT NULL AND COALESCE(:#{#dto.toDate}, NULL) IS NULL AND sc.startAt >= :#{#dto.fromDate})
                           OR sc.startAt BETWEEN :#{#dto.fromDate} AND :#{#dto.toDate} OR sc.endAt BETWEEN :#{#dto.fromDate} AND :#{#dto.toDate}
                           )
             AND ( COALESCE(:#{#dto.classId}, null ) is null OR sc.clazz.id IN (:#{#dto.classId}) )
             AND ( COALESCE(:#{#dto.teacherId}, null ) is null OR sc.clazz.teacher.id IN (:#{#dto.teacherId}) )
             AND ( COALESCE(:#{#dto.status}, null ) is null OR sc.status IN (:#{#dto.status})  )
             AND (COALESCE(:classIds, null ) is null OR sc.clazz.id IN (:classIds) )
            """)
    Page<ClassSchedule> filterSchedule(SearchScheduleSto dto, List<Long> classIds, Pageable pageable);

//    @Query("""
//                SELECT sc.id
//                FROM ClassSchedule sc
//                WHERE sc.clazz.id = :classId
//                AND (
//                     CURRENT_TIMESTAMP >= TIMESTAMPADD('HOUR', -2, sc.startAt )
//                  OR CURRENT_TIMESTAMP <= TIMESTAMPADD('HOUR', 2, sc.endAt )
//                )
//            """)
//    Long getAvailableSchedule(Long classId);

    @Query(value = """
                SELECT sc.id
                FROM class_schedule sc
                WHERE sc.class = :classId
                AND DATE(CURRENT_TIMESTAMP) = DATE(sc.start_at)
                AND (
                     CURRENT_TIMESTAMP >= TIMESTAMPADD(HOUR, -2, sc.start_at)
                  OR CURRENT_TIMESTAMP <= TIMESTAMPADD(HOUR, 2, sc.end_at)
                )
            """, nativeQuery = true)
    Long getAvailableSchedule(Long classId);


    @Query("""
                    SELECT COUNT(sc) > 0 FROM ClassSchedule sc
                    WHERE sc.room.id = :roomId
                    AND sc.startAt = :startAt AND sc.endAt = :endAt
            """)
    boolean existsScheduleByRoomIdAndStartAtAndEndAt(Date startAt, Date endAt, Long roomId);
}
