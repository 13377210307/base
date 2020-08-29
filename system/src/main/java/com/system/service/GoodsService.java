package com.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.model.entity.SysGoods;

import java.util.List;

public interface GoodsService extends IService<SysGoods> {

    List<SysGoods> goods();
}
