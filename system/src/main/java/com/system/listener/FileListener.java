package com.system.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.model.entity.SysGoods;
import com.system.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class FileListener extends AnalysisEventListener<SysGoods> {

    @Autowired
    private GoodsService goodsService;

    private List<SysGoods> goods = new ArrayList<>();

    public FileListener(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    /**
     * 每解析到一条数据进行一次调用
     */
    @Override
    public void invoke(SysGoods sysGoods, AnalysisContext analysisContext) {

        goods.add(sysGoods);
    }

    /**
     * 解析完成之后的回调
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        boolean result = this.goodsService.saveBatch(goods);
        if (result) {
            System.out.println("导入成功");
        }else {
            System.out.println("导入失败");
        }
    }
}
