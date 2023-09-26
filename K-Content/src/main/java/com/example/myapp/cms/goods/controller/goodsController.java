package com.example.myapp.cms.goods.controller;

import com.example.myapp.cms.goods.model.Goods;
import com.example.myapp.cms.goods.model.GoodsFile;
import com.example.myapp.cms.goods.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/cs/goods")
public class goodsController {
    @Autowired
    IGoodsService goodsService;
    @Value("${part4.upload.path}")
    private String uploadPath;
    @Value("${goods}")
    private String url;

    // admin side-bar 상품 리스트 접근
    @GetMapping("/main")
    public String getGoodsPages() {
        return "cms/goods/goodsListMain";
    }

    @GetMapping("")
    public String getPages() {
        return "cms/goods/goodsList";
    }

    // 상품 상세 화면 보여주기
    @GetMapping("/detail")
    public String getGoodsDetail(@RequestParam(value = "goodsId") int goodsId, Model model) {

        Goods goods = goodsService.getGoodsJFileByGoodsId(goodsId);

        model.addAttribute("goods", goods);

        return "cms/goods/goodsDetail";
    }

    //모든 상품 리스트 가져오기
    @GetMapping("/list")
    @ResponseBody
    public List<Goods> getAllGoods() {
        List<Goods> goodsList = goodsService.getAllGoodsJFile();
        return goodsList;
    }

    // 상품 검색 결과 출력
    @GetMapping("/search")
    @ResponseBody
    public List<Goods> getSearchGoods(String search) {
        List<Goods> goodsList = goodsService.getSearchGoodsJFile(search);
        return goodsList;
    }

    @GetMapping("/item")
    @ResponseBody
    public List<Goods> getSearchGoodsResult(@RequestParam(value = "sendData") List<String> receivedData) {
        List<Goods> goodsList = new ArrayList<>();
        for (int i = 0; i < receivedData.size(); i++) {
            goodsList.add(goodsService.getGoodsJFileByGoodsId(Integer.parseInt(receivedData.get(i))));
        }

        return goodsList;
    }

    @GetMapping("/form")
    public String getMakeGoodsForm() {

        return "cms/goods/makeGoodsInNav";
    }

    // 상품 생성
    @PostMapping("")
    public ResponseEntity<String> createGoods(@RequestParam("goodsTitle") String goodsTitle,
                                              @RequestParam("goodsBrand") String goodsBrand,
                                              @RequestParam("goodsURL") String goodsURL,
                                              @RequestParam("goodsPrice") String goodsPrice,
                                              @RequestParam("keywordList") List<String> keywordListJson,
                                              @RequestParam("goodsFile") MultipartFile goodsFile) throws IOException {

//        try {

        String keywordlist = keywordListJson.toString();

        String keyword = keywordListJson.toString().substring(1, keywordlist.length() - 1);
        String originalEncodingFilename = Normalizer.normalize(goodsFile.getOriginalFilename(), Normalizer.Form.NFC);
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        String originalFilename = originalEncodingFilename;
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = uuidString + "_" + originalFilename;

        GoodsFile newGoodsFile = new GoodsFile();
        Goods newGoods = new Goods();

        newGoodsFile.setGoodsFileID(newFilename);
        newGoodsFile.setGoodsFileName(originalFilename);
        newGoodsFile.setGoodsFileExt(fileExtension);
        newGoodsFile.setGoodsFileSize(goodsFile.getSize());
        newGoodsFile.setGoodsFilePath(url);

        newGoods.setGoodsName(goodsTitle);
        newGoods.setGoodsBrand(goodsBrand);
        newGoods.setGoodsPrice(Integer.parseInt(goodsPrice));
        newGoods.setGoodsPurchsLink(goodsURL);
        newGoods.setGoodsKwrd(keyword);

        Path path = Paths.get(uploadPath + url).toAbsolutePath().normalize();

//        newFilename = new String(newFilename.getBytes(StandardCharsets.ISO_8859_1),"UTF-8");
        Path realPath = path.resolve(newFilename).normalize();

        int rowsAffected = goodsService.insertGoods(newGoods, newGoodsFile);
        goodsFile.transferTo(realPath);

        if (rowsAffected <= 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("goods 업로드 실패");
        }
        return ResponseEntity.ok("goods 업로드 및 처리 완료");
//TODO try catch 문 작성

//        }
//        catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("굿즈 업로드 실패");
//        }    // JSON 문자열을 객체로 변환 (keywordListJson)


    }

    //상품 수정 폼
    @GetMapping("modify-form")
    public String updateGoods(@RequestParam(value = "goodsId") int goodsId, Model model) {

        Goods goods = goodsService.getGoodsJFileByGoodsId(goodsId);

        model.addAttribute("goods", goods);
        //키워드
        List<String> keywordList = Arrays.stream(goods.getGoodsKwrd().split(",")).toList();
        model.addAttribute("keywordList", keywordList);

        return "cms/goods/makeGoodsInNav";
    }

    //상품삭제
    @PatchMapping("")
    public String createGoods(@RequestParam("goodsId") int goodsId) {
        System.out.println(goodsId);
        goodsService.updateDelYnGoods(goodsId);
        return "cms/goods/goodsListMain";
    }

    //상품 수정
    @PutMapping("")
    public ResponseEntity<String> updateGoods(@RequestParam("goodsId") int goodsId,
                                              @RequestParam("goodsTitle") String goodsTitle,
                                              @RequestParam("goodsBrand") String goodsBrand,
                                              @RequestParam("goodsURL") String goodsURL,
                                              @RequestParam("goodsPrice") String goodsPrice,
                                              @RequestParam("keywordList") List<String> keywordListJson,
                                              @RequestParam("goodsFile") MultipartFile goodsFile) throws IOException {

//        try {

        String keywordlist = keywordListJson.toString();

        String keyword = keywordListJson.toString().substring(1, keywordlist.length() - 1);
        String originalEncodingFilename = Normalizer.normalize(goodsFile.getOriginalFilename(), Normalizer.Form.NFC);
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        String originalFilename = originalEncodingFilename;
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = uuidString + "_" + originalFilename;

        GoodsFile newGoodsFile = new GoodsFile();
        Goods newGoods = new Goods();

        newGoodsFile.setGoodsFileID(newFilename);
        newGoodsFile.setGoodsFileName(originalFilename);
        newGoodsFile.setGoodsFileExt(fileExtension);
        newGoodsFile.setGoodsFileSize(goodsFile.getSize());
        newGoodsFile.setGoodsFilePath(url);

        newGoods.setGoodsName(goodsTitle);
        newGoods.setGoodsBrand(goodsBrand);
        newGoods.setGoodsPrice(Integer.parseInt(goodsPrice));
        newGoods.setGoodsPurchsLink(goodsURL);
        newGoods.setGoodsKwrd(keyword);

        Path path = Paths.get(uploadPath + url).toAbsolutePath().normalize();

//        newFilename = new String(newFilename.getBytes(StandardCharsets.ISO_8859_1),"UTF-8");
        Path realPath = path.resolve(newFilename).normalize();

        //TODO 굿드 파일은 일단 있는거 지우고
        goodsService.deleteGoodsFile(goodsId);
        //TODO  다시 파일 넣기

        //TODO 수정으로 바꾸기
        int rowsAffected = goodsService.insertGoods(newGoods, newGoodsFile);
        goodsFile.transferTo(realPath);

        if (rowsAffected <= 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("goods 업로드 실패");
        }
        return ResponseEntity.ok("goods 업로드 및 처리 완료");
//TODO try catch 문 작성

//        }
//        catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("굿즈 업로드 실패");
//        }    // JSON 문자열을 객체로 변환 (keywordListJson)


    }
}
