package com.hmall.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hmall.common.dto.Item;

public interface IItemService extends IService<Item> {
    void updateStatus(Long id, Integer status);
}
