package com.marketTrio.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketTrio.review.domain.ReviewEntity;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Integer> {
	boolean existsBySenderIdAndSHPostId(String memberId, int postId);
	int countByReceiverId(String memberId);
}
