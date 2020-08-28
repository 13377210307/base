package com.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.model.entity.SysUser;
import com.user.vo.UserInfoVo;
import org.apache.ibatis.annotations.Param;

public interface UserMapper extends BaseMapper<SysUser> {

    SysUser loadUserByUsername(@Param("username") String username);

    UserInfoVo getUserInfo(@Param("username") String username);
}
