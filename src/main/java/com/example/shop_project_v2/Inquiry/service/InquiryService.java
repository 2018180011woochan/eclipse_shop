package com.example.shop_project_v2.Inquiry.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.shop_project_v2.Inquiry.dto.InquiryRequestDto;
import com.example.shop_project_v2.Inquiry.entity.Inquiry;
import com.example.shop_project_v2.Inquiry.repository.InquiryRepository;
import com.example.shop_project_v2.member.entity.Member;
import com.example.shop_project_v2.member.repository.MemberRepository;
import com.example.shop_project_v2.product.entity.Product;
import com.example.shop_project_v2.product.repository.ProductRepository;

import jakarta.transaction.Transactional;
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
    
    @Transactional
    public void createInquiry(InquiryRequestDto form) {
        // 1) 상품 조회
        Product product = productRepository.findById(form.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
        
        // 2) 로그인 회원: 실제 구현 시 SecurityContext나 세션에서 memberId를 가져와 조회
        Member member = memberRepository.findByEmail("zxcv@naver.com")
        		.orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다.")); // 일단 임시로

        Inquiry inquiry = new Inquiry();
        inquiry.setProduct(product);
        inquiry.setMember(member);
        inquiry.setTitle(form.getTitle());
        inquiry.setContent(form.getContent());
        inquiry.setType(form.getType());
        inquiry.setSecret(form.getIsSecret());
        // createdAt, updatedAt 등 BaseEntity 처리

        inquiryRepository.save(inquiry);
    }
    
}
