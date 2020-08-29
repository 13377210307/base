package com.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class SysGoods {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String name;

    private String price;

    private String description;

    private String type;

    private String number;

    private String unit;

    private String size;
}
