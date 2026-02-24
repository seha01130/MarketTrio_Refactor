package com.marketTrio.reply.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.marketTrio.member.domain.Member;
import com.marketTrio.reply.domain.ReplyEntity;

public interface ReplyRepository extends JpaRepository<ReplyEntity, Integer> {
	List<ReplyEntity> findBySHEntity_SHPostIdOrderByCreateDateAsc(int SHPostId);

	@Query("SELECT DISTINCT r.member FROM ReplyEntity r WHERE r.SHEntity.SHPostId = :SHPostId")
	List<Member> findDistinctUserBySHPostId(@Param("SHPostId") int SHPostId);

	List<ReplyEntity> getBySHEntity_SHPostId(int shPostId);
}
