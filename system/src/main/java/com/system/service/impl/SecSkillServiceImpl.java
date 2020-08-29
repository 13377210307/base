package com.system.service.impl;

import com.system.service.SecSkillService;
import com.system.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecSkillServiceImpl implements SecSkillService {

    @Autowired
    private RedisUtil redisUtil;


    @Override
    public String secSkill(String userId, String productId) {

        // 定义库存数量
        String count = "skill:"+productId+":count:";

        // 定义已抢购用户
        String user = "skill:"+productId+":user:";

        // 秒杀未开始
        boolean result = this.redisUtil.hasKey(count);
        if (!result) {
            return "活动未开始，请稍候";
        }

        //已抢购成功，不能重复抢购
        boolean succeccUserResult = this.redisUtil.sHasKey(user, userId);

        if (succeccUserResult) {
            return "您已抢购成功，不能重复抢购";
        }

        // 库存问题
        Integer number = (Integer)this.redisUtil.get(count);
        if (number <= 0) {
            return "很遗憾，该商品已抢光";
        }

        // 否则，减库存，加人
        this.redisUtil.decr(count,1);

        this.redisUtil.sSet(user,userId);

        return "恭喜您，抢购成功";
    }


    // 设置库存
    @Override
    public String setCount(String productId) {
        // 定义库存数量
        String count = "skill:"+productId+":count:";

        this.redisUtil.set(count,100);
        return "新增商品成功";
    }
}
