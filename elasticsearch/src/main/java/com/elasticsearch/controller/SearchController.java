package com.elasticsearch.controller;

import com.elasticsearch.entity.Content;
import com.elasticsearch.entity.Page;
import com.elasticsearch.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;


    /**
     * 精准搜索
     */
    @GetMapping("/accurate/{keyWord}")
    public List<Map<String,Object>> accurateSearch(@PathVariable("keyWord") String keyWord) {
        Page page = new Page();
        page.setCurrentPage(1);
        page.setSize(50);
        return this.searchService.accurateSearch(page,keyWord);
    }

    /**
     * 高亮搜索
     */
    @GetMapping("/highLight/{keyWord}")
    public List<Map<String,Object>> highLightSearch(@PathVariable("keyWord") String keyWord) {
        Page page = new Page();
        page.setCurrentPage(1);
        page.setSize(50);
        return this.searchService.highLightSearch(page,keyWord);
    }
}
