package com.system.controller;

import com.system.service.SecSkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/secSkill")
public class SecSkillController {

    @Autowired
    private SecSkillService secSkillService;

    @GetMapping
    public String secSkill(@RequestParam("userId") String userId,@RequestParam("productId") String productId) {
        return this.secSkillService.secSkill(userId,productId);
    }

    @PostMapping("/setCount")
    public String setcount(@RequestParam("productId") String productId) {
        return this.secSkillService.setCount(productId);
    }
}
