package com.user.request;

import lombok.Data;

/**
 * 密码重置参数
 */
@Data
public class PasswordRequest {

    private String username;

    private String oldPassword;

    private String newPassword;
}
