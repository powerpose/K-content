package com.example.myapp.user.commu.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.myapp.user.commu.service.ICommuService;

import org.apache.commons.io.FilenameUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.myapp.commoncode.model.CommonCode;
import com.example.myapp.commoncode.service.CommonCodeService;
import com.example.myapp.user.commu.dao.ICommuRepository;
import com.example.myapp.user.commu.model.Commu;
import com.example.myapp.user.commu.model.CommuFile;
import com.google.common.collect.Lists;

import jakarta.servlet.http.HttpSession;

@Controller
public class CommuController {
	static final Logger logger = LoggerFactory.getLogger(CommuController.class);

	@Autowired
	ICommuService commuService;

	@Autowired
	private CommonCodeService commonCodeService;

	@GetMapping("/commu/list") // 커뮤니티 메인
	public String main(@RequestParam(defaultValue = "1") int currentPage, @ModelAttribute("commu") Commu commu,
			Model model, HttpSession session) {

		model.addAttribute("showAll", true); // "BEST" 대신 "ALL" 표시

		List<Commu> commulist = commuService.selectAllPost();

		int totalPage = 0;
		int totalCommu = 0;

		// 검색결과가 있는 경우 paging처리
		if (commulist != null && !commulist.isEmpty()) {

			totalCommu = commulist.size();
			int partitionSize = 10;
			List<List<Commu>> partitionedList = Lists.partition(commulist, partitionSize);
			totalPage = partitionedList.size();
			commulist = partitionedList.get(currentPage - 1);
		}

		model.addAttribute("totalPage", totalPage);
		model.addAttribute("currentPage", currentPage);

		model.addAttribute("totalCommu", totalCommu);
		model.addAttribute("commulist", commulist);
		return "user/commu/list";
	}

	// 커뮤니티 게시글 제목 누르면 상세보기
	@GetMapping("/commu/{commuId}")
	public String getCommuDetails(@PathVariable int commuId, Model model) {
		Commu commu = commuService.selectPost(commuId);
		model.addAttribute("commuview", commu);
		logger.info("getCommuDetails" + commu.toString());
		return "user/commu/view";
	}

	// 커뮤니티 게시글 글번호,카테고리에 따른 게시글 상세보기
	@GetMapping("/commu/{commuId}/{commuCateCode}")
	public String getCommuDetails(@PathVariable int commuId, @PathVariable String commuCateCode, Model model) {
		Commu commu = commuService.selectPost(commuId);
		model.addAttribute("commuview", commu);
		model.addAttribute("commuCateCode", commuCateCode);
		model.addAttribute("commuId", commu.getCommuId());
		logger.info("getCommuDetails" + commu.toString());
		return "user/commu/view";
	}

	// 카테고리별 커뮤니티 글쓰기
	@GetMapping("/commu/write/{commuCateCode}")
	public String writePost(Model model) {
		model.addAttribute("showAll", true);
		List<CommonCode> commuCateCodeList = commonCodeService.findCommonCateCodeByUpperCommonCode("C03");
		model.addAttribute("commuCateCodeList", commuCateCodeList);
		logger.info("Fetched commuCateCodeList: " + commuCateCodeList);// 실제 데이터 확인

		return "user/commu/write";
	}

	// 커뮤니티 글쓰기 및 파일 업로드를 처리하는 메서드
	@PostMapping("/commu/write/{commuCateCode}")
	public String writePost(Commu commu,
	                        @RequestParam("commuUploadFiles") MultipartFile[] commuUploadFiles,
	                        BindingResult results,
	                        RedirectAttributes redirectAttrs) {

	    logger.info("writePost method started.");
	    logger.info("Commu object: " + commu.toString());

	    // 게시물 정보 로깅
	    logger.info("/user/commu/write : " + commu.toString());

	    try {
	        // 게시물 내용에서 줄 바꿈을 HTML 태그로 변경
	        commu.setCommuCntnt(commu.getCommuCntnt().replace("\r\n", "<br>"));
	        // 게시물 제목과 내용에 대해 HTML 태그를 제거 (XSS 방지)
	        commu.setCommuTitle(Jsoup.clean(commu.getCommuTitle(), Safelist.basic()));
	        commu.setCommuCntnt(Jsoup.clean(commu.getCommuCntnt(), Safelist.basic()));
	        
	        // 데이터베이스에 게시물 정보 삽입
	        commuService.insertPost(commu);

	        // 첨부파일 리스트 초기화
	        List<CommuFile> commuFiles = new ArrayList<>();

	        if (commuUploadFiles != null && commuUploadFiles.length > 0) {
	            logger.info("Processing uploaded files.");

	            for (MultipartFile uploadFile : commuUploadFiles) {
	                if (!uploadFile.isEmpty()) {
	                    String originalName = uploadFile.getOriginalFilename();
	                    String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);
	                    String uuid = UUID.randomUUID().toString();
	                    String url = "C:\\Users\\KOSA\\Downloads";
	                    File dir = new File(url);
	                    if (!dir.exists()) {
	                        dir.mkdirs();
	                    }
	                    String savefileName = url + File.separator + uuid + "_" + fileName;
	                    Path savePath = Paths.get(savefileName);
	                    
	                    try {
	                        uploadFile.transferTo(savePath);
	                        logger.info("File saved successfully at: " + savePath);

	                        CommuFile file = new CommuFile();
	                        file.setCommuFileId(UUID.randomUUID().toString());
	                        file.setCommuFileName(fileName);
	                        file.setCommuFileSize(uploadFile.getSize());
	                        file.setCommuFileExt(FilenameUtils.getExtension(originalName));
	                        file.setCommuFilePath(savefileName);
	                        file.setCommuFileCommuId(commu.getCommuId());
	                        commuFiles.add(file);
	                        logger.info("Created CommuFile metadata: " + file.toString());

	                    } catch (IOException e) {
	                        logger.error("File saving failed: ", e);
	                    }
	                }
	            }
	            
	            if (!commuFiles.isEmpty()) {
	                logger.info("Attempting to save the following CommuFiles to the DB: " + commuFiles.toString());
	                commuService.insertPost(commu, commuFiles);
	                logger.info("Successfully saved CommuFiles to the DB.");
	            }
	        }
	        
	    } catch (Exception e) {
	        logger.error("Error message", e);
	        redirectAttrs.addFlashAttribute("message", e.getMessage());
	    }

	    return "redirect:/user/commu/view" + commu.getCommuId();
	}
}
