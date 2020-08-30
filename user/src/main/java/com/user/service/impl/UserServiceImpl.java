package com.user.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.model.entity.SysUser;
import com.user.mapper.UserMapper;
import com.user.request.PasswordRequest;
import com.user.request.UserStatusRequest;
import com.user.service.UserService;
import com.user.utils.RedisUtil;
import com.user.vo.UserInfoVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, SysUser> implements UserService {

    @Autowired
    private RedisUtil redisUtil;


    /**
     * 根据用户名查询用户信息
     */
    @Override
    public SysUser loadUserByUsername(String username) {
        SysUser sysUser = this.baseMapper.loadUserByUsername(username);
        if (sysUser == null) {
            return null;
        }
        // 日期转换
        String today= DateUtil.today();
        String loginUserKey = "login:user:"+today;
        String loginCountKey = "login:count:"+today;
        // 判断redis中是否有用户登录记录；使用set，用户id作为值
        boolean loginUserResult = this.redisUtil.sHasKey(loginUserKey,sysUser.getId());
        // 今日登录统计数
        boolean loginCountResult = this.redisUtil.hasKey(loginCountKey);

        if (!loginUserResult) {
            // 今日登录统计数是否存在
            if (!loginCountResult) {
                // 设置值为0
                this.redisUtil.set(loginCountKey,0);
            } else {
                // 当前用户今日未登录
                // 记录用户
                this.redisUtil.sSet(loginUserKey,sysUser.getId());
                // 增加统计数
                this.redisUtil.incr(loginCountKey,1);
            }

        }
        return sysUser;
    }

    @Override
    public UserInfoVo getUserInfo(String username) {
        return this.baseMapper.getUserInfo(username);
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
