package com.example.shop_project_v2.point.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shop_project_v2.member.entity.Member;
import com.example.shop_project_v2.member.service.MemberService;
import com.example.shop_project_v2.point.dto.PointResponseDto;
import com.example.shop_project_v2.point.entity.Point;
import com.example.shop_project_v2.point.repository.PointRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/points")
@RequiredArgsConstructor
public class PointAPIController {
    private final PointRepository pointRepository;
    private final MemberService memberService;
    
    @GetMapping("/me")
    public ResponseEntity<PointResponseDto> getMyPoint() {

        Member member = memberService.getCurrentMember();

        Point point = pointRepository.findByMember(member)
            .orElseThrow(() -> new IllegalArgumentException("포인트 정보가 없습니다."));

        PointResponseDto response = new PointResponseDto(point.getBalance());
        return ResponseEntity.ok(response);
    }
    
}
