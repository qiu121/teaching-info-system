package com.github.qiu121.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/3/14
 * <p>
 * 图形验证码
 */
@RestController
@RequestMapping("/api/v1")
@Slf4j
@Tag(name = "图形验证码生成接口")
public class CaptchaController {
    /**
     * @param session    session参数
     * @param response   响应体参数
     * @param randomCode 随机数(不使用的参数、只是为了调用接口可以随机数刷新)
     * @throws IOException IO异常类型
     */
    @GetMapping("/captcha/{randomCode}")
    @Operation(description = "生成图形验证码", summary = "生成")
    public void getCode(HttpSession session,
                        HttpServletResponse response,
                        @PathVariable String randomCode) throws IOException {

        // 定义图片的显示大小
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(120, 40);

        response.setContentType("image/jpeg");
        response.setHeader("Pragma", "No-cache");

        // 输出到页面
        lineCaptcha.write(response.getOutputStream());
        // 打印日志
        log.info("生成的验证码:{}", lineCaptcha.getCode());


        // 利用session来存储验证码
        session.setAttribute("verifyCode", lineCaptcha.getCode());
        // 关闭流
        response.getOutputStream().close();


    }

}
