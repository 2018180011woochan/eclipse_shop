package com.example.shop_project_v2.Inquiry.dto;

import com.example.shop_project_v2.Inquiry.InquiryType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquiryRequestDto {
    private Long productId; 

    @NotBlank(message = "제목은 필수 입력 항목입니다.")
    private String title; 

    @NotBlank(message = "내용은 필수 입력 항목입니다.")
    private String content;

    @NotNull(message = "문의 유형은 필수 입력 항목입니다.")
    private InquiryType type; 

    private Boolean isSecret; 
}
