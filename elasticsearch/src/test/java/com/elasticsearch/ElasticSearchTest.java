package com.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.elasticsearch.entity.Content;
import com.elasticsearch.entity.Page;
import com.elasticsearch.utils.ElasticsearchUtil;
import com.elasticsearch.utils.HtmlParseUtil;
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
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ElasticSearchTest {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private ElasticsearchUtil elasticsearchUtil;

    @Autowired
    private HtmlParseUtil htmlParseUtil;


    // 创建索引
    @Test
    public void test1() throws IOException {

        // 创建索引请求
        CreateIndexRequest request = new CreateIndexRequest("wen");

        // 客户端执行请求，执行后获得响应
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);

        System.out.println(createIndexResponse);
    }

    // 获取索引，索引相当于一个数据库只能判断它存不存在
    @Test
    public void test2() throws IOException {

        // 创建获取索引请求
        GetIndexRequest request = new GetIndexRequest("wen");

        boolean exists = restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);

        System.out.println(exists);

    }

    // 删除索引
    @Test
    public void test3() throws IOException {

        // 创建获取索引
        DeleteIndexRequest request = new DeleteIndexRequest("wen");

        AcknowledgedResponse delete = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);

        System.out.println(delete.isAcknowledged());
    }


    // 添加文档
    @Test
    public void testAddDocument() throws IOException {
        // 创建对象
        SysUser sysUser = new SysUser();
        sysUser.setUsername("admin");
        sysUser.setId("1");
        sysUser.setPhoneNumber("13377210458");
        sysUser.setStatus(true);
        sysUser.setPassword("123456");

        // 创建请求
        IndexRequest request = new IndexRequest("wen");

        // 规则
        request.id("1");


        // 将数据放入请求 json
        request.source(JSON.toJSONString(sysUser), XContentType.JSON);

        // 客户端发送请求
        IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);

        System.out.println(response.toString());
        System.out.println(response.status());

    }

    // 获取文档是否存在
    @Test
    public void testIsExist() throws IOException {
        // 创建请求
        GetRequest request = new GetRequest("wen", "1");

        // 不获取返回的_source的上下文
        request.fetchSourceContext(new FetchSourceContext(false));
        request.storedFields("_none_");

        boolean exists = restHighLevelClient.exists(request, RequestOptions.DEFAULT);

        System.out.println(exists);

    }

    // 获取文档信息
    @Test
    public void getDocument() throws IOException {
        GetRequest getRequest = new GetRequest("wen","1");

        GetResponse response = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);

        System.out.println(response.getSourceAsString()); // 打印文档
    }

    // 更新文档信息
    @Test
    public void updateDocument() throws IOException {

        // 获取文档
//        GetRequest getRequest = new GetRequest("wen", "1");
//        GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
//        // 将JSON转成SysUser
//        Map<String, Object> source = getResponse.getSource();
        UpdateRequest updateRequest = new UpdateRequest("wen","1");

        SysUser sysUser = new SysUser();
        sysUser.setUsername("test");
        sysUser.setId("2");
        sysUser.setPhoneNumber("13377210458");
        sysUser.setStatus(true);
        sysUser.setPassword("123456789");

        // 将对象添加到请求中
        updateRequest.doc(JSON.toJSONString(sysUser), XContentType.JSON);

        UpdateResponse response = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);

        System.out.println(response.status());
    }

    // 删除文档
    @Test
    public void deleteDocument() throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest("wen", "1");
        deleteRequest.timeout("1s");
        DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println(deleteResponse.status());
    }

    // 批量插入
    @Test
    public void moreCreate() throws IOException {
        // 创建请求
        BulkRequest bulkRequest = new BulkRequest();

        //设置超时时间
        bulkRequest.timeout("10s");

        // 设置插入对象集合
        //List<SysUser> users = this.createUser();

        // 遍历用户集合
        /*for (int i = 0; i < users.size(); i++) {
            bulkRequest.add(
                    new IndexRequest("wen")
                    .source(JSON.toJSONString(users.get(i)),XContentType.JSON));
        }*/

        // 批量插入
        BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);

        System.out.println(bulkResponse.status());

    }

    // 查询
    @Test
    public void testQuery() throws IOException {
        // 创建查询请求
        SearchRequest searchRequest = new SearchRequest("wen");

        // 构建搜索条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // termQuery 精确匹配
        //TermQueryBuilder queryBuilder = QueryBuilders.termQuery("username", "zs");
        MatchAllQueryBuilder queryBuilder = QueryBuilders.matchAllQuery();

        SearchSourceBuilder query = searchSourceBuilder.query(queryBuilder);

        query.timeout(new TimeValue(60, TimeUnit.SECONDS));

        searchRequest.source(query);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        SearchHit[] hits = searchResponse.getHits().getHits();

        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsMap());
        }

    }


    @Test
    public void test10() throws Exception {

        // 批量导入数据
        /*List<Object> javas = this.htmlParseUtil.parseData("手机");

        this.elasticsearchUtil.addBatchDocuments("jd_goods",javas);*/

        // 精确查询
        Page page = new Page();
        page.setSize(50);
        page.setCurrentPage(1);
        SearchHit[] searchHits = this.elasticsearchUtil.searchAccurateDocument("jd_goods", "title", "java",page);
        //SearchHit[] searchHits = this.elasticsearchUtil.searchAllDocument("jd_goods");
        if (searchHits.length > 0) {
            for (int i = 0; i < searchHits.length; i++) {
                System.out.println(searchHits[i].getSourceAsMap());
            }
        }

    }

    // 创建对象集合
    private List<Object> createUser() {
        ArrayList<Object> sysUsers = new ArrayList<>();
        SysUser sysUser = new SysUser();
        sysUser.setId("3");
        sysUser.setUsername("zs");
        sysUser.setPassword("123456");
        sysUser.setStatus(true);
        sysUser.setPhoneNumber("15079750332");
        sysUsers.add(sysUser);

        SysUser sysUser1 = new SysUser();
        sysUser1.setId("4");
        sysUser1.setUsername("ls");
        sysUser1.setPassword("123456");
        sysUser1.setStatus(true);
        sysUser1.setPhoneNumber("15079750332");
        sysUsers.add(sysUser1);

        SysUser sysUser2 = new SysUser();
        sysUser2.setId("5");
        sysUser2.setUsername("ww");
        sysUser2.setPassword("123456");
        sysUser2.setStatus(true);
        sysUser2.setPhoneNumber("15079750332");
        sysUsers.add(sysUser2);

        SysUser sysUser3 = new SysUser();
        sysUser3.setId("6");
        sysUser3.setUsername("ll");
        sysUser3.setPassword("123456");
        sysUser3.setStatus(true);
        sysUser3.setPhoneNumber("15079750332");
        sysUsers.add(sysUser3);

        SysUser sysUser4 = new SysUser();
        sysUser4.setId("7");
        sysUser4.setUsername("user");
        sysUser4.setPassword("123456");
        sysUser4.setStatus(true);
        sysUser4.setPhoneNumber("15079750332");
        sysUsers.add(sysUser4);

        return sysUsers;
    }
}
