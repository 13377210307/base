package com.model.remotes;

import com.model.entity.SysUser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserRemote {

    @PostMapping("/user/loadUserByUsername")
    SysUser loadUserByUsername(@RequestParam("username") String username);
}
