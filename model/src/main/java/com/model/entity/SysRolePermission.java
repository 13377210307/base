package com.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lwg
 * @since 2020-07-11 15:53:54
 */
@Data
public class SysRolePermission implements Serializable{

    private String roleId;

    private String permissionId;
}
