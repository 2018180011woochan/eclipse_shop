package com.example.shop_project_v2.point.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.shop_project_v2.point.entity.Point;
import com.example.shop_project_v2.point.entity.SavedPoint;
import com.example.shop_project_v2.point.entity.UsedPoint;


@Repository
public interface UsedPointRepository extends JpaRepository<UsedPoint, Long> {
	List<UsedPoint> findAllByPoint(Point point);
}
