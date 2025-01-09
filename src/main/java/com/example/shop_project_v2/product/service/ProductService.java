package com.example.shop_project_v2.product.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.shop_project_v2.product.dto.ProductRequestDTO;
import com.example.shop_project_v2.product.entity.Product;
import com.example.shop_project_v2.product.entity.ProductImage;
import com.example.shop_project_v2.product.repository.ProductImageRepository;
import com.example.shop_project_v2.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductService {
	private final ProductRepository productRepository;
	private final ProductImageRepository productImageRepository;
	
	public void CreateProduct(ProductRequestDTO productRequestDto) {
		Product product = Product.builder()
				.name(productRequestDto.getName())
				.price(productRequestDto.getPrice())
				.description(productRequestDto.getDescription())
				.build();
		
		productRepository.save(product);
		
		List<MultipartFile> imageFiles = productRequestDto.getImages();
		if (imageFiles != null && !imageFiles.isEmpty()) {
			for (MultipartFile imageFile : imageFiles) {
				String imageUrl = saveImage(imageFile);
				ProductImage productImage = new ProductImage();
				productImage.setProduct(product);
				productImage.setImageUrl(imageUrl);
				productImageRepository.save(productImage);
			}
		}
	}
	
	private String saveImage(MultipartFile imageFile) {
		if (imageFile.isEmpty()) return null;
		
		try {
			String uploadDir = "C:/uploads/";
			String fileName = imageFile.getOriginalFilename();
			String filePath = uploadDir + fileName;
			
			Path path = Paths.get(filePath);
			Files.createDirectories(path.getParent());
			Files.write(path, imageFile.getBytes());
			
			return "/uploads/" + fileName;
			
		} catch (IOException e) {
			throw new RuntimeException("이미지 저장 중 오류 발생", e);
		}
	}
	
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}
}
