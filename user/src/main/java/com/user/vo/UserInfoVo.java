package com.user.vo;

import com.model.entity.SysPermission;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserInfoVo implements Serializable {

    private String username;

    private String phoneNumber;

    private List<SysPermission> permissionList=new ArrayList<>();
}
