package cn.itcast.hmall;

import cn.itcast.hmall.pojo.hmallDoc;
import cn.itcast.hmall.service.IItemService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hmall.common.dto.Item;
import lombok.SneakyThrows;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class hmallTest {

    private RestHighLevelClient client = null;

    @Autowired
    private IItemService service;

    //批量添加文档
    @SneakyThrows
    @Test
    public void addAllDoc() {
        System.out.println("ssss");
//        for (int i1 = 1; i1 <= 1000; i1++) {
//            BulkRequest request = new BulkRequest();
//            Page<Item> page = new Page<>(i1, 5000);
//            Page<Item> page1 = service.page(page);
//            for (Item record : page1.getRecords()) {
//                String JsonHoteDoc = JSON.toJSONString(new hmallDoc(record));
//                IndexRequest request1 = new IndexRequest("hmall");
//                request1.id(record.getId() + "");
//                request1.source(JsonHoteDoc, XContentType.JSON);
//                request.add(request1);
//            }
//            BulkResponse response = client.bulk(request, RequestOptions.DEFAULT);
//            System.out.println(response.status());
//        }
    }


    /**
     * 在单元测试之前执行
     * 创建RestApi对象
     */
    @BeforeEach
    void init() {
        client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("192.168.92.131", 9200, "http")
                ));
        System.out.println("client = " + client);
    }

    /**
     * 在单元测试结束之后执行
     * 关闭RestApi对象
     */
    @SneakyThrows
    @AfterEach
    void destory() {
        if (client != null) {
            client.close();
        }
    }
}
