package com.example.shop_project_v2.admin.point;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.shop_project_v2.point.entity.Point;
import com.example.shop_project_v2.point.entity.SavedPoint;
import com.example.shop_project_v2.point.entity.UsedPoint;
import com.example.shop_project_v2.point.repository.PointRepository;
import com.example.shop_project_v2.point.repository.SavedPointRepository;
import com.example.shop_project_v2.point.repository.UsedPointRepository;

import lombok.RequiredArgsConstructor;

@RequestMapping("/admin/point")
@RequiredArgsConstructor
@Controller
public class AdminPointViewController {
    private final PointRepository pointRepository;
    private final SavedPointRepository savedPointRepository;
    private final UsedPointRepository usedPointRepository;
	
	@GetMapping
    public String getPointList(Model model) {

        List<Point> points = pointRepository.findAll();
        List<SavedPoint> savedPoints = savedPointRepository.findAll();
        List<UsedPoint> usedPoints = usedPointRepository.findAll();
       
        model.addAttribute("points", points);
        model.addAttribute("savedPoints", savedPoints);
        model.addAttribute("usedPoints", usedPoints);

        
        return "admin/point/pointListPage"; // 회원 관리 페이지
    }
}
