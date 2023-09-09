package com.github.qiu121.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.qiu121.common.R;
import com.github.qiu121.common.enumeration.PermissionEnum;
import com.github.qiu121.common.exception.BusinessException;
import com.github.qiu121.common.exception.DuplicateException;
import com.github.qiu121.common.exception.NotFoundException;
import com.github.qiu121.dto.StudentDTO;
import com.github.qiu121.entity.Permission;
import com.github.qiu121.entity.StuAdmin;
import com.github.qiu121.service.PermissionService;
import com.github.qiu121.service.StuAdminService;
import com.github.qiu121.util.SecureUtil;
import com.github.qiu121.vo.StuAdminVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/03/17
 * @description 几乎和StudentUserController一致，懒得写😂
 */
@RestController
@Slf4j
@RequestMapping("/users/stuAdmin")
@Tag(name = "信息员组长用户操作接口")
public class StuAdminUserController {

    @Resource
    private StuAdminService stuAdminService;
    @Resource
    private PermissionService permissionService;

    /**
     * 新增组长账户
     *
     * @param stuAdminDTO 信息员组长DTO对象
     * @return R
     */
    @PostMapping("/add")
    @SaCheckRole("admin")
    @Operation(description = "新增信息员组长用户", summary = "新增")
    public R<Boolean> addUser(@RequestBody StudentDTO stuAdminDTO) {
        final String username = stuAdminDTO.getUsername();
        final String password = stuAdminDTO.getPassword();
        if (StringUtils.isNotBlank(password)) {// 哈希加密
            stuAdminDTO.setPassword(SecureUtil.encrypt(password));
        }
        // 查询现有用户名，校验重复数据
        final List<String> usernameList = permissionService.list()
                .stream()
                .map(Permission::getUsername)
                .collect(Collectors.toList());

        if (!usernameList.contains(username)) {
            // DTO-> DAO
            StuAdmin stuAdmin = new StuAdmin(stuAdminDTO);
            final boolean saveUser = stuAdminService.save(stuAdmin);
            final boolean savePermission = permissionService.save(
                    new Permission(username, PermissionEnum.STU_ADMIN_PERMISSION.getType()));
            if (savePermission & saveUser) {
                log.info("添加完成： {}", true);
                return new R<>(20010, "添加完成");
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
     * @return R
     */
    @DeleteMapping("/removeBatch/{idArray}")
    @SaCheckRole("admin")
    @Operation(description = "批量删除信息员组长用户", summary = "批量删除")
    public R<Boolean> removeBatchUser(@PathVariable Long[] idArray) {

        // 根据 用户id 查询用户名
        final QueryWrapper<StuAdmin> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .select(StuAdmin::getUsername)
                .in(StuAdmin::getId, Arrays.asList(idArray));

        final List<String> usernameList = stuAdminService.list(wrapper)
                .stream()
                .map(StuAdmin::getUsername)
                .collect(Collectors.toList());

        // 根据用户名,删除权限表中对应数据
        final LambdaQueryWrapper<Permission> queryWrapper = new QueryWrapper<Permission>().lambda();
        queryWrapper.in(Permission::getUsername, usernameList)
                .eq(Permission::getType, PermissionEnum.STU_ADMIN_PERMISSION.getType());
        final boolean removedPermission = permissionService.remove(queryWrapper);
        final boolean removedUser = stuAdminService.removeByIds(Arrays.asList(idArray));

        log.info("删除完成： {}", removedPermission & removedUser);
        return removedPermission & removedUser ?
                new R<>(20021, "删除完成") :
                new R<>(20022, "删除失败");
    }

    /**
     * 根据 id 查询组长账户信息
     *
     * @param id 主键
     * @return R
     */
    @GetMapping("/get/{id}")
    @SaCheckRole("admin")
    @Operation(description = "查询信息员组长用户", summary = "查询")
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
     * 修改信息员组长账户密码
     *
     * @param stuAdminDTO 组长账户DTO
     * @return R
     */
    @PutMapping("/update/secure")
    @SaCheckRole("stuAdmin")
    @Operation(description = "修改信息员组长用户密码", summary = "修改密码")
    public R<?> updateUserPassword(@RequestParam String old, @RequestBody StudentDTO stuAdminDTO) {
        // 验证旧密码
        StuAdmin stuAdminOne = stuAdminService.getOne(new LambdaQueryWrapper<StuAdmin>()
                .eq(StuAdmin::getUsername, stuAdminDTO.getUsername()));
        if (stuAdminOne == null) {
            throw new NotFoundException("账户不存在");
        } else {
            if (!SecureUtil.verify(old, stuAdminOne.getPassword())) {
                return new R<>(20033, "旧密码错误");
            }
            if (Objects.equals(stuAdminDTO.getPassword(), old)) {
                throw new BusinessException("新密码与原密码相同，请重试");
            }
        }

        // 修改新密码
        stuAdminDTO.setPassword(SecureUtil.encrypt(stuAdminDTO.getPassword()));

        final LambdaUpdateWrapper<StuAdmin> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(StuAdmin::getUsername, stuAdminDTO.getUsername())
                .set(StringUtils.isNotBlank(stuAdminDTO.getPassword()), StuAdmin::getPassword, stuAdminDTO.getPassword());
        final boolean success = stuAdminService.update(wrapper);
        return success ? new R<>(20031, "修改完成") :
                new R<>(20032, "修改失败");
    }

    /**
     * 修改组长用户信息
     *
     * @param stuAdminDTO 信息员组长DTO
     * @return R
     */
    @PutMapping("/update")
    @SaCheckRole("admin")
    @Operation(description = "修改信息员组长用户信息", summary = "修改")
    private R<Boolean> updateUser(@RequestBody StudentDTO stuAdminDTO) {

        final String oldPassword = stuAdminDTO.getPassword();
        if (StringUtils.isNotBlank(oldPassword)) {
            stuAdminDTO.setPassword(SecureUtil.encrypt(stuAdminDTO.getPassword()));
        }

        final LambdaUpdateWrapper<StuAdmin> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(StuAdmin::getId, stuAdminDTO.getId())
                .set(StringUtils.isNotBlank(stuAdminDTO.getName()), StuAdmin::getName, stuAdminDTO.getName())
                .set(StringUtils.isNotBlank(stuAdminDTO.getUsername()), StuAdmin::getUsername, stuAdminDTO.getUsername())
                .set(StringUtils.isNotBlank(stuAdminDTO.getPassword()), StuAdmin::getPassword, stuAdminDTO.getPassword())
                .set(StringUtils.isNotBlank(stuAdminDTO.getClassName()), StuAdmin::getClassName, stuAdminDTO.getClassName())

                // 限定的输入格式，不需要判空
                .set(StuAdmin::getCollege, stuAdminDTO.getCollege())
                .set(StuAdmin::getEnrollmentYear, stuAdminDTO.getEnrollmentYear());

        final boolean success = stuAdminService.update(wrapper);

        log.info("修改完成: {}", success);
        return success ? new R<>(20031, "修改完成") :
                new R<>(20032, "修改失败");
    }

    /**
     * 动态条件(学院、班级)、分页询组长账户
     *
     * @param stuAdminDTO 信息员组长DTo对象
     * @param currentNum  当前页号
     * @param pageSize    每页条数
     * @return R<IPage < StuAdmin>>
     */
    @PostMapping("/list/{currentNum}/{pageSize}")
    @SaCheckRole("admin")
    @Operation(description = "分页查询所有信息员用户信息", summary = "分页查询")
    public R<IPage<StuAdminVo>> list(@RequestBody StudentDTO stuAdminDTO,
                                     @PathVariable long currentNum,
                                     @PathVariable long pageSize) {
        final LambdaQueryWrapper<StuAdmin> wrapper = new QueryWrapper<StuAdmin>().lambda();
        wrapper.like(StringUtils.isNotBlank(stuAdminDTO.getCollege()), StuAdmin::getCollege, stuAdminDTO.getCollege())
                .like(StringUtils.isNotBlank(stuAdminDTO.getClassName()), StuAdmin::getClassName, stuAdminDTO.getClassName())
                .like(StringUtils.isNotBlank(stuAdminDTO.getEducationLevel()), StuAdmin::getEducationLevel, stuAdminDTO.getEducationLevel());

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

