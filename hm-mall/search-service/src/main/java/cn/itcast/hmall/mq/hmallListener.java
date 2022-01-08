package cn.itcast.hmall.mq;

import cn.itcast.hmall.constants.MqConstants;
import cn.itcast.hmall.service.impl.ItemService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class hmallListener {
    @Autowired
    private ItemService service;

    @RabbitListener(queues = MqConstants.HMALL_INSERT_QUEUE)
    public void listenerSaveOrUpdate(Long id) {
        service.insetInes(id);
    }

    @RabbitListener(queues = MqConstants.HMALL_DELETE_QUEUE)
    public void listenerDelete(Long id) {
        service.delInes(id);
    }
}
