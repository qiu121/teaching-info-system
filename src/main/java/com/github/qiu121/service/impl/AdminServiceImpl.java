package com.github.qiu121.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.qiu121.mapper.AdminMapper;
import com.github.qiu121.pojo.Admin;
import com.github.qiu121.service.AdminService;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/3/14
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
}
