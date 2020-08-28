package com.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lwg
 * @since 2020-07-10 11:33:36
 */
@Data
public class SysPermission implements Serializable{

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String pid;

    private String name;

    private String permissionKey;

    private Integer type;

    private Integer level;

    private Integer sort;

    private Integer status;

    @TableField(exist = false)
    private List<SysPermission> children=new ArrayList<>();
}
