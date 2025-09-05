package com.doan2025.webtoeic.repository;

import com.doan2025.webtoeic.domain.ClassMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassMemberRepository extends JpaRepository<ClassMember, Long> {
    @Query("""
                    SELECT clazz.id
                    FROM ClassMember
                    WHERE member.email = :email
            """)
    List<Long> findClassOfMember(String email);
}
