package com.example.shop_project_v2.order.entity;

import com.example.shop_project_v2.BaseEntity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    // Order와 다대일(N:1) 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    
	private String thumbnailUrl;

    // 상품 정보가 바뀌어도 과거 주문 정보는 변하지 않아야 함
    private Long productId;       // product_id
    private String productName;   // 상품명
    private String color;         // 옵션 - 색상
    private String size;          // 옵션 - 사이즈
    private int quantity;         // 구매 수량
    private int unitPrice;        // 단가 (상품 가격)
    private int totalPrice;       // (단가 * 수량) 계산
    private Boolean isReview;

    public void calculatePrice() {
        this.totalPrice = this.unitPrice * this.quantity;
    }
}