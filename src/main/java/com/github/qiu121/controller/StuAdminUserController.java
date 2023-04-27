package com.github.qiu121.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.qiu121.common.R;
import com.github.qiu121.common.enumeration.PermissionEnum;
import com.github.qiu121.common.exception.DuplicateException;
import com.github.qiu121.pojo.Permission;
import com.github.qiu121.pojo.StuAdmin;
import com.github.qiu121.service.PermissionService;
import com.github.qiu121.service.StuAdminService;
import com.github.qiu121.util.SecureUtil;
import com.github.qiu121.vo.StuAdminVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/03/17
 * @description 几乎和StudentUserController一致，懒得写😂
 */
@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/users/stuAdmin")
public class StuAdminUserController {

    @Resource
    private StuAdminService stuAdminService;
    @Resource
    private PermissionService permissionService;

    /**
     * 新增组长账户
     *
     * @param stuAdmin
     * @return
     */
    @PostMapping("/add")
    public R<Boolean> addUser(@RequestBody StuAdmin stuAdmin) {
        final String username = stuAdmin.getUsername();
        final String password = stuAdmin.getPassword();
        if (StringUtils.isNotBlank(password)) {//哈希加密
            stuAdmin.setPassword(SecureUtil.encrypt(password));
        }
        //查询现有用户名，校验重复数据
        final ArrayList<String> usernameList = new ArrayList<>();
        for (StuAdmin admin : stuAdminService.list()) {
            usernameList.add(admin.getUsername());
        }
        if (!usernameList.contains(username)) {
            final boolean saveUser = stuAdminService.save(stuAdmin);
            final boolean savePermission = permissionService.save(
                    new Permission(username, PermissionEnum.STU_ADMIN_PERMISSION.getType()));
            if (savePermission & saveUser) {
                log.info("添加完成： {}", true);
                return new R<>(20012, "添加完成");
            }
        } else {
            throw new DuplicateException("添加失败，该账户已存在");
        }
        return new R<>(20012, "添加失败");

    }

    /**
     * 批量删除组长账户
     *
     * @param idArray id数组
     * @return
     */
    @DeleteMapping("/removeBatch/{idArray}")
    public R<Boolean> removeBatchUser(@PathVariable Long[] idArray) {

        //根据 用户id 查询用户名
        final QueryWrapper<StuAdmin> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .select(StuAdmin::getUsername)
                .in(StuAdmin::getId, Arrays.asList(idArray));
        final ArrayList<String> usernameList = new ArrayList<>();
        for (StuAdmin stuAdmin : stuAdminService.list()) {
            usernameList.add(stuAdmin.getUsername());
        }

        //根据用户名,删除权限表中对应数据
        final LambdaQueryWrapper<Permission> queryWrapper = new QueryWrapper<Permission>().lambda();
        queryWrapper.in(Permission::getUsername, usernameList)
                .eq(Permission::getType, PermissionEnum.STU_ADMIN_PERMISSION.getType());
        final boolean removedPermission = permissionService.remove(queryWrapper);
        final boolean removedUser = stuAdminService.removeByIds(Arrays.asList(idArray));

        log.info("删除完成： {}", removedPermission & removedUser);
        return removedPermission & removedUser ? new R<>(20021, "删除完成") :
                new R<>(20022, "删除失败");
    }

    /**
     * 根据 id 查询组长账户信息
     *
     * @param id
     * @return
     */
    @GetMapping("/get/{id}")
    public R<StuAdminVo> getUser(@PathVariable Long id) {
        final StuAdmin stuAdmin = stuAdminService.getById(id);
        Integer code = 20040;
        String msg = "查询完成";
        if (stuAdmin != null) {
            log.info("查询完成");
            return new R<>(code, msg, new StuAdminVo(stuAdmin));
        }
        return new R<>(code, msg, null);

    }

    /**
     * 修改组长用户信息
     *
     * @param stuAdmin
     * @return
     */
    @PutMapping("/update")
    private R<Boolean> updateUser(@RequestBody StuAdmin stuAdmin) {

        final String oldPassword = stuAdmin.getPassword();
        if (StringUtils.isNotBlank(oldPassword)) {
            stuAdmin.setPassword(SecureUtil.encrypt(stuAdmin.getPassword()));
        }
        final boolean success = stuAdminService.updateById(stuAdmin);

        log.info("修改完成： {}", success);
        return success ? new R<>(20031, "修改完成") :
                new R<>(20032, "修改失败");

    }

    /**
     * 动态条件(学院、班级)、分页询组长账户
     *
     * @param stuAdmin   信息员组长对象
     * @param currentNum 当前页号
     * @param pageSize   每页条数
     * @return R<IPage < StuAdmin>>
     */
    @GetMapping("/list/{currentNum}/{pageSize}")
    public R<IPage<StuAdminVo>> list(@RequestBody StuAdmin stuAdmin,
                                     @PathVariable long currentNum,
                                     @PathVariable long pageSize) {
        final LambdaQueryWrapper<StuAdmin> wrapper = new QueryWrapper<StuAdmin>().lambda();
        wrapper.like(StringUtils.isNotBlank(stuAdmin.getCollege()), StuAdmin::getCollege, stuAdmin.getCollege());
        wrapper.like(StringUtils.isNotBlank(stuAdmin.getClassName()), StuAdmin::getClassName, stuAdmin.getClassName());

        final Page<StuAdmin> page = stuAdminService.page(new Page<>(currentNum, pageSize), wrapper);


        // 将 List<StuAdmin> 转化为 List<StudentAdminVo>
        List<StuAdminVo> stuAdminVoLists = page.getRecords().stream()
                .map(s -> {
                    StuAdminVo stuAdminVo = new StuAdminVo();
                    BeanUtils.copyProperties(s, stuAdminVo);
                    return stuAdminVo;
                }).collect(Collectors.toList());

        // 将 List<StudentVo> 封装到 Page<StudentVo> 并返回
        Page<StuAdminVo> stuAdminPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        stuAdminPage.setRecords(stuAdminVoLists);

        log.info("查询完成: {}", stuAdminPage);
        return new R<>(20040, "查询完成", stuAdminPage);

    }

}

