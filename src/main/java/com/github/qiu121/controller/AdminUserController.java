package com.github.qiu121.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.qiu121.common.R;
import com.github.qiu121.common.enumeration.PermissionEnum;
import com.github.qiu121.common.exception.BusinessException;
import com.github.qiu121.common.exception.DuplicateException;
import com.github.qiu121.dto.AdminDTO;
import com.github.qiu121.entity.Admin;
import com.github.qiu121.entity.Permission;
import com.github.qiu121.service.AdminService;
import com.github.qiu121.service.PermissionService;
import com.github.qiu121.util.SecureUtil;
import com.github.qiu121.vo.AdminVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/03/17
 * @description
 */
@RestController
@Slf4j
@CrossOrigin
@SaCheckRole("admin")
@RequestMapping("/users/admin")
public class AdminUserController {
    @Resource
    private AdminService adminService;
    @Resource
    private PermissionService permissionService;

    /**
     * 新增管理员用户
     *
     * @param adminDTO 管理员DTO
     * @return R
     */
    @PostMapping("/add")
    public R<Boolean> addUser(@RequestBody AdminDTO adminDTO) {
        final String username = adminDTO.getUsername();
        final String password = adminDTO.getPassword();
        if (password != null) {//哈希加密
            adminDTO.setPassword(SecureUtil.encrypt(password));
        }
        //查询现有用户名，校验重复数据
        final Set<String> usernameList = permissionService.list()
                .stream()
                .map(Permission::getUsername)
                .collect(Collectors.toSet());

        if (!usernameList.contains(username)) {
            final boolean savePermission = permissionService.save(
                    new Permission(username, PermissionEnum.ADMIN_PERMISSION.getType()));
            //DTO -> DAO
            Admin admin = new Admin(adminDTO);
            final boolean saveUser = adminService.save(admin);
            if (savePermission & saveUser) {
                log.info("添加完成： {}", true);
                return new R<>(20010, "添加完成");
            }
        } else {
            throw new DuplicateException("该账户已存在，请勿重复添加");
        }
        return new R<>(20012, "添加失败");


    }

    /**
     * 根据 id查询
     *
     * @param id
     * @return
     */
    @GetMapping("/get/{id}")
    public R<AdminVo> getUser(@PathVariable Long id) {
        final Admin admin = adminService.getById(id);
        Integer code = 20040;
        String msg = "查询完成";
        if (!ObjectUtils.isEmpty(admin)) {
            log.info("查询完成,查询结果： {}", admin);
            return new R<>(code, msg, new AdminVo(admin));
        }
        return new R<>(code, msg, null);

    }

    /**
     * 根据 id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("/remove/{id}")
    public R<Boolean> removeUser(@PathVariable Long id) {

        //根据用户 id查询用户名
        final LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Admin::getUsername)
                .in(Admin::getId, id);

        final List<String> adminUsernameList = adminService.list(wrapper)
                .stream()
                .map(Admin::getUsername)
                .collect(Collectors.toList());

        if (CollectionUtils.containsAny(adminUsernameList, Collections.singleton(StpUtil.getLoginId()))) {
            throw new BusinessException("当前用户不可移除");
        }

        //编写条件构造器，查询用户名，进行权限删除
        final LambdaQueryWrapper<Permission> permissionWrapper = new LambdaQueryWrapper<>();
        permissionWrapper.in(Permission::getUsername, adminUsernameList)
                .eq(Permission::getType, PermissionEnum.ADMIN_PERMISSION.getType());

        final boolean removePer = permissionService.remove(permissionWrapper);
        final boolean removeUser = adminService.removeById(id);

        log.info("删除完成： {}", removePer & removeUser);
        return new R<>(20020, "删除完成");
    }

    /**
     * 修改管理员用户
     *
     * @param adminDTO 管理员DTO
     * @return R
     */
    @PutMapping("/update")
    public R<Boolean> updateUser(@RequestBody AdminDTO adminDTO) {

        final String oldPassword = adminDTO.getPassword();
        if (StringUtils.isNotBlank(oldPassword)) {
            adminDTO.setPassword(SecureUtil.encrypt(adminDTO.getPassword()));
        }

        //动态修改
        final LambdaUpdateWrapper<Admin> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Admin::getId, adminDTO.getId())
                .set(StringUtils.isNotBlank(adminDTO.getUsername()), Admin::getUsername, adminDTO.getUsername())
                .set(StringUtils.isNotBlank(adminDTO.getPassword()), Admin::getPassword, adminDTO.getPassword());

        final boolean success = adminService.update(wrapper);

        log.info("修改完成: {}", success);
        return success ? new R<>(20031, "修改完成") :
                new R<>(20032, "修改失败");

    }

    /**
     * 列出所有管理员账户
     *
     * @return
     */
    @GetMapping("/list")
    public R<List<AdminVo>> list() {
        final List<Admin> list = adminService.list();
        final List<AdminVo> voList = list.stream()
                .map(admin -> {
                    AdminVo vo = new AdminVo();
                    BeanUtils.copyProperties(admin, vo);
                    return vo;
                }).collect(Collectors.toList());

        log.info("查询结果： {}", voList);
        return new R<>(20040, "查询完成", voList);
    }

}
