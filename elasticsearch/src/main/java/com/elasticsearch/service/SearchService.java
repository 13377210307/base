package com.elasticsearch.service;

import com.elasticsearch.entity.Content;
import com.elasticsearch.entity.Page;

import java.util.List;
import java.util.Map;

public interface SearchService {

    List<Map<String,Object>> accurateSearch(Page page, String keyWord);

    List<Map<String, Object>> highLightSearch(Page page, String keyWord);
}
