package com.example.myapp.user.bkmk.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.example.myapp.user.bkmk.model.CntntBkmk;

@Repository
@Mapper
public interface IBkmkRepository {
	List<CntntBkmk> selectCntntBkmkList(@Param("mberId") String mberId);
	void insertCntntBkmkList(@Param("mberId") String mberId, @Param("cntntId") int cntntId);
	void deleteCntntBkmkList(@Param("mberId") String mberId, @Param("cntntId") int cntntId);
	int selectCntntBkmk(@Param("mberId") String mberId, @Param("cntntId") int cntntId);

}
