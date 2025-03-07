package com.example.shop_project_v2.review.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.shop_project_v2.member.entity.Member;
import com.example.shop_project_v2.order.entity.Order;
import com.example.shop_project_v2.review.entity.Review;


public interface ReviewRepository extends JpaRepository<Review, Long> {
	List<Review> findByMember(Member member);
	List<Review> findByProductId(Long productId);
	
    // 특정 상품의 평균 별점 구하기
	@Query("SELECT AVG(r.stars) FROM Review r WHERE r.productId = :productId")
	Double findAverageStarsByProductId(@Param("productId") Long productId);
}
