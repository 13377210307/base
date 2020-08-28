package com.user.utils;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;

@Configuration
public class UserUtil {

    public Object getUserByAuthen(Authentication authentication) {
        return authentication.getPrincipal();
    }
}
