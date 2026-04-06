package org.jeecg.modules.yy.place.controller;

import org.jeecg.config.shiro.IgnoreAuth;
import org.jeecg.modules.yy.place.service.impl.TestSendGridServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class TestSendGridController {

    @Resource
    private TestSendGridServiceImpl mailService;

    @GetMapping("/test/mail")
    @IgnoreAuth
    public String testMail() {

        mailService.sendMail(
                "jiang1980jp@gmail.com",   // 👉 换成你自己的邮箱
                "EasyRide 测试邮件",
                "恭喜！SendGrid 已经成功接入 🚗"
        );

        return "发送完成";
    }
}