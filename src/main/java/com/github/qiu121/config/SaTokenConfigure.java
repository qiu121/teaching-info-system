package com.github.qiu121.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.spring.SpringMVCUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaFoxUtil;
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
                            "/users/stu/update/**",
                            "/api/**",
                            "/favicon.ico"

//                    ).check(r -> StpUtil.checkLogin())
                    ).check(r -> {
                        //未登录，拦截重定向到登录页面
                        if (!StpUtil.isLogin()) {
                            String back = SaFoxUtil.joinParam(SaHolder.getRequest().getUrl(),
                                    SpringMVCUtil.getRequest().getQueryString());
                            SaHolder.getResponse().redirect("/login/index.html?back=" + SaFoxUtil.encodeUrl(back));
                            SaRouter.back();
                        }
                    })
            ;


            // 角色权限校验 -- 不同模块校验不同权限
            SaRouter.match("/submit/**", r -> StpUtil.checkRole(PermissionEnum.STU_PERMISSION.getType()));
            SaRouter.match("/secondary/**", r -> StpUtil.checkRole(PermissionEnum.STU_ADMIN_PERMISSION.getType()));
            SaRouter.match("/backend/**", r -> StpUtil.checkRole(PermissionEnum.ADMIN_PERMISSION.getType()));

        })).addPathPatterns("/**");
    }
}

