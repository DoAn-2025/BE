package com.doan2025.webtoeic.domain;

import com.doan2025.webtoeic.constants.enums.EOrderStatus;
import com.doan2025.webtoeic.constants.enums.EPayment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "order")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
    private User user;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "price")
    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EOrderStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private EPayment paymentMethod;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

}
