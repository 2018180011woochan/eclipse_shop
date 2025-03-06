package com.example.shop_project_v2.Inquiry.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shop_project_v2.Inquiry.dto.InquiryRequestDto;
import com.example.shop_project_v2.Inquiry.entity.Inquiry;
import com.example.shop_project_v2.Inquiry.service.InquiryService;
import com.example.shop_project_v2.member.entity.Member;
import com.example.shop_project_v2.member.service.MemberService;
import com.example.shop_project_v2.product.entity.Product;
import com.example.shop_project_v2.product.service.ProductService;
import com.example.shop_project_v2.review.entity.Review;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/inquiry")
@RequiredArgsConstructor
public class InquiryViewController {
	private final InquiryService inquiryService;
	private final MemberService memberService;
	private final ProductService productService;
	
	@GetMapping
	public String listByProduct(@RequestParam("productId") Long productId, Model model) {
	    Product product = productService.FindById(productId);
	    
	    List<Inquiry> inquiries = inquiryService.getInquiriesByProduct(productId);
	    
	    model.addAttribute("product", product);      // 상품 정보
	    model.addAttribute("inquiries", inquiries);  // 문의 목록
	    
	    return "inquiry/inquiryList"; 
	}
	
    @GetMapping("/{inquiryId}")
    public String detail(@PathVariable Long inquiryId, Model model) {
        Inquiry inquiry = inquiryService.getInquiryById(inquiryId);
        model.addAttribute("inquiry", inquiry);
        return "inquiry/inquiryDetail";
    }
    
    @GetMapping("/create")
    public String newForm(@RequestParam Long productId, Model model) {
        model.addAttribute("productId", productId);
        InquiryRequestDto dto = new InquiryRequestDto();
        dto.setProductId(productId); 
        model.addAttribute("inquiryForm", dto);
        return "inquiry/inquiryForm";
    }
    
    @GetMapping("/history")
    public String inquiryHistory(Model model) {
    	Member currentMember = memberService.getCurrentMember();
    	List<Inquiry> inquirys = inquiryService.findInquirysByMember(currentMember);
    	model.addAttribute("inquirys", inquirys);
    	return "inquiry/inquiryHistory";
    }

    @PostMapping
    public String createInquiry(@ModelAttribute InquiryRequestDto form) {
        inquiryService.createInquiry(form);
        return "redirect:/inquiry?productId=" + form.getProductId();
    }
}
