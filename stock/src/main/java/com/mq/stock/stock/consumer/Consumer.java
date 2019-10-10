package com.mq.stock.stock.consumer;

import com.mq.stock.stock.entity.Stock;
import com.mq.stock.stock.mapper.StockMapper;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * @BelongsProject: mqDemo
 * @Author: zf
 * @CreateTime: 2019-10-09 17:41
 * @Description:
 */
@Component
public class Consumer {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StockMapper stockMapper;

    @RabbitListener(queues = "queue")
    public void process(Message message, Channel channel) throws Exception {
        String messageId = message.getMessageProperties().getMessageId();
        String msg = new String(message.getBody(), "utf-8");
        if(StringUtils.isEmpty(messageId)){
            //日志采集
            return;
        }
        //重试时幂等性问题
        if(!redisTemplate.hasKey(messageId)){
            Stock stock = new Stock();
            stock.setId(1);
            stock.setStockNum(new BigDecimal(1));
            System.out.println("进来了");

            try{
                stockMapper.insert(stock);
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
                //存入redis
                redisTemplate.opsForValue().set(messageId,true);
            }catch (Exception e){
                //丢弃消息
                channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,false);
            }
        }
    }
}