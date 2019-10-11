package com.mq.order.order.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @BelongsProject: mqDemo
 * @Author: zf
 * @CreateTime: 2019-10-08 17:05
 * @Description: rabbitmq配置
 */
@Component
public class RabbitMqConfig {
    public static final String queue = "queue";
    public static final String d1Exchange = "d1Exchange";
    public static final String bindkey = "bindkey";
    public static final String supplyQueue = "supplyQueue";
    public static final String supplyBindKey = "supplyBindKey";

    @Bean
    public DirectExchange getDirectExchange(){
        return new DirectExchange(d1Exchange);
    }

    @Bean
    public Queue getQueueOne(){
        return new Queue(queue);
    }


    @Bean
    public Binding bindQueueOne(){
        return BindingBuilder.bind(getQueueOne()).to(getDirectExchange()).with(bindkey);
    }

    @Bean
    public Queue getSupplyQueue(){
        return new Queue(supplyQueue);
    }

    @Bean
    public Binding BindSupplyQueue(){
        return BindingBuilder.bind(getSupplyQueue()).to(getDirectExchange()).with(supplyBindKey);
    }
}