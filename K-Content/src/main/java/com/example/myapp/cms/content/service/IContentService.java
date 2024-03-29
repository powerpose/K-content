package com.example.myapp.cms.content.service;

import com.example.myapp.cms.content.model.CmsContent;
import java.util.List;

public interface IContentService {
    List<CmsContent> getAllContent(String commonCodeVal, int page);

    CmsContent getAContent(int id);

    int insertAContent(CmsContent content, List<Integer> goodsList);

    int updateAContent(CmsContent content, List<Integer> goodsList);

    List<CmsContent> getContentByKeyword(List<String> keywordList);

    List<CmsContent> getPagingContentBySearch(List<String> keywordList, int page);

    void updateDelStat(int cntntId);

    int totalCntnt(String commonCodeVal);
    
    int contentTotalCnt();
    
    int totalSearch(List<String> keywordList);
}

