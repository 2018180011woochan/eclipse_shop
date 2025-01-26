package com.example.shop_project_v2.point.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.shop_project_v2.order.entity.Order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class UsedPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long usedPointId;
    @Column(nullable = false)
    private Integer usedPoint;
    @Column(nullable = false)
    private String usedReason;
    @CreatedDate
    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "point_id", nullable = false)
    private Point point;

    @OneToOne
    @JoinColumn(name = "order_no", nullable = false)
    private Order order;

    public void assignPointToCreate(Point point){
        this.point = point;
    }
}
