package com.example.shop_project_v2.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shop_project_v2.review.entity.Review;


public interface ReviewRepository extends JpaRepository<Review, Long> {

}
