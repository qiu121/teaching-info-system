package com.github.qiu121.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.qiu121.pojo.Student;
import com.github.qiu121.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/3/15
 */
@SpringBootTest
@Slf4j
class StudentServiceImplTest {
    @Resource
    private StudentService studentService;

    @Test
    void get() {
        QueryWrapper<Student> wrapper = new QueryWrapper<>();

        wrapper.eq("password", SaSecureUtil.sha256("123456"))
                .eq("username", "a14112")
                .select("id");

        Student one = studentService.getOne(wrapper);
        log.info(String.valueOf(one));

    }

    @Test
    void add() {
        Student student = new Student();
        String s = SaSecureUtil.sha256("123456");
        student.setUsername("a14112").setPassword(s);

        boolean save = studentService.save(student);
        log.info(String.valueOf(save));

    }

    @Test
    void update() {
        Student student = new Student();
        student.setId(1L)
                .setUsername("a143312");

        studentService.updateById(student);
    }

}