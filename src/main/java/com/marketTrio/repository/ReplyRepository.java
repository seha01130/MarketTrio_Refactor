package com.marketTrio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.marketTrio.domain.Member;
import com.marketTrio.domain.ReplyEntity;


@Repository
public interface ReplyRepository extends JpaRepository<ReplyEntity, Integer>{

	
	// createDate 순서대로 List받아오기
	// 여기서 에러 발생!!!!! => ReplyEntity에서는 SHEntity를 끌어다가 써서 ReplyRepository엔 SHEntity가 들어있어서 에러남!!
	List<ReplyEntity> findBySHEntity_SHPostIdOrderByCreateDateAsc(int SHPostId);
	
//	List<Member> findDistinctUserBySHPostId(int SHPostId);
    @Query("SELECT DISTINCT r.member FROM ReplyEntity r WHERE r.SHEntity.SHPostId = :SHPostId")
    List<Member> findDistinctUserBySHPostId(@Param("SHPostId") int SHPostId);

	List<ReplyEntity> getBySHEntity_SHPostId(int shPostId);
	


}
