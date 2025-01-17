package com.example.shop_project_v2.order.entity;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

import com.example.shop_project_v2.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders") // 테이블명을 "orders"로 지정
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    // 예: 주문자 정보(회원이라면 Member 엔티티와 연관관계를 맺을 수도 있음)
    private Long memberId;        // 간단히 memberId만 저장
    private String address;       // 배송 주소
    private String paymentMethod; // 결제 수단 (카카오페이, 토스페이 등)

    // 주문 총액 (주문 아이템 가격 합계)
    private int totalPrice;

    // 주문 아이템 목록
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    /**
     * 주문 총액 계산 로직 (예시)
     */
    public void calculateTotalPrice() {
        this.totalPrice = orderItems.stream()
                                    .mapToInt(OrderItem::getTotalPrice)
                                    .sum();
    }
}

