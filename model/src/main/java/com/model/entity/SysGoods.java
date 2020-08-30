package com.model.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class SysGoods {

    @TableId(type = IdType.ASSIGN_ID)
    @ExcelIgnore
    private String id;

    @ExcelProperty(value = "商品名称",index = 0)
    private String name;

    @ExcelProperty(value = "商品价格",index = 1)
    private String price;

    @ExcelProperty(value = "商品数量",index = 2)
    private String number;

    @ExcelIgnore
    private String unit;

    @ExcelProperty(value = "商品类型",index = 3)
    private String type;

    @ExcelProperty(value = "商品尺寸",index = 4)
    private String size;

    @ExcelProperty(value = "商品描述",index = 5)
    private String description;
}
