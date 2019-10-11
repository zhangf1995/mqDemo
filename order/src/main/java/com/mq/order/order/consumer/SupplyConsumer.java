package com.mq.order.order.consumer;

import com.alibaba.fastjson.JSONObject;
import com.mq.order.order.entity.Order;
import com.mq.order.order.mapper.OrderMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @BelongsProject: mqDemo
 * @Author: zf
 * @CreateTime: 2019-10-11 15:30
 * @Description: 补单消费者
 */
@Component
public class SupplyConsumer {

    @Autowired
    private OrderMapper mapper;

    @RabbitListener(queues = "supplyQueue")
    public void process(Message message) throws Exception{
        String msg = new String(message.getBody(), "utf-8");
        JSONObject json = JSONObject.parseObject(msg);
        String orderId = json.getString("orderId");
        Order order = mapper.selectById(orderId);
        if(null == order){
            //重新补单，插入一条新的order
        }
    }
}