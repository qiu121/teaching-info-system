package com.github.qiu121;

import com.github.qiu121.config.StpInterfaceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class TeachingEvaluationSystemApplicationTests {
    @Resource
    private StpInterfaceImpl stpInterface;

    @Test
    void contextLoads() {
        stpInterface.getPermissionList("admin","login");
    }

}
