package com.hmall.common.client;

import com.hmall.common.filter.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "SPRING-CLOUD-CLIENT-DEMO",configuration = FeignConfiguration.class)
public interface ClientFeign {

    @GetMapping("/demo/get")
    String weight(@RequestParam String param);
}