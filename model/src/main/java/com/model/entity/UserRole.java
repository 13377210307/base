package com.model.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRole implements Serializable {

    private String userId;

    private String roleId;
}
