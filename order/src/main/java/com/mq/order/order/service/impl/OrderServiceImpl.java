package com.mq.order.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mq.order.order.config.RabbitMqConfig;
import com.mq.order.order.entity.Order;
import com.mq.order.order.mapper.OrderMapper;
import com.mq.order.order.service.OrderService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * @BelongsProject: mqDemo
 * @Author: zf
 * @CreateTime: 2019-10-09 11:29
 * @Description: impl
 */
@Service
public class OrderServiceImpl implements OrderService, RabbitTemplate.ConfirmCallback {

    @Autowired
    private OrderMapper mapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addOrder() {
        Order order = new Order();
        order.setId(2);
        order.setOrderName("test");
        mapper.insert(order);
        send(2);
        try {
            int i = 1 / 0;
        } catch (Exception e) {
            //补单
            supplySend(order);
        }
    }

    /**
     * 补单发送
     * @param order
     */
    private void supplySend(Order order) {
        JSONObject json = new JSONObject();
        json.put("order",order);
        Message message = MessageBuilder.withBody(json.toJSONString().getBytes()).setContentType(MessageProperties.CONTENT_TYPE_JSON).setContentEncoding("utf-8").setMessageId(String.valueOf(order.getId())).build();
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback(this::confirm);
        rabbitTemplate.convertAndSend(RabbitMqConfig.d1Exchange,RabbitMqConfig.supplyBindKey,message);
    }

    private void send(int orderId) {
        JSONObject json = new JSONObject();
        json.put("orderId", orderId);
        String jsonStr = json.toJSONString();
        Message message = MessageBuilder.withBody(jsonStr.getBytes()).setContentType(MessageProperties.CONTENT_TYPE_JSON).setContentEncoding("utf-8").setMessageId(String.valueOf(orderId)).build();
        //回调返回数据
        CorrelationData correlationData = new CorrelationData(String.valueOf(orderId));
        //确认消息
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback(this::confirm);
        rabbitTemplate.convertAndSend(RabbitMqConfig.d1Exchange, RabbitMqConfig.bindkey, message, correlationData);
    }




    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String error) {
        String id = correlationData.getId();
        System.out.println("消息id:" + id);
        if (ack) {
            System.out.println("消息发送成功");
        } else {
            System.out.println("消息发送失败，正在重新发送消息");
            send(Integer.valueOf(id));
        }
    }
}