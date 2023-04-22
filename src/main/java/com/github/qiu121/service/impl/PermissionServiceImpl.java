package com.github.qiu121.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.qiu121.mapper.PermissionMapper;
import com.github.qiu121.pojo.Permission;
import com.github.qiu121.service.PermissionService;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/3/14
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {
}
