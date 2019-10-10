package com.mq.order.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @BelongsProject: lcnDemo
 * @Author: zf
 * @CreateTime: 2019-09-27 11:08
 * @Description: order
 */
@Data
@TableName("lcn_order")
public class Order {
    private Integer id;
    private String orderName;
}