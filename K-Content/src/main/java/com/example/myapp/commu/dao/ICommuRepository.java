package com.example.myapp.commu.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.example.myapp.commu.model.Commu;
import com.example.myapp.commu.model.CommuFile;

@Repository
@Mapper
public interface ICommuRepository {
	
	List<Commu> selectAllPost();
	

	
	int selectMaxPost();
	
	void insertPost(Commu commu);
	void insertFileData(CommuFile file);
	CommuFile getFile(int commuFileId);
	
	
}