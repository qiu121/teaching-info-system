package com.github.qiu121.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.qiu121.mapper.CollegeMapper;
import com.github.qiu121.pojo.College;
import com.github.qiu121.service.CollegeService;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/3/14
 */
@Service
public class CollegeServiceImpl extends ServiceImpl<CollegeMapper, College> implements CollegeService {
}
