package com.example.shop_project_v2.point.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shop_project_v2.member.entity.Member;
import com.example.shop_project_v2.point.entity.Point;


public interface PointRepository extends JpaRepository<Point, Long> {
	Optional<Point> findByMember(Member member);
}
