package com.doan2025.webtoeic.repository;

import com.doan2025.webtoeic.domain.Class;
import com.doan2025.webtoeic.domain.ClassMember;
import com.doan2025.webtoeic.domain.User;
import com.doan2025.webtoeic.dto.request.ClassRequest;
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

    @Query("""
                    SELECT cm
                    FROM ClassMember cm
                    WHERE cm.clazz.id = :#{#classRequest.id} AND cm.member.id IN (:#{#classRequest.memberIds})
            """)
    List<ClassMember> findByClassAndUser(ClassRequest classRequest);

    boolean existsByClazzAndMember(Class clazz, User member);

    @Query("""
                    SELECT cm
                    FROM ClassMember cm
                    WHERE cm.clazz.id = :clazzId AND cm.member.id = :userId
            """)
    ClassMember findByClassAndMember(Long userId, Long clazzId);
}
