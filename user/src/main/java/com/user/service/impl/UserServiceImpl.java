package com.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.model.entity.SysUser;
import com.user.mapper.UserMapper;
import com.user.request.PasswordRequest;
import com.user.request.UserStatusRequest;
import com.user.service.UserService;
import com.user.vo.UserInfoVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, SysUser> implements UserService {


    /**
     * 根据用户名查询用户信息
     */
    @Override
    public SysUser loadUserByUsername(String username) {
        return this.baseMapper.loadUserByUsername(username);
    }


    /**
     * 用户分页
     */
    @Override
    public Page<SysUser> userPage(SysUser sysUser, Page<SysUser> page) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(sysUser.getUsername()),"username",sysUser.getUsername());
        return this.baseMapper.selectPage(page,queryWrapper);
    }

    /**
     * 新增用户
     */
    @Override
    public Integer createUser(SysUser sysUser) {
        // 密码加密
        sysUser.setPassword(new BCryptPasswordEncoder().encode(sysUser.getPassword()));
        return this.baseMapper.insert(sysUser);
    }

    @Override
    public Integer updateUser(SysUser sysUser) {
        // 密码加密
        sysUser.setPassword(new BCryptPasswordEncoder().encode(sysUser.getPassword()));
        return this.baseMapper.updateById(sysUser);
    }

    @Override
    public Integer resetPassword(PasswordRequest passwordRequest) {
        // 加密
        String encodePassword = new BCryptPasswordEncoder().encode(passwordRequest.getNewPassword());
        SysUser user = this.getUserByUsername(passwordRequest.getUsername());
        user.setPassword(encodePassword);
        return this.baseMapper.updateById(user);
    }

    @Override
    public Integer updateUserStatus(UserStatusRequest userStatusRequest) {
        SysUser sysUser = this.baseMapper.selectById(userStatusRequest.getUserId());
        if (sysUser == null) {
            return -1;
        }
        sysUser.setStatus(userStatusRequest.getStatus());
        return this.baseMapper.updateById(sysUser);
    }

    @Override
    public UserInfoVo getUserInfo(String username) {
        return this.baseMapper.getUserInfo(username);
    }

    @Override
    public Integer deleteUsers(List<String> ids) {
        return this.baseMapper.deleteBatchIds(ids);
    }

    /**
     * 原密码是否有误
     */
    @Override
    public Boolean truePassword(PasswordRequest passwordRequest) {
        // 加密
        String encodePassword = new BCryptPasswordEncoder().encode(passwordRequest.getOldPassword());
        SysUser user = this.getUserByUsername(passwordRequest.getUsername());
        if (user != null) {
            if (encodePassword.equals(user.getPassword())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否存在相同用户名的用户
     */
    @Override
    public Boolean hasSameUsername(String username) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        return this.baseMapper.selectCount(queryWrapper) > 0;
    }

    /**
     * 是否存在相同手机号
     */
    @Override
    public Boolean hasSamePhone(String phone) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone_number",phone);
        return this.baseMapper.selectCount(queryWrapper) > 0;
    }

    // 根据用户名查询用户
    private SysUser getUserByUsername(String username) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        return this.baseMapper.selectOne(queryWrapper);
    }
}
