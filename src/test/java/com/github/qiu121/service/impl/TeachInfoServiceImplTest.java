package com.github.qiu121.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.qiu121.mapper.TeachInfoMapper;
import com.github.qiu121.pojo.TeachInfo;
import com.github.qiu121.service.TeachInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/3/15
 */
@SpringBootTest
@Slf4j
class TeachInfoServiceImplTest {
    @Resource
    private TeachInfoService teachInfoService;
    @Resource
    private TeachInfoMapper teachInfoMapper;

    @Test
    void add() {
        TeachInfo teachInfo = new TeachInfo();
        TeachInfo model = teachInfo.setCourseName("操作系统");
        boolean save = teachInfoService.save(model);
        System.out.println(save);
    }

    @Test
    void getByCondition() {
        TeachInfo model = new TeachInfo();
        model.setCourseName("操");

        QueryWrapper<TeachInfo> wrapper = new QueryWrapper<>();
        wrapper.select("*")
                .like(StringUtils.isNotBlank(model.getTeacherName()), "teacher_name", model.getTeacherName())
                .like(StringUtils.isNotBlank(model.getCourseName()), "course_name", model.getCourseName())
                .like(StringUtils.isNotBlank(model.getClassLocation()), "class_location", model.getClassLocation());
        List<TeachInfo> list = teachInfoMapper.selectList(wrapper);

        list.forEach(System.out::println);
    }

}