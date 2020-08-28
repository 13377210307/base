package com.elasticsearch.service.impl;

import com.elasticsearch.entity.Content;
import com.elasticsearch.entity.Page;
import com.elasticsearch.service.SearchService;
import com.elasticsearch.utils.ElasticsearchUtil;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private ElasticsearchUtil elasticsearchUtil;

    private String index = "jd_goods";

    @Override
    public List<Map<String,Object>> accurateSearch(Page page, String keyWord) {

        List<Map<String,Object>> contents = new ArrayList<>();
        try {
            SearchHit[] hits = this.elasticsearchUtil.searchAccurateDocument(index, "title", keyWord, page);
            if (hits.length > 0) {
                for (SearchHit hit : hits) {
                    contents.add(hit.getSourceAsMap());
                }
            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return contents;
    }

    @Override
    public List<Map<String,Object>> highLightSearch(Page page, String keyWord) {

        List<Map<String,Object>> maps = new ArrayList<>();
        try {
            maps = this.elasticsearchUtil.searchHighLight(index, "title", keyWord, page);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return maps;
    }
}
