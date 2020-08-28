package com.oauth.service;

import com.model.entity.SysRole;
import com.model.entity.SysUser;
import com.oauth.clients.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.stream.Collectors;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserClient userClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        SysUser sysUser = this.userClient.loadUserByUsername(username);

        if (sysUser == null) {
            return null;
        }

        // 获取用户角色
        String roles = "";
        if (!CollectionUtils.isEmpty(sysUser.getRoles())) {
            roles = sysUser.getRoles().stream().map(SysRole::getKey).collect(Collectors.joining(","));
        }
        return new User(username,sysUser.getPassword(),true,true,true,true,
                AuthorityUtils.commaSeparatedStringToAuthorityList(roles));
    }
}
