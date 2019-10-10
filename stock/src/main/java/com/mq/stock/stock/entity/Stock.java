package com.mq.stock.stock.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @BelongsProject: lcnDemo
 * @Author: zf
 * @CreateTime: 2019-09-27 11:08
 * @Description: stock
 */
@Data
@TableName("lcn_stock")
public class Stock {
    private Integer id;
    private BigDecimal stockNum;
}