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

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/inquiry")
@RequiredArgsConstructor
public class InquiryViewController {
	private final InquiryService inquiryService;
	
	@GetMapping
    public String listByProduct(@RequestParam("productId") Long productId, Model model) {
        List<Inquiry> inquiries = inquiryService.getInquiriesByProduct(productId);
        model.addAttribute("inquiries", inquiries);
        model.addAttribute("productId", productId);
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

    // 4) 문의 저장
    @PostMapping
    public String createInquiry(@ModelAttribute InquiryRequestDto form) {
        // form: title, content, type, isSecret, productId 등
        inquiryService.createInquiry(form);
        System.out.println("!!!!!!!!" + form.getProductId());
        return "redirect:/inquiry?productId=" + form.getProductId();
    }
}
