package com.hmall.item.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmall.common.dto.Item;
import com.hmall.item.mapper.ItemMapper;
import com.hmall.item.service.IItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemService extends ServiceImpl<ItemMapper, Item> implements IItemService {

    @Transactional
    @Override
    public void updateStatus(Long id, Integer status){
        /*Item item = new Item();
        item.setId(id);
        item.setStatus(status);
        this.getBaseMapper().updateById(item);
        */

        // update tb_item set status = ? where id = ?
        this.update().set("status", status).eq("id", id).update();
    }

}
