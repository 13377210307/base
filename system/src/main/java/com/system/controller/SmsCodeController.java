package com.system.controller;

import com.system.service.SmsCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/code")
public class SmsCodeController {

    @Autowired
    private SmsCodeService smsCodeService;

    // 发送短信验证码，并设置过期时间
    @GetMapping("/send")
    public String sendCode(@RequestParam("phone") String phone) {
       return this.smsCodeService.sendCode(phone);
    }

    // 验证验证码
    @GetMapping("/check")
    public String checkCode(@RequestParam("phone") String phone,@RequestParam("code") String code) {
        return this.smsCodeService.checkCode(phone,code);
    }
}
