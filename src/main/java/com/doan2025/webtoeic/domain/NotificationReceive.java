package com.doan2025.webtoeic.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "notification_receive")
@AllArgsConstructor
@NoArgsConstructor
public class NotificationReceive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
