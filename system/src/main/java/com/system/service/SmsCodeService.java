package com.system.service;

public interface SmsCodeService {


    String sendCode(String phone);

    String checkCode(String phone,String code);

    String codeCout(String phone,String code);
}
