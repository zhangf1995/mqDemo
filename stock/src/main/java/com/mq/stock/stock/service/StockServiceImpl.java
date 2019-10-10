package com.mq.stock.stock.service;

import com.mq.stock.stock.entity.Stock;
import com.mq.stock.stock.mapper.StockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @BelongsProject: mqDemo
 * @Author: zf
 * @CreateTime: 2019-10-09 17:26
 * @Description:
 */
@Service
public class StockServiceImpl implements StockService{

    @Autowired
    private StockMapper mapper;

    @Override
    public void saveStock() {
        Stock stock = new Stock();
        stock.setId(1);
        stock.setStockNum(new BigDecimal(10));
        mapper.insert(stock);
    }
}