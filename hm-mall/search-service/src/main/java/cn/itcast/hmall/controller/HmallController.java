package cn.itcast.hmall.controller;


import cn.itcast.hmall.pojo.RequestParams;
import cn.itcast.hmall.pojo.hmallDoc;
import cn.itcast.hmall.service.IItemService;
import com.hmall.common.dto.PageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("search")
public class HmallController {

    @Autowired
    private IItemService service;

    @PostMapping("/list")
    public PageDTO<hmallDoc> list(@RequestBody RequestParams params) {
        return service.search(params);
    }


    @GetMapping("/suggestion")
    public List<String> suggestion(@RequestParam("key") String key) {
        return service.getkey(key);
    }

    @PostMapping("/filters")
    public Map<String, List<String>> fifters(@RequestBody RequestParams param) {
        return service.fifters(param);
    }
}