package com.github.qiu121.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/09/09
 * @desc Knife4j配置
 */
@Configuration
public class SpringdocConfiguration {
    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .description("校教学信息员反馈系统接口文档")
                        .title("校教学信息员反馈系统接口文档")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("to_Geek")
                                .email("geeeek@duck.com"))
                        .license(new License()
                                .name("MIT")
                                .url("https://github.com/qiu121/teaching-info-system")
                        )
                );
    }
}
