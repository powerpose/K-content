package com.example.myapp.user;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.myapp.commoncode.dao.ICommonCodeRepository;
import com.example.myapp.commoncode.model.CommonCode;
import com.example.myapp.commoncode.service.ICommonCodeService;

@Controller
public class MainCon {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	ICommonCodeService commonCodeService;

    @GetMapping("/")
    public String getCate(Model model) {
    	// 공통코드를 이용한 content의 카테고리 조회
    	List<String> cateList = commonCodeService.cateList("C03");
    	model.addAttribute("cateList", cateList);
    	    	
        return "user/index";
    }
}