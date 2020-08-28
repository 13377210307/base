package com.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.model.entity.SysUser;
import com.user.request.PasswordRequest;
import com.user.request.UserStatusRequest;
import com.user.vo.UserInfoVo;

import java.util.List;

public interface UserService extends IService<SysUser> {

    // 根据用户名查询用户信息
    SysUser loadUserByUsername(String username);

    // 用户分页
    Page<SysUser> userPage(SysUser sysUser, Page<SysUser> page);

    // 是否存在相同用户名的用户
    Boolean hasSameUsername(String username);

    // 新增用户
    Integer createUser(SysUser sysUser);

    //是否存在相同手机号
    Boolean hasSamePhone(String phoneNumber);

    // 修改用户
    Integer updateUser(SysUser sysUser);

    // 判断原密码是否有误
    Boolean truePassword(PasswordRequest passwordRequest);

    // 重置密码
    Integer resetPassword(PasswordRequest passwordRequest);

    // 禁用获启用用户
    Integer updateUserStatus(UserStatusRequest userStatusRequest);

    // 获取用户信息
    UserInfoVo getUserInfo(String username);

    // 批量删除用户
    Integer deleteUsers(List<String> ids);
}
