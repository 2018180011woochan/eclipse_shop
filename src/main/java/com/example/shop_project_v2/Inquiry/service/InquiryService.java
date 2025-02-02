package com.example.shop_project_v2.Inquiry.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.example.shop_project_v2.Inquiry.dto.InquiryRequestDto;
import com.example.shop_project_v2.Inquiry.entity.Inquiry;
import com.example.shop_project_v2.Inquiry.repository.InquiryRepository;
import com.example.shop_project_v2.member.entity.Member;
import com.example.shop_project_v2.member.repository.MemberRepository;
import com.example.shop_project_v2.member.service.MemberService;
import com.example.shop_project_v2.product.entity.Product;
import com.example.shop_project_v2.product.repository.ProductRepository;
import com.example.shop_project_v2.review.entity.Review;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InquiryService {
    private final InquiryRepository inquiryRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    
    public List<Inquiry> getInquiriesByProduct(Long productId) {
        return inquiryRepository.findByProduct_ProductId(productId);
    }

    public Inquiry getInquiryById(Long inquiryId) {
        return inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new IllegalArgumentException("문의가 존재하지 않습니다."));
    }
    
    @Transactional
    public void createInquiry(InquiryRequestDto form) {
        Product product = productRepository.findById(form.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
        
        Member member = memberService.getCurrentMember();

        Inquiry inquiry = new Inquiry();
        inquiry.setProduct(product);
        inquiry.setMember(member);
        inquiry.setTitle(form.getTitle());
        inquiry.setContent(form.getContent());
        inquiry.setType(form.getType());
        inquiry.setSecret(form.getIsSecret());

        inquiryRepository.save(inquiry);
    }
    
    public Integer getInquiryCountByMember(Member member){
        return inquiryRepository.findByMember(member).size();
    }
    
    public List<Inquiry> findInquirysByMember(Member member) {
        return inquiryRepository.findByMember(member);
    }
   
}
