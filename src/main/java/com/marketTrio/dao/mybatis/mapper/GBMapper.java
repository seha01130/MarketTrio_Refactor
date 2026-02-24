package com.marketTrio.dao.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.marketTrio.domain.GBParticipantEntity;
import com.marketTrio.domain.OptionEntity;

@Mapper
public interface GBMapper {
	
	// JPA 쓰면 안 해도 되나?
//	List<GBPostEntity> getGBPostList();
//	
//	GBPostEntity getGBPost(int GBPostId);
//	
//	void createGBPost(GBInfoCommand gbInfo);
//	
//	void deleteGBPost(int GBPostId);
//	
//	void getUpdateGBPost(GBUpdateInfoCommand gbUpdateInfo);
//	
//	void updateGBPost(GBParticipateCommand gbParticipate);
			
	// ----------- 
			
	void insertGBParticipant(GBParticipantEntity GBParticipant);
	
	void updateOption(GBParticipantEntity GBParticipant);
	
	void deleteGBParticipant(GBParticipantEntity GBParticipant);

	void insertOption(OptionEntity Option);
	
}
