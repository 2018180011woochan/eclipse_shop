package com.example.shop_project_v2.review.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shop_project_v2.member.entity.Member;
import com.example.shop_project_v2.review.entity.Review;


public interface ReviewRepository extends JpaRepository<Review, Long> {
	List<Review> findByMember(Member member);
}
