package com.example.shop_project_v2.member.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.shop_project_v2.member.Membership;
import com.example.shop_project_v2.member.Role;
import com.example.shop_project_v2.member.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByEmail(String email);
	Optional<Member> findByNickname(String nickname);
	Optional<Member> findById(Long id);
	List<Member> findAllByMembership(Membership membership);

	boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    
    long countByRole(Role role);
    
    // withdraw가 true이면서 withdrawDate가 주어진 날짜 이전인 회원 찾기
    List<Member> findByWithdrawTrueAndWithdrawDateBefore(LocalDateTime dateTime);
}