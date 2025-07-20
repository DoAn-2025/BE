package com.doan2025.webtoeic.repository;

import com.doan2025.webtoeic.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Long> {
    @Query("""
                    SELECT ci
                    FROM CartItem ci
                    WHERE ci.user.email = :email
            """)
    List<CartItem> findByEmailUser(String email);
}
