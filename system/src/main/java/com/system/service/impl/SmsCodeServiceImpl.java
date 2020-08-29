package com.system.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.system.service.SmsCodeService;
import com.system.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class SmsCodeServiceImpl implements SmsCodeService {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 发送验证码，设置过期时间
     */
    @Override
    public String sendCode(String phone) {
        // 随机生成验证码
        String code = RandomUtil.randomString(4);

        // 将手机号与验证码保存在redis中
        this.redisUtil.set("code_"+phone,code,60);

        return code;
    }

    /**
     * 验证验证码
     * 1：手机号不存在
     * 2：已过期
     */
    @Override
    public String checkCode(String phone,String code) {

        String key = "code_" + phone;

        if (!StringUtils.isEmpty(code)) {
            // 手机未发送验证码或验证码过期
            boolean isExist = this.redisUtil.hasKey(key);

            // 验证码错误
            String realCode = (String) this.redisUtil.get(key);

            if (!code.equals(realCode)) {
                return "验证码有误";
            } else if (!isExist) {
                return "验证码已过期";
            } else {
                return "验证成功";
            }
        }else {
            return "验证码不能为空";
        }
    }


}
