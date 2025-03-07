package com.example.shop_project_v2.comment.service;

import org.springframework.stereotype.Service;

import com.example.shop_project_v2.Inquiry.entity.Inquiry;
import com.example.shop_project_v2.Inquiry.repository.InquiryRepository;
import com.example.shop_project_v2.comment.entity.Comment;
import com.example.shop_project_v2.comment.repository.CommentRepository;
import com.example.shop_project_v2.member.entity.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final InquiryRepository inquiryRepository;
    private final CommentRepository commentRepository;

    public Comment addComment(Long inquiryId, String content, Member member) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new IllegalArgumentException("문의가 존재하지 않습니다."));

        Comment comment = new Comment();
        comment.setInquiry(inquiry);
        comment.setMember(member);
        comment.setContent(content);

        return commentRepository.save(comment);
    }
}
