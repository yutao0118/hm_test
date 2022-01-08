package com.hmall.item.constants;

public class MqConstants {
    /**
     * 交换机
     */
    public final static String HMALL_EXCHANGE = "hmall.topic";

    /**
     * 监听新增和修改的队列
     */
    public final static String HMALL_INSERT_QUEUE = "hmall.insert.queue";

    /**
     * 监听删除的队列
     */
    public final static String HMALL_DELETE_QUEUE = "hmall.delete.queue";

    /**
     * 新增或者修改的Routingkey
     */
    public final static String HMALL_INSERT_KEY = "hmall.insert";

    /**
     * 删除的Routingkey
     */
    public final static String HMALL_DELETE_KEY = "hmall.delete";
}
