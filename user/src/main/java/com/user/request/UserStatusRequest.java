package com.user.request;

import lombok.Data;

/**
 * 用户状态
 */
@Data
public class UserStatusRequest {

    private String userId;

    private Boolean status;
}
