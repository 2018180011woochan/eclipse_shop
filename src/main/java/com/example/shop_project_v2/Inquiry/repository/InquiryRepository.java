package com.example.shop_project_v2.Inquiry.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shop_project_v2.Inquiry.entity.Inquiry;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
	List<Inquiry> findByProduct_ProductId(Long productId);
}
