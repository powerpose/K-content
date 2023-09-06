package com.example.myapp.commoncode.service;

import java.util.List;

import com.example.myapp.commoncode.model.CommonCode;

public interface ICommonCodeService {
	
	List<CommonCode> findCommonCateCodeByUpperCommonCode(String upperCommonCode);
	
	List<String> cateList(String upperCommonCode);
}
