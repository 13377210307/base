package com.elasticsearch.entity;

import lombok.Data;

@Data
public class Page {

    private Integer currentPage = 1;

    private Integer size = 30;

}
