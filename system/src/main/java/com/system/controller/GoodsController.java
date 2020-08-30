package com.system.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.baomidou.mybatisplus.extension.api.R;
import com.model.entity.SysGoods;
import com.system.listener.FileListener;
import com.system.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    // 导入
    @PostMapping("/import")
    public R importFile(MultipartFile file) throws IOException {
        FileListener fileListener = new FileListener(goodsService);
        if (file == null) {
            return R.failed("导入文件为空");
        }

        EasyExcel.read(file.getInputStream(),SysGoods.class,fileListener).sheet().headRowNumber(4).doRead();
        return R.ok("导入成功");
    }

    // 导出
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        List<SysGoods> goods = this.goodsService.goods();
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("excel/goods.xlsx");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(resourceAsStream).build();
        WriteSheet writeSheet = EasyExcel.writerSheet().build();
        FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
        excelWriter.fill(goods, fillConfig, writeSheet);
        Map<String, Object> map = new HashMap<String, Object>();
        excelWriter.fill(map, writeSheet);
        excelWriter.finish();
    }
}
