package com.github.qiu121.controller;

import cn.dev33.satoken.basic.SaBasicUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/03/29
 * <p>
 * 退出当前账号，注销下线
 */
@RestController
@RequestMapping("/logout/users")
@Slf4j
@CrossOrigin
public class LogoutController {

    @GetMapping
    public SaResult logout() {
        StpUtil.logout();
        log.info("当前账号已登出： {}", StpUtil.getTokenInfo());
        return SaResult.ok("当前账号已登出");
    }

    @GetMapping("/test")
    public SaResult test() {
        SaBasicUtil.check("sa:123456");
        return SaResult.ok();
    }
}
