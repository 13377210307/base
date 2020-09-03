package com.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.model.entity.SeckillGoods;

public interface SecKillGoodsService extends IService<SeckillGoods> {

    // 新增秒杀商品
    Integer create(SeckillGoods seckillGoods);
}
