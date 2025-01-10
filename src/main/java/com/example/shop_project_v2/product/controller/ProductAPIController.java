package com.example.shop_project_v2.product.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.shop_project_v2.product.dto.ProductResponseDTO;
import com.example.shop_project_v2.product.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductAPIController {
    private final ProductService productService;

    @GetMapping
    public List<ProductResponseDTO> getProducts(@RequestParam(defaultValue = "newest") String sort) {
        return productService.getProductsSorted(sort);
    }
}
