package com.github.qiu121.config;

import cn.dev33.satoken.stp.StpInterface;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.qiu121.pojo.Permission;
import com.github.qiu121.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/3/17
 */
@Component
@Slf4j
public class StpInterfaceImpl implements StpInterface {
    @Resource
    private PermissionService permissionService;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {

        final LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Permission::getType)
                .eq(Permission::getUsername, loginId.toString());
        final Permission one = permissionService.getOne(wrapper);

        final ArrayList<String> list = new ArrayList<>();
        list.add(one.getType());
        log.info("当前用户权限：{}", list);

        return list;
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return new ArrayList<>();
    }

}

