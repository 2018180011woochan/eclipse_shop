package com.example.shop_project_v2.point.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.shop_project_v2.member.entity.Member;
import com.example.shop_project_v2.member.repository.MemberRepository;
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
	private final MemberRepository memberRepository;
	
	public Optional<Point> findByMember(Member member) {
		return pointRepository.findByMember(member);
	}
	
	public List<UsedPoint> findUsedPointsByMember(Point point) {
		return usedPointRepository.findAllByPoint(point);
	}
	
	public List<SavedPoint> findSavedPointsByMember(Point point) {
		return savedPointRepository.findAllByPoint(point);
	}
	
    // 회원가입할 때 같이 생성
    public void createPointByEmail(String email) {      
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Point point = Point.builder()
                .member(member)
                .balance(0) 
                .build();
        pointRepository.save(point);

        SavedPoint savedPoint = SavedPoint.builder()
                .point(point)
                .savedPoint(500) 
                .saveReason("가입 축하 포인트") 
                .build();
        savedPointRepository.save(savedPoint);

        point.savePoint(savedPoint);
        pointRepository.save(point);
    }

}
