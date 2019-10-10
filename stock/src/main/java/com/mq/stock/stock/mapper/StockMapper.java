package com.mq.stock.stock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mq.stock.stock.entity.Stock;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StockMapper extends BaseMapper<Stock> {

}
