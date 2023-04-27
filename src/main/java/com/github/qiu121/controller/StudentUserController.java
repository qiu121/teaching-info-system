package com.github.qiu121.controller;

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
import com.github.qiu121.pojo.Permission;
import com.github.qiu121.pojo.Student;
import com.github.qiu121.service.PermissionService;
import com.github.qiu121.service.StudentService;
import com.github.qiu121.util.SecureUtil;
import com.github.qiu121.vo.StudentVo;
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
 * @description
 */
@RestController
@CrossOrigin
@RequestMapping("/users/stu")
@Slf4j
public class StudentUserController {
    @Resource
    private StudentService studentService;
    @Resource
    private PermissionService permissionService;

    @PostMapping("/add")
    public R<Boolean> addUser(@RequestBody Student student) {

        final String username = student.getUsername();
        final String password = student.getPassword();
        if (password != null) {//哈希加密
            student.setPassword(SecureUtil.encrypt(password));
        }
        //查询现有用户名，校验重复数据
        final ArrayList<String> usernameList = new ArrayList<>();
        for (Student stu : studentService.list()) {
            usernameList.add(stu.getUsername());
        }
        if (!usernameList.contains(username)) {
            final boolean savePermission = permissionService.save(new Permission(username, PermissionEnum.STU_PERMISSION.getType()));
            final boolean saveUser = studentService.save(student);
            if (savePermission & saveUser) {
                log.info("添加完成： {}", true);
                return new R<>(20012, "添加完成");
            }
        } else {
            throw new DuplicateException("该账户已存在，请勿重复添加");
        }
        return new R<>(20012, "添加失败");

    }

    /**
     * 学生账户修改密码
     *
     * @param student     学生类对象(账户名、新的密码)
     * @param oldPassword 旧的密码
     * @return 响应封装类型
     */
    @PutMapping("/update/{oldPassword}")
    public R<String> updatePassword(@RequestBody Student student, @PathVariable String oldPassword) {
        LambdaQueryWrapper<Student> studentWrapper = new LambdaQueryWrapper<>();
        final String encryptedOldPassword = SecureUtil.encrypt(oldPassword);
        studentWrapper.select(Student::getId)
                .eq(Student::getUsername, student.getUsername())
                .eq(Student::getPassword, encryptedOldPassword);

        Student one = studentService.getOne(studentWrapper);
        if (one == null) {
            throw new BusinessException("账号或密码错误,请检查账号信息");
        }

        //加密处理
        if (StringUtils.isNotBlank(student.getPassword())) {
            student.setPassword(SecureUtil.encrypt(student.getPassword()));

            if (encryptedOldPassword.equals(student.getPassword())) {
                throw new DuplicateException("新密码不可和原密码一致");
            }
        }

        LambdaUpdateWrapper<Student> wrapper = new UpdateWrapper<Student>().lambda();
        wrapper.eq(Student::getUsername, student.getUsername())
                .set(Student::getPassword, student.getPassword());

        boolean success = studentService.update(wrapper);
        log.info("修改完成:{}", success);
        return success ? new R<>(20031, "修改完成") :
                new R<>(20032, "修改失败");

    }

    /**
     * 根据id 批量删除信息员账户
     *
     * @param idArray 主键数组
     * @return R
     */
    @DeleteMapping("/removeBatch/{idArray}")
    public R<Boolean> removeBatchUser(@PathVariable Long[] idArray) {

        //根据 用户id 查询用户名
        final LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Student::getUsername)
                .in(Student::getId, Arrays.asList(idArray));
        final ArrayList<String> usernameList = new ArrayList<>();
        for (Student student : studentService.list(wrapper)) {
            usernameList.add(student.getUsername());
        }

        //根据用户名,删除权限表中对应数据
        final LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Permission::getUsername, usernameList)
                .eq(Permission::getType, PermissionEnum.STU_PERMISSION.getType());


        final boolean removed = permissionService.remove(queryWrapper);
        final boolean success = studentService.removeByIds(Arrays.asList(idArray));

        log.info("修改完成： {}", success & removed);
        return success & removed ? new R<>(20021, "删除完成") :
                new R<>(20022, "删除失败");
    }

    /**
     * 根据 id 查询信息员用户
     *
     * @param id 主键
     * @return R
     */
    @GetMapping("/get/{id}")
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
     * 修改信息员账户信息
     *
     * @param student 信息员用户(带id)
     * @return R
     */
    @PutMapping("/update")
    private R<Boolean> updateUser(@RequestBody Student student) {

        final String oldPassword = student.getPassword();
        if (StringUtils.isNotBlank(oldPassword)) {
            student.setPassword(SecureUtil.encrypt(student.getPassword()));
        }
        final boolean success = studentService.updateById(student);

        log.info("修改完成: {}", success);
        return success ? new R<>(20031, "修改完成") :
                new R<>(20032, "修改失败");

    }

    /**
     * 动态条件(学院、班级)、分页查询信息员账户
     *
     * @param student    学生类对象
     * @param currentNum 当前页号
     * @param pageSize   每页条数
     * @return R
     */
    @GetMapping("/listAll/{currentNum}/{pageSize}")
    public R<IPage<StudentVo>> list(@RequestBody Student student,
                                    @PathVariable long currentNum,
                                    @PathVariable long pageSize) {
        final LambdaQueryWrapper<Student> wrapper = new QueryWrapper<Student>().lambda();
        wrapper.like(StringUtils.isNotBlank(student.getCollege()), Student::getCollege, student.getCollege());
        wrapper.like(StringUtils.isNotBlank(student.getClassName()), Student::getClassName, student.getClassName());

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
