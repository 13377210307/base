package com.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.model.entity.SeckillGoods;
import com.seckill.mapper.SecKillGoodsMapper;
import com.seckill.service.SecKillGoodsService;
import org.springframework.stereotype.Service;

@Service
public class SecKillGoodsServiceImpl extends ServiceImpl<SecKillGoodsMapper, SeckillGoods> implements SecKillGoodsService {

    /**
     * 新增秒杀商品
     *
     * 1：数据库新增数据
     * 2：将数据存到redis中
     */
    @Override
    public Integer create(SeckillGoods seckillGoods) {
        return null;
    }
}
