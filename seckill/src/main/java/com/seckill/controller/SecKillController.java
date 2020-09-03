package com.seckill.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.model.entity.SeckillGoods;
import com.seckill.service.SecKillGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/skill")
public class SecKillController {

    @Autowired
    private SecKillGoodsService secKillGoodsService;

    /**
     * 新增秒杀商品
     */
    @PostMapping("/goods")
    public R creategoods(@RequestBody SeckillGoods seckillGoods) {
        return R.ok(this.secKillGoodsService.create(seckillGoods));
    }
}
