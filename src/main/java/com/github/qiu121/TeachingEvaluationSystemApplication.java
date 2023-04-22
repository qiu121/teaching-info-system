package com.github.qiu121;

import cn.dev33.satoken.SaManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/3/14
 */
@SpringBootApplication
@Slf4j
@EnableJpaAuditing
public class TeachingEvaluationSystemApplication {
    public static void main(String... args) {
        SpringApplication.run(TeachingEvaluationSystemApplication.class, args);
        log.info("启动成功：Sa-Token配置如下:{}", SaManager.getConfig());

    }
}
