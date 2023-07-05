package com.github.qiu121.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/07/05
 * @description 登录DTO对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不可为空")
    private String username;
    /**
     * 用户密码
     */
    @NotBlank(message = "密码不可为空")
    private String password;
    /**
     * 登录验证码
     */
    private String captcha;
}
