package com.github.qiu121.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.qiu121.common.R;
import com.github.qiu121.common.exception.BusinessException;
import com.github.qiu121.pojo.Permission;
import com.github.qiu121.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/04/23
 * @description 查询账户权限
 */
@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/users/permission")
public class PermissionController {
    @Resource
    private PermissionService permissionService;

    /**
     * 查询用户权限
     *
     * @param username 用户名
     * @return R
     */
    @GetMapping("/{username}")
    public R<?> getPermission(@PathVariable String username) {
        final LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Permission::getUsername, username);
        log.info("校验权限用户： {}", username);
        if (ObjectUtils.isEmpty(wrapper)) {
            throw new BusinessException("该用户不存在");
        }
        final String type = permissionService.getOne(wrapper).getType();
        return new R<>(20040, "查询完成", type);
    }
}
