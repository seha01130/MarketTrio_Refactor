package com.marketTrio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marketTrio.domain.ReviewEntity;
import com.marketTrio.repository.ReviewRepository;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    //내가 리뷰를 작성했는지 아닌지 확인해주는 메소드. SHService에서 호출함
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
