package com.github.qiu121.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.qiu121.common.R;
import com.github.qiu121.pojo.Student;
import com.github.qiu121.pojo.Teacher;
import com.github.qiu121.service.StudentService;
import com.github.qiu121.service.TeacherService;
import com.github.qiu121.util.SHAUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/3/17
 */
@RestController
@RequestMapping("/users")
@Slf4j
public class UserCRUDController {
    @Resource
    private StudentService studentService;
    @Resource
    private TeacherService teacherService;

    /**
     * 学生账户修改密码
     *
     * @param student     学生类对象(账户名、新的密码)
     * @param oldPassword 旧的密码
     * @return 响应封装类型
     */
    @PutMapping("/update/stu/{oldPassword}")
    public R<String> update(@RequestBody Student student, @PathVariable String oldPassword) {
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
        studentQueryWrapper.lambda()//使用函数式接口,方法引用
                .select(Student::getId)
                .eq(Student::getUsername, student.getUsername())
                .eq(Student::getPassword, SHAUtil.SHA256(oldPassword));

        Student one = studentService.getOne(studentQueryWrapper);
        if (one == null) {
            throw new NullPointerException("账号或密码错误,请检查账号信息");
        }

        //加密处理
        if (StringUtils.isNotBlank(student.getPassword())) {
            student.setPassword(SHAUtil.SHA256(student.getPassword()));
        }

        UpdateWrapper<Student> wrapper = new UpdateWrapper<>();
        wrapper.lambda().
                eq(Student::getUsername, student.getUsername())
                .set(Student::getPassword, student.getPassword());

        boolean flag = studentService.update(wrapper);
        log.info("修改完成:{}", flag);
        return flag ? new R<>(20031, "修改完成") :
                new R<>(20032, "修改失败");

    }

    @PutMapping("/update/teach")
    public R<String> update(@RequestBody Teacher teacher) {

        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()//使用函数式接口,方法引用
                .select(Teacher::getId)
                .eq(Teacher::getUsername, teacher.getUsername());

        Teacher one = teacherService.getOne(queryWrapper);
        if (one == null) {
            throw new NullPointerException("该账户不存在,请检查账号信息");
        }

        //加密处理
        if (StringUtils.isNotBlank(teacher.getPassword())) {
            teacher.setPassword(SHAUtil.SHA256(teacher.getPassword()));
        }
        LambdaUpdateWrapper<Teacher> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Teacher::getUsername, teacher.getUsername())
                .set(Teacher::getPassword, teacher.getPassword());

        boolean flag = teacherService.update(wrapper);
        log.info("修改完成:{}", flag);
        return flag ? new R<>(20031, "修改成功") :
                new R<>(20032, "修改失败");

    }

    /**
     * 新增单个一般信息员账户
     *
     * @param student 学生信息员对象
     * @return 封装数据对象
     */
    @PostMapping("/addUser/stu")
    public R<String> addStuUser(@RequestBody Student student) {
        if (StringUtils.isNotBlank(student.getPassword())) {
            student.setPassword(SHAUtil.SHA256(student.getPassword()));
        }
        boolean flag = studentService.save(student);
        R<String> r = new R<>();
        log.info("新增单个用户完成:{}", flag);
        return flag ? r.setMsg("新增用户完成").setCode(20011) :
                r.setMsg("操作失败，请重试").setCode(20012);
    }

}
