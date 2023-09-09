package com.github.qiu121.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.qiu121.common.R;
import com.github.qiu121.common.enumeration.PermissionEnum;
import com.github.qiu121.common.exception.BusinessException;
import com.github.qiu121.common.exception.DuplicateException;
import com.github.qiu121.dto.StudentDTO;
import com.github.qiu121.entity.Permission;
import com.github.qiu121.entity.Student;
import com.github.qiu121.service.PermissionService;
import com.github.qiu121.service.StudentService;
import com.github.qiu121.util.SecureUtil;
import com.github.qiu121.vo.StudentVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/03/17
 * @description
 */
@RestController
@RequestMapping("/users/stu")
@Slf4j
@Tag(name = "学生信息员用户操作接口")
public class StudentUserController {
    @Resource
    private StudentService studentService;
    @Resource
    private PermissionService permissionService;

    @PostMapping("/add")
    @SaCheckRole("admin")
    @Operation(description = "新增学生信息员用户", summary = "新增")
    public R<Boolean> addUser(@RequestBody StudentDTO studentDTO) {

        final String username = studentDTO.getUsername();
        final String password = studentDTO.getPassword();
        if (password != null) {// 哈希加密
            studentDTO.setPassword(SecureUtil.encrypt(password));
        }
        // 查询现有用户名，校验重复数据
        // 修改为按权限表判定唯一性，而不是用户表
        final List<String> usernameList = permissionService.list()
                .stream()
                .map(Permission::getUsername)
                .collect(Collectors.toList());


        if (!usernameList.contains(username)) {
            final boolean savePermission = permissionService.save(new Permission(username, PermissionEnum.STU_PERMISSION.getType()));

            // 将DTO对象转换为实体映射类
            Student student = new Student(studentDTO);
            final boolean saveUser = studentService.save(student);
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
     * 学生账户修改密码
     *
     * @param studentDTO  学生类DTO对象(账户名、新的密码)
     * @param oldPassword 旧的密码
     * @return 响应封装类型
     */
    @PutMapping("/update/{oldPassword}")
    @Operation(description = "修改学生信息员用户密码", summary = "修改密码")
    public R<String> updatePassword(@RequestBody StudentDTO studentDTO,
                                    @PathVariable String oldPassword) {
        LambdaQueryWrapper<Student> studentWrapper = new LambdaQueryWrapper<>();
        final String encryptedOldPassword = SecureUtil.encrypt(oldPassword);
        studentWrapper.select(Student::getId)
                .eq(Student::getUsername, studentDTO.getUsername())
                .eq(Student::getPassword, encryptedOldPassword);

        Student one = studentService.getOne(studentWrapper);
        if (one == null) {
            throw new BusinessException("账号或密码错误,请检查账号信息");
        }

        // 加密处理
        if (StringUtils.isNotBlank(studentDTO.getPassword())) {
            studentDTO.setPassword(SecureUtil.encrypt(studentDTO.getPassword()));

            if (encryptedOldPassword.equals(studentDTO.getPassword())) {
                throw new DuplicateException("新密码不可和原密码一致");
            }
        }

        LambdaUpdateWrapper<Student> wrapper = new UpdateWrapper<Student>().lambda();
        wrapper.eq(Student::getUsername, studentDTO.getUsername())
                .set(Student::getPassword, studentDTO.getPassword());

        boolean success = studentService.update(wrapper);
        log.info("修改完成:{}", success);
        return success ? new R<>(20031, "修改完成") :
                new R<>(20032, "修改失败");

    }

    /**
     * 根据 id 查询信息员用户
     *
     * @param id 主键
     * @return R
     */
    @GetMapping("/get/{id}")
    @SaCheckRole("admin")
    @Operation(description = "查询学生信息员用户信息", summary = "查询")
    public R<StudentVo> getUser(@PathVariable Long id) {
        final Student student = studentService.getById(id);
        Integer code = 20040;
        String msg = "查询完成";
        if (student != null) {
            log.info("查询完成,查询结果： {}", student);
            return new R<>(code, msg, new StudentVo(student));
        }
        return new R<>(code, msg, null);

    }

    /**
     * 根据id 批量删除信息员账户
     *
     * @param idArray 主键数组
     * @return R
     */
    @DeleteMapping("/removeBatch/{idArray}")
    @SaCheckRole("admin")
    @Operation(description = "批量删除学生信息员用户", summary = "批量删除")
    public R<Boolean> removeBatchUser(@PathVariable Long[] idArray) {

        // 根据 用户id 查询用户名
        final LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Student::getUsername)
                .in(Student::getId, Arrays.asList(idArray));

        List<String> usernameList = studentService.list(wrapper)
                .stream()
                .map(Student::getUsername)
                .collect(Collectors.toList());


        // 根据用户名,删除权限表中对应数据
        final LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Permission::getUsername, usernameList)
                .eq(Permission::getType, PermissionEnum.STU_PERMISSION.getType());


        final boolean removed = permissionService.remove(queryWrapper);
        final boolean success = studentService.removeByIds(Arrays.asList(idArray));

        log.info("删除完成： {}", success & removed);
        return success & removed ? new R<>(20021, "删除完成") :
                new R<>(20022, "删除失败");
    }

    /**
     * 修改信息员账户信息
     *
     * @param studentDTO 信息员用户DTO对象(带id)
     * @return R
     */
    @PutMapping("/update")
    @SaCheckRole("admin")
    @Operation(description = "修改学生信息员用户信息", summary = "修改")
    private R<Boolean> updateUser(@RequestBody StudentDTO studentDTO) {

        final String oldPassword = studentDTO.getPassword();
        if (StringUtils.isNotBlank(oldPassword)) {
            studentDTO.setPassword(SecureUtil.encrypt(studentDTO.getPassword()));
        }

        final LambdaUpdateWrapper<Student> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Student::getId, studentDTO.getId())
                .set(StringUtils.isNotBlank(studentDTO.getUsername()), Student::getUsername, studentDTO.getUsername())
                .set(StringUtils.isNotBlank(studentDTO.getPassword()), Student::getPassword, studentDTO.getPassword())
                .set(StringUtils.isNotBlank(studentDTO.getClassName()), Student::getClassName, studentDTO.getClassName())
                .set(StringUtils.isNotBlank(studentDTO.getName()), Student::getName, studentDTO.getName())

                // 限定输入，不需要动态判断为空
                .set(Student::getCollege, studentDTO.getCollege())
                .set(Student::getEnrollmentYear, studentDTO.getEnrollmentYear())
                .set(Student::getEducationLevel, studentDTO.getEducationLevel());

        final boolean success = studentService.update(wrapper);
        // 直接通过 id更新，不能动态更新。传入的数据包括时间类型，直接覆盖之前时间(也就是同样的时间,就不能做到自动同步更新修改时间)
//        final boolean success = studentService.updateById(student);

        log.info("修改完成: {}", success);
        return success ? new R<>(20031, "修改完成") :
                new R<>(20032, "修改失败");

    }

    /**
     * 动态条件(学院、班级)、分页查询信息员账户
     *
     * @param studentDTO 学生类DTO对象
     * @param currentNum 当前页号
     * @param pageSize   每页条数
     * @return R
     */
    @PostMapping("/listAll/{currentNum}/{pageSize}")
    @SaCheckRole("admin")
    @Operation(description = "分页查询所有学生信息员用户信息", summary = "分页查询")
    public R<IPage<StudentVo>> list(@RequestBody StudentDTO studentDTO,
                                    @PathVariable long currentNum,
                                    @PathVariable long pageSize) {
        final LambdaQueryWrapper<Student> wrapper = new QueryWrapper<Student>().lambda();
        wrapper.like(StringUtils.isNotBlank(studentDTO.getCollege()), Student::getCollege, studentDTO.getCollege())
                .like(StringUtils.isNotBlank(studentDTO.getClassName()), Student::getClassName, studentDTO.getClassName())
                .like(StringUtils.isNotBlank(studentDTO.getEducationLevel()), Student::getEducationLevel, studentDTO.getEducationLevel());

        final Page<Student> page = studentService.page(new Page<>(currentNum, pageSize), wrapper);


        // 将 List<Student> 转化为 List<StudentVo>
        List<StudentVo> studentVoList = page.getRecords().stream()
                .map(s -> {
                    StudentVo studentVo = new StudentVo();
                    BeanUtils.copyProperties(s, studentVo);
                    return studentVo;
                }).collect(Collectors.toList());

        // 将 List<StudentVo> 封装到 Page<StudentVo> 并返回
        Page<StudentVo> studentVoPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        studentVoPage.setRecords(studentVoList);
        log.info("查询结果： {}", studentVoPage);
        return new R<>(20040, "查询完成", studentVoPage);

    }


}
