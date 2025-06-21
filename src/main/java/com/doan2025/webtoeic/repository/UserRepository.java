package com.doan2025.webtoeic.repository;

import com.doan2025.webtoeic.domain.User;
import com.doan2025.webtoeic.dto.request.UserRequest;
import com.doan2025.webtoeic.dto.response.UserResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @Query(value = """
            SELECT new com.doan2025.webtoeic.dto.response.UserResponse(
                            u.firstName, u.lastName, u.phone, u.address, 
                            u.dob, u.gender, u.avatarUrl, u.isActive, u.isDelete, 
                            s.education, s.major)
            FROM User u 
            LEFT JOIN u.student s 
            LEFT JOIN u.consultant c 
            LEFT JOIN u.teacher t
            LEFT JOIN u.manager m 
            WHERE u.email = :email 
                AND u.isActive = true AND u.isDelete = false

""")
    Optional<UserResponse> findUser(String email);

    @Query(value = """
            SELECT new com.doan2025.webtoeic.dto.response.UserResponse(
                            u.firstName, u.lastName, u.phone, u.address, 
                            u.dob, u.gender, u.avatarUrl, u.isActive, u.isDelete, 
                            s.education, s.major)
            FROM User u 
            LEFT JOIN u.student s 
            LEFT JOIN u.consultant c 
            LEFT JOIN u.teacher t
            LEFT JOIN u.manager m 
            WHERE u.id = :#{#request.id} 
                AND u.isActive = true AND u.isDelete = false

""")
    Optional<UserResponse>  findUserById(UserRequest request);

    Optional<User> findById(Long id);
}
