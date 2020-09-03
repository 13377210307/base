package com.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author makejava
 * @since 2020-09-03 16:57:10
 */
@Data
public class SeckillOrder implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    //秒杀商品id
    private String skillId;
    //支付金额
    private Object money;
    //用户id
    private String userId;
    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    //支付时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;
    //支付状态：0：未  1：支付
    private Object payStatus;
    //收货人地址
    private String receiverAddress;
    //收货人电话
    private String receiverMobile;
    //收货人
    private String receiver;
}
