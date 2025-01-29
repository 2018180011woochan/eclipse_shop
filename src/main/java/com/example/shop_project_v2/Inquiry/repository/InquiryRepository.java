package com.example.shop_project_v2.Inquiry.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shop_project_v2.Inquiry.entity.Inquiry;
import com.example.shop_project_v2.member.entity.Member;
import com.example.shop_project_v2.review.entity.Review;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
	List<Inquiry> findByProduct_ProductId(Long productId);
	List<Inquiry> findByMember(Member member);
}
