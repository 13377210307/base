package com.elasticsearch.utils;


import com.elasticsearch.entity.Content;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 解析网页工具类
 */
@Configuration
public class HtmlParseUtil {

    private static String url = "https://search.jd.com/Search?keyword=";

    public List<Object> parseData(String keyword) throws Exception {

        ArrayList<Object> contents = new ArrayList<>();

        String newUrl = url+keyword;

        // 解析网页：返回浏览器document对象
        Document document = Jsoup.parse(new URL(newUrl), 30000);

        Element element = document.getElementById("J_goodsList");

        // 获取元素中的li
        Elements liElements = element.getElementsByTag("li");

        // 获取元素中的内容
        for (Element el: liElements) {
            String img = el.getElementsByTag("img").eq(0).attr("src");
            //String img = el.getElementsByTag("img").eq(0).attr("source-data-lazy-img");
            String price = el.getElementsByClass("p-price").eq(0).text();
            String title = el.getElementsByClass("p-name").eq(0).text();

            System.out.println(img);
            System.out.println(title);
            System.out.println(price);

            Content content = new Content();
            content.setImg(img);
            content.setTitle(title);
            content.setPrice(price);

            contents.add(content);
        }

        return contents;

    }
}
