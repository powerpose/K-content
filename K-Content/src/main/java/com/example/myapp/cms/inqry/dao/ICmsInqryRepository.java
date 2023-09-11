package com.example.myapp.cms.inqry.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.example.myapp.cms.inqry.model.CmsInqry;

@Repository
@Mapper
public interface ICmsInqryRepository {
	List<CmsInqry> selectCmsInqryList();
}