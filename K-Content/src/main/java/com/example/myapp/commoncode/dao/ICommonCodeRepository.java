package com.example.myapp.commoncode.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.example.myapp.commoncode.model.CommonCode;

@Repository
@Mapper
public interface ICommonCodeRepository {
	List<CommonCode> findByUpperCommonCode(String upperCommonCode);

	List<String> cateList(String upperCommonCode);

	String mberStatByCode(String mberId);

	String mberRoleByCode(String mberId);

	String mberGenderByCode(String mberId);
	CommonCode findByCommonCode(String commonCode);
	
	List<CommonCode> findByCommonCodeVal(String CommonCodeVal);

}
