package com.hmall.item.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hmall.common.dto.Item;
import com.hmall.common.dto.PageDTO;
import com.hmall.item.constants.MqConstants;
import com.hmall.item.service.IItemService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("item")
public class ItemController {

    @Autowired
    private IItemService itemService;
    @Autowired
    private RabbitTemplate rabbitTemplate;


    @GetMapping("hi")
    public String hi() {
        return "hi";
    }

    @GetMapping("/list")
    public PageDTO<Item> queryItemByPage(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        // 分页查询
        Page<Item> page1 = new Page<>(page, size);
        Page<Item> page2 = itemService.page(page1);
        return new PageDTO<>(page2.getTotal(), page2.getRecords());
    }

    @GetMapping("{id}")
    public Item queryItemById(@PathVariable("id") Long id) {
        return itemService.getById(id);
    }

    /**
     * 在mybatis-plus中，查询只需要以下几个方法：
     *  - 根据id查询一个 getById(id)
     *  - 根据id查询多个 listByIds(List<Id>)
     *  - 根据复杂条件查询一个、多个、分页
     *      - 1个：query().eq().ge().or().le().one();
     *      - 多个：query().eq().ge().or().le().list();
     *      - 分页：query().eq().ge().or().le().page(new Page(1, 3));
     *
     */
    @GetMapping("/query")
    public List<Item> queryItemById(
            @RequestParam("minPrice") Integer minPrice,
            @RequestParam("maxPrice") Integer maxPrice,
            @RequestParam("category") String category
    ) {
        return null;
    }

    @PostMapping
    public void saveItem(@RequestBody Item item) {
        itemService.save(item);
    }

    @PutMapping("/status/{id}/{status}")
    public void updateItemStatus(@PathVariable("id") Long id, @PathVariable("status") Integer status){
        Item item = itemService.getById(id);
        item.setStatus(status);
        itemService.updateById(item);
        rabbitTemplate.convertAndSend(MqConstants.HMALL_EXCHANGE, MqConstants.HMALL_INSERT_KEY, id);
    }

    @PutMapping
    public void updateItem(@RequestBody Item item) {
        itemService.updateById(item);
        rabbitTemplate.convertAndSend(MqConstants.HMALL_EXCHANGE, MqConstants.HMALL_INSERT_KEY, item.getId());

    }

    @DeleteMapping("{id}")
    public void deleteItemById(@PathVariable("id") Long id) {
        itemService.removeById(id);
        rabbitTemplate.convertAndSend(MqConstants.HMALL_EXCHANGE, MqConstants.HMALL_DELETE_KEY, id);
    }
}
