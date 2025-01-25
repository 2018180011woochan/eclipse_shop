package com.example.shop_project_v2.point.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.shop_project_v2.point.entity.Point;
import com.example.shop_project_v2.point.entity.SavedPoint;


@Repository
public interface SavedPointRepository extends JpaRepository<SavedPoint, Long> {
	List<SavedPoint> findAllByPoint(Point point);
}
