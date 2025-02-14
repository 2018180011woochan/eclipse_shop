package com.example.shop_project_v2.order.service;

import java.util.concurrent.CountDownLatch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.shop_project_v2.order.entity.Order;
import com.example.shop_project_v2.order.entity.OrderItem;
import com.example.shop_project_v2.product.Size;
import com.example.shop_project_v2.product.entity.Product;
import com.example.shop_project_v2.product.entity.ProductOption;
import com.example.shop_project_v2.product.repository.ProductOptionRepository;
import com.example.shop_project_v2.product.repository.ProductRepository;

@SpringBootTest
public class OptimisticLockTest {
    
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductOptionRepository productOptionRepository;
    @Autowired
    private ProductRepository productRepository;

    private Long productId;           
    private Long productOptionId;     

    @BeforeEach
    void 테스트_준비() {
        // 테스트용 상품 생성 
        Product product = new Product();
        product.setName("테스트 상품");
        product.setPrice(1000);
        product.setSalesCount(0);
        product = productRepository.save(product);

        this.productId = product.getProductId();

        ProductOption productOption = new ProductOption();
        productOption.setProduct(product);
        productOption.setColor("RED");
        productOption.setSize(Size.M);
        productOption.setStockQuantity(10);  // 재고 10개
        productOptionRepository.save(productOption);

        this.productOptionId = productOption.getOptionId();
    }

    @Test
    void 상품주문_낙관적락_테스트() throws InterruptedException {
        int threadCount = 2;
        int orderQuantity = 7; // 각 스레드가 7개씩 주문

        CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                readyLatch.countDown();

                try {
                    startLatch.await();
                    
                    Order order = new Order();
                    
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProductId(productId);
                    orderItem.setColor("RED");
                    orderItem.setSize(Size.M);
                    orderItem.setQuantity(orderQuantity);
                    orderItem.setUnitPrice(1000); 
                    
                    order.addOrderItem(orderItem);

                    orderService.createOrder(order);
                } catch (Exception e) {
                    System.out.println(
                        "Thread " + Thread.currentThread().getName() + 
                        " Exception: " + e.getClass().getSimpleName() + 
                        " - " + e.getMessage()
                    );
                }
                doneLatch.countDown();
            }).start();
        }

        readyLatch.await();
        startLatch.countDown();
        doneLatch.await();

        ProductOption productOption = productOptionRepository.findById(productOptionId)
            .orElseThrow(() -> new RuntimeException("Option not found"));
        
        System.out.println("최종 재고: " + productOption.getStockQuantity());
        System.out.println("판매 횟수: " + productOption.getProduct().getSalesCount());
    }
}
