package com.example.shop_project_v2.member.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.shop_project_v2.BaseEntity;
import com.example.shop_project_v2.coupon.entity.Coupon;
import com.example.shop_project_v2.member.Membership;
import com.example.shop_project_v2.member.Provider;
import com.example.shop_project_v2.member.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Member extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	@Column(name = "email", nullable = false)
	private String email;
	
	@Column(name = "password", nullable = false)
	private String password;
	
	@Column(name = "name", nullable = false)
	private String name;
	 
	@Column(name = "phone", nullable = false)
	private String phone;
	
	@Column(name = "post_no", nullable = true)
	private String postNo;
	
	@Column(name = "address", nullable = true)
	private String address;

	@Column(name = "address_detail", nullable = true)
	private String addressDetail;
	
	@Column(name = "nickname", nullable = false)
	private String nickname;
	
	// 삭제 예정
	@Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
	
	@Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
	
	// 프로필 사진 URL
    @Column(name = "profile_image_url")
    private String profileImageUrl;

    // 권한 (스프링 시큐리티와 연동)
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    // 회원 등급
    @Enumerated(EnumType.STRING)
    @Column(name = "membership")
    private Membership membership;
    
    @Column(name = "withdraw", nullable = false)
    private Boolean withdraw;
    
    private LocalDateTime withdrawDate;
    
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Coupon> coupons = new ArrayList<>();
    
    // OAuth2를 사용하는 경우 provider 저장
    @Column(name = "provider")
    @Enumerated(EnumType.STRING)
    private Provider provider;
   
	@PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        
        this.createdAt = now;
        this.updatedAt = now;
        
        // 기본값 설정
        if (this.role == null) 
            this.role = Role.USER;
        
        if (this.membership == null) 
            this.membership = Membership.BRONZE;
        
        if (this.provider == null) 
            this.provider = Provider.NONE;
        
        withdraw = false;
    }
	
	@PreUpdate
    public void preUpdate() {
		this.updatedAt = LocalDateTime.now();
    }
	
}
