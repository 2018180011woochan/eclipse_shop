package com.example.shop_project_v2.point.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.shop_project_v2.member.entity.Member;
import com.example.shop_project_v2.point.entity.Point;
import com.example.shop_project_v2.point.entity.SavedPoint;
import com.example.shop_project_v2.point.entity.UsedPoint;
import com.example.shop_project_v2.point.repository.PointRepository;
import com.example.shop_project_v2.point.repository.SavedPointRepository;
import com.example.shop_project_v2.point.repository.UsedPointRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PointService {
	private final PointRepository pointRepository;
	private final UsedPointRepository usedPointRepository;
	private final SavedPointRepository savedPointRepository;
	
	public Optional<Point> findByMember(Member member) {
		return pointRepository.findByMember(member);
	}
	
	public List<UsedPoint> findUsedPointsByMember(Point point) {
		return usedPointRepository.findAllByPoint(point);
	}
	
	public List<SavedPoint> findSavedPointsByMember(Point point) {
		return savedPointRepository.findAllByPoint(point);
	}
}
