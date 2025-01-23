package com.example.shop_project_v2.Inquiry.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.shop_project_v2.Inquiry.entity.Inquiry;
import com.example.shop_project_v2.Inquiry.repository.InquiryRepository;
import com.example.shop_project_v2.member.repository.MemberRepository;
import com.example.shop_project_v2.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InquiryService {
    private final InquiryRepository inquiryRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    
    public List<Inquiry> getInquiriesByProduct(Long productId) {
        return inquiryRepository.findByProduct_ProductId(productId);
    }

    public Inquiry getInquiryById(Long inquiryId) {
        return inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new IllegalArgumentException("문의가 존재하지 않습니다."));
    }
}
