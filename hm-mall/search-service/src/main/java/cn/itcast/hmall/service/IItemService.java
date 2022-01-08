package cn.itcast.hmall.service;

import cn.itcast.hmall.pojo.RequestParams;
import cn.itcast.hmall.pojo.hmallDoc;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hmall.common.dto.Item;
import com.hmall.common.dto.PageDTO;

import java.util.List;
import java.util.Map;

public interface IItemService extends IService<Item> {
    PageDTO<hmallDoc> search(RequestParams params);

    List<String> getkey(String key);

    Map<String, List<String>> fifters(RequestParams param);
}
