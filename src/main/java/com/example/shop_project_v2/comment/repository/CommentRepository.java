package com.example.shop_project_v2.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shop_project_v2.comment.entity.Comment;


public interface CommentRepository extends JpaRepository<Comment, Long> {
}
