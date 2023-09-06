package com.github.qiu121.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.qiu121.entity.Admin;
import com.github.qiu121.entity.StuAdmin;
import com.github.qiu121.entity.Student;
import com.github.qiu121.util.SecureUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/3/15
 * @desc 测试用户登入
 */
@SpringBootTest
@Slf4j
class LoginServiceTest {
    @Resource
    private StudentService studentService;
    @Resource
    private StuAdminService stuAdminService;
    @Resource
    private AdminService adminService;

    /**
     * 测试学生(信息员)用户无验证码登入
     */
    @Test
    void testStudentLoginWithoutCaptcha() {
        QueryWrapper<Student> wrapper = new QueryWrapper<>();

        wrapper.lambda()
                .eq(Student::getPassword, SecureUtil.encrypt("test"))
                .eq(Student::getUsername, "test")
                .select(Student::getId);

        Student one = studentService.getOne(wrapper, true);
        log.info("{}", one);

    }

    /**
     * 测试信息员组长用户无验证码登入
     */
    @Test
    void testStuAdminLoginWithCaptcha() {
        QueryWrapper<StuAdmin> wrapper = new QueryWrapper<>();

        wrapper.lambda()
                .eq(StuAdmin::getPassword, SecureUtil.encrypt("stuadmin"))
                .eq(StuAdmin::getUsername, "stuadmin");

        StuAdmin one = stuAdminService.getOne(wrapper, true);
        log.info("{}", one);

    }

    /**
     * 测试管理员用户无验证码登入
     */
    @Test
    void testAdminLoginWithCaptcha() {
        QueryWrapper<Admin> wrapper = new QueryWrapper<>();

        wrapper.lambda()
                .eq(Admin::getPassword, SecureUtil.encrypt("admin"))
                .eq(Admin::getUsername, "admin");
        Admin one = adminService.getOne(wrapper, true);
        log.info("{}", one);
    }

}