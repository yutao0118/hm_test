package cn.itcast.hmall;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@MapperScan("cn.itcast.hmall.mapper")
@SpringBootApplication
public class HmallDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(HmallDemoApplication.class, args);
    }


    @Bean
    public RestHighLevelClient client(){
        return  new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://192.168.92.131:9200")
        ));
    }
}

