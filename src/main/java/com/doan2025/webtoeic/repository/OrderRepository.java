package com.doan2025.webtoeic.repository;

import com.doan2025.webtoeic.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    @Query("""
            SELECT o
            FROM Orders o
            WHERE o.user.email = :email
            """)
    Optional<List<Orders>> findByEmail(String email);


}
