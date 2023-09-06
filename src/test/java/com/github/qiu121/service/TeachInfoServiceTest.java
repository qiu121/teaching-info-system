package com.github.qiu121.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.qiu121.entity.TeachInfo;
import com.github.qiu121.mapper.TeachInfoMapper;
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
class TeachInfoServiceTest {
    @Resource
    private TeachInfoMapper teachInfoMapper;

    /**
     * 条件查询[教学信息]测试
     */
    @Test
    void testListTeachInfoByCondition() {
        TeachInfo model = new TeachInfo();
        model.setCourseName("操");

        QueryWrapper<TeachInfo> wrapper = new QueryWrapper<>();
        wrapper.select("*").lambda()
                .like(StringUtils.isNotBlank(model.getTeacherName()), TeachInfo::getTeacherName, model.getTeacherName())
                .like(StringUtils.isNotBlank(model.getCourseName()), TeachInfo::getCourseName, model.getCourseName())
                .like(StringUtils.isNotBlank(model.getClassLocation()), TeachInfo::getClassLocation, model.getClassLocation());
        List<TeachInfo> list = teachInfoMapper.selectList(wrapper);

        list.forEach(System.out::println);
    }

}