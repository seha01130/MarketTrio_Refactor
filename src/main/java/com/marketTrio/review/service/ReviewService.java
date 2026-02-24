package com.marketTrio.review.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marketTrio.review.domain.ReviewEntity;
import com.marketTrio.review.repository.ReviewRepository;

@Service
public class ReviewService {

	@Autowired
	private ReviewRepository reviewRepository;

	@Transactional(readOnly = true)
	public boolean hasReviewed(String memberId, int postId) {
		return reviewRepository.existsBySenderIdAndSHPostId(memberId, postId);
	}

	@Transactional
	public ReviewEntity insertReview(ReviewEntity review) {
		return reviewRepository.save(review);
	}

	@Transactional(readOnly = true)
	public int rateCount(String memberId) {
		return reviewRepository.countByReceiverId(memberId);
	}
}
