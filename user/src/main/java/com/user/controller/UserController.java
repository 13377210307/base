package com.user.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.model.entity.SysUser;
import com.user.request.PasswordRequest;
import com.user.request.UserStatusRequest;
import com.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // 根据用户名查询用户信息
    @PostMapping("/loadUserByUsername")
    public SysUser loadUserByUsername(@RequestParam("username") String username) {
        return this.userService.loadUserByUsername(username);
    }

    /**
     * 获取用户信息
     *
     */
    @GetMapping("/info")
    public R getUserInfo(Authentication authentication) {
        return R.ok(this.userService.getUserInfo(String.valueOf(authentication.getPrincipal())));
    }

    // 用户分页
    @GetMapping("/page")
    public R userPage(SysUser sysUser, Page<SysUser> page) {
        return R.ok(this.userService.userPage(sysUser,page));
    }

    // 新增用户
    @PostMapping
    public R createUser(@RequestBody SysUser sysUser) {

        // 判断用户名是否存在相同
        Boolean nameResult = this.userService.hasSameUsername(sysUser.getUsername());
        if (nameResult) {
            return R.failed("存在相同用户名，新增失败");
        }

        // 判断手机号是否重复
        Boolean phoneResult = this.userService.hasSamePhone(sysUser.getPhoneNumber());
        if (phoneResult) {
            return R.failed("存在相同手机号，新增失败");
        }
        return R.ok(this.userService.createUser(sysUser));
    }

    // 修改用户
    @PutMapping
    public R updateUser(@RequestBody SysUser sysUser) {
        // 判断用户名是否存在相同
        Boolean nameResult = this.userService.hasSameUsername(sysUser.getUsername());
        if (nameResult) {
            return R.failed("存在相同用户名，修改失败");
        }

        // 判断手机号是否重复
        Boolean phoneResult = this.userService.hasSamePhone(sysUser.getPhoneNumber());
        if (phoneResult) {
            return R.failed("存在相同手机号，修改失败");
        }
        return R.ok(this.userService.updateUser(sysUser));
    }

    //禁用或启用用户
    @PutMapping("/updateStatus")
    public R updateUserStatus(@RequestBody UserStatusRequest userStatusRequest) {
        return R.ok(this.userService.updateUserStatus(userStatusRequest));
    }

    // 重置密码
    @PutMapping("/resetPassword")
    public R resetPassword(@RequestBody PasswordRequest passwordRequest) {

        // 判断原密码是否有误
        Boolean result = this.userService.truePassword(passwordRequest);

        if (!result) {
            return R.failed("原密码有误");
        }

        // 重置密码
        return R.ok(this.userService.resetPassword(passwordRequest));
    }

    // 批量删除用户
    @DeleteMapping
    public R deleteUsers(@RequestParam("ids") List<String> ids){
        return R.ok(this.userService.deleteUsers(ids));
    }






}
