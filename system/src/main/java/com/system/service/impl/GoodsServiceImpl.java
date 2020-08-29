package com.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.model.entity.SysGoods;
import com.system.mapper.GoodsMapper;
import com.system.service.GoodsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, SysGoods> implements GoodsService {

    @Override
    public List<SysGoods> goods() {
        QueryWrapper<SysGoods> queryWrapper = new QueryWrapper<>();
        return this.baseMapper.selectList(queryWrapper);
    }
}
