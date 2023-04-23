package com.github.qiu121.config;

import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.github.qiu121.common.enumeration.PermissionEnum;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/3/17
 */
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {
    /**
     * 注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) throws SaTokenException {

        registry.addInterceptor(new SaInterceptor(handler -> {
            //拦截路径，排除路径
            SaRouter.match("/**")
                    .notMatch("/login/**",
                            "/login/users/**",
                            "/users/stu/update/**",
                            "/api/v1/captcha/**",

                            "/api/v1/QRcode/**",
                            "/favicon.ico",

                            //第三方引用资源放行
                            "/lib/**"


                    ).check(r -> StpUtil.checkLogin());

            // 权限校验 -- 不同模块校验不同权限
            SaRouter.match("/users/admin/**", r -> StpUtil.checkPermission(PermissionEnum.ADMIN_PERMISSION.getType()));
            SaRouter.match("/colleges/**", r -> StpUtil.checkPermission(PermissionEnum.ADMIN_PERMISSION.getType()));

            SaRouter.match("/users/stuAdmin/**", r -> StpUtil.checkPermission(
                    PermissionEnum.ADMIN_PERMISSION.getType()));
            SaRouter.match("/users/stuAdmin/**", r -> StpUtil.checkPermission(
                    PermissionEnum.STU_ADMIN_PERMISSION.getType()));

        })).addPathPatterns("/**");
    }
}

