package com.doan2025.webtoeic.repository;

import com.doan2025.webtoeic.domain.User;
import com.doan2025.webtoeic.dto.request.BaseRequest;
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
                            u.student.education, u.student.major)
            FROM User u 
            WHERE u.email = :email

""")
    Optional<UserResponse> findUser(String email);

    @Query(value = """
            SELECT new com.doan2025.webtoeic.dto.response.UserResponse(
                            u.firstName, u.lastName, u.phone, u.address, 
                            u.dob, u.gender, u.avatarUrl, u.isActive, u.isDelete, 
                            u.student.education, u.student.major)
            FROM User u 
            WHERE u.id = :#{#request.id}

""")
    Optional<UserResponse>  findUserById(UserRequest request);

    Optional<User> findById(Long id);
}
