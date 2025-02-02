package com.example.shop_project_v2.order.entity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.shop_project_v2.BaseEntity;
import com.example.shop_project_v2.member.Membership;
import com.example.shop_project_v2.member.Role;
import com.example.shop_project_v2.member.entity.Member;
import com.example.shop_project_v2.order.OrderStatus;
import com.example.shop_project_v2.point.entity.UsedPoint;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders") 
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;
    
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    private String address; 
    private String paymentMethod; 

    // 주문 총액 (주문 아이템 가격 합계)
    private int totalPrice;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UsedPoint usedPoint; // 사용한 포인트 추가
    
	@PrePersist
    public void prePersist() {
		orderStatus = OrderStatus.NEW;
    }
    
    // 주문 아이템 목록
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void updateStatus(OrderStatus status) {
        orderStatus = status;
    }
    public void calculateTotalPrice() {
        this.totalPrice = orderItems.stream()
                                    .mapToInt(OrderItem::getTotalPrice)
                                    .sum();
    }
}

