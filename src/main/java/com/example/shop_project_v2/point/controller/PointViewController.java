package com.example.shop_project_v2.point.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.shop_project_v2.member.entity.Member;
import com.example.shop_project_v2.member.service.MemberService;
import com.example.shop_project_v2.point.entity.Point;
import com.example.shop_project_v2.point.entity.SavedPoint;
import com.example.shop_project_v2.point.entity.UsedPoint;
import com.example.shop_project_v2.point.service.PointService;
import com.example.shop_project_v2.review.entity.Review;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/point")
@RequiredArgsConstructor
public class PointViewController {
	private final PointService pointService;
	private final MemberService memberService;
	
	@GetMapping("/history")
	public String PointList(Model model) {
		Member currentMember = memberService.getCurrentMember();
    	
    	Point points = pointService.findByMember(currentMember).orElse(null);
    	model.addAttribute("points", points);
    	
    	List<UsedPoint> usedPoints = pointService.findUsedPointsByMember(points);
    	model.addAttribute("usedPoints", usedPoints);
    	
    	List<SavedPoint> savedPoints = pointService.findSavedPointsByMember(points);
    	model.addAttribute("savedPoints", savedPoints);
		
		return "point/pointHistory";
	}
	
}
