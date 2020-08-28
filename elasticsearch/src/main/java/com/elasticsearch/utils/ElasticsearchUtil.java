package com.elasticsearch.utils;

import com.alibaba.fastjson.JSON;
import com.elasticsearch.entity.Page;
import com.model.entity.SysUser;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.swing.text.Highlighter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
public class ElasticsearchUtil {

    @Autowired
    private RestHighLevelClient client;



    /**
     * 新增索引
     */
    public void createIndex(String index) throws IOException {

        // 创建请求
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);

        //发送请求
        CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);

        // 输出响应状态
        System.out.println(createIndexResponse);

    }

    /**
     * 判断索引是否存在
     */
    public Boolean indexIsExist(String index) throws IOException {

        // 创建请求
        GetIndexRequest getIndexRequest = new GetIndexRequest(index);

        // 发送请求
        return client.indices().exists(getIndexRequest,RequestOptions.DEFAULT);

    }


    /**
     * 删除索引
     */
    public void deleteIndex(String index) throws IOException {

        // 创建请求
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(index);

        // 添加超时请求
        deleteIndexRequest.timeout("1s");

        //发送请求
        AcknowledgedResponse delete = client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);

        System.out.println(delete.isAcknowledged());
    }


    /**
     * 新增文档
     */
    public void addDocument(String index, SysUser sysUser) throws IOException {

        // 创建请求
        IndexRequest indexRequest = new IndexRequest(index);

        // 添加规则以及数据
        indexRequest.source(JSON.toJSONString(sysUser))
                .timeout("1s");

        // 发送请求
        IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);

        System.out.println(response.status());
    }

    /**
     * 批量新增文档
     */
    public void addBatchDocuments(String index, List<Object> objects) throws IOException {

        // 创建请求
        BulkRequest bulkRequest = new BulkRequest();

        // 遍历数据
        for (int i = 0; i < objects.size(); i++) {
            bulkRequest.add(
                    new IndexRequest(index)
                    .source(JSON.toJSONString(objects.get(i)), XContentType.JSON));
        }

        // 发送请求
        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);

        // 输出响应
        System.out.println(bulkResponse.status());
    }


    /**
     *  修改文档
     */
    public void updateDocument(String index,String id,Object object) throws IOException {

        // 创建请求
        UpdateRequest updateRequest = new UpdateRequest(index, id);

        //将数据放入请求中
        updateRequest.doc(JSON.toJSONString(object),XContentType.JSON);

        // 发送请求
        UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);

        // 输出响应
        System.out.println(updateResponse.status());
    }

    /**
     * 删除文档
     */
    public void deleteDocument(String index,String id) throws IOException {

        // 创建请求
        DeleteRequest deleteRequest = new DeleteRequest(index, id);

        // 发送请求
        DeleteResponse deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);

        //输出响应
        System.out.println(deleteResponse.status());
    }

    /**
     * 获取某文档信息
     */
    public Object documentDetail(String index,String id) throws IOException {

        // 创建请求
        GetRequest getRequest = new GetRequest(index, id);

        //发送请求
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);

        return getResponse.getSourceAsString();
    }

    /**
     * 查询全部
     */
    public SearchHit[] searchAllDocument(String index) throws IOException {

        // 创建请求
        SearchRequest searchRequest = new SearchRequest(index);

        // 构建查询条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.from(1);
        searchSourceBuilder.size(100);

        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();

        searchSourceBuilder.query(matchAllQueryBuilder);

        // 将条件放入请求中
        searchRequest.source(searchSourceBuilder);

        // 发送请求
        return client.search(searchRequest,RequestOptions.DEFAULT).getHits().getHits();

    }

    /**
     * 精准查询
     */
    public SearchHit[] searchAccurateDocument(String index, String searchKey, String searchValue, Page page) throws IOException {

        // 创建请求
        SearchRequest searchRequest = new SearchRequest(index);

        // 构建查询条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // 设置分页
        searchSourceBuilder.from(page.getCurrentPage());
        searchSourceBuilder.size(page.getSize());

        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(searchKey, searchValue);

        searchSourceBuilder.query(termQueryBuilder);

        // 将查询条件放入请求中
        searchRequest.source(searchSourceBuilder);

        // 发送请求
        return client.search(searchRequest, RequestOptions.DEFAULT).getHits().getHits();

    }

    /**
     * 精准查询
     */
    public List<Map<String,Object>> searchHighLight(String index, String searchKey, String searchValue, Page page) throws IOException {

        List<Map<String,Object>> maps = new ArrayList<>();

        // 创建请求
        SearchRequest searchRequest = new SearchRequest(index);

        // 构建查询条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // 设置分页
        searchSourceBuilder.from(page.getCurrentPage());
        searchSourceBuilder.size(page.getSize());

        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(searchKey, searchValue);

        // 高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field(searchKey);
        highlightBuilder.requireFieldMatch(false);// 多个高亮显示
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder);

        searchSourceBuilder.query(termQueryBuilder);

        // 将查询条件放入请求中
        searchRequest.source(searchSourceBuilder);

        // 解析结果
        SearchHit[] hits = client.search(searchRequest, RequestOptions.DEFAULT).getHits().getHits();

        for (SearchHit hit : hits) {
           Map<String, HighlightField> highlightFieldMap = hit.getHighlightFields();

            HighlightField title = highlightFieldMap.get(searchKey);

            Map<String, Object> sourceAsMap = hit.getSourceAsMap(); // 原来的结果
            // 解析高亮字段替换原来结果
            if (title != null) {
                Text[] fragments = title.fragments();
                String n_title = "";

                for (Text text : fragments) {
                    n_title += text;
                }
                sourceAsMap.put(searchKey,n_title);  // 替换
            }

            maps.add(sourceAsMap);
        }

        // 返回结果
        return maps;

    }



}
