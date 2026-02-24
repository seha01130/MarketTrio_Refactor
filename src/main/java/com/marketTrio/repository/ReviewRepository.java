package com.marketTrio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.marketTrio.domain.ReviewEntity;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Integer> {
    boolean existsBySenderIdAndSHPostId(String memberId, int postId);
    
    int countByReceiverId(String memberId);
}
