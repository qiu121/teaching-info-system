package com.github.qiu121.controller;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.qiu121.common.R;
import com.github.qiu121.pojo.Admin;
import com.github.qiu121.pojo.StuAdmin;
import com.github.qiu121.pojo.Student;
import com.github.qiu121.service.AdminService;
import com.github.qiu121.service.StuAdminService;
import com.github.qiu121.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/3/14
 */
@RestController
@RequestMapping("/login/users")
@Slf4j
public class UserLoginController {
    @Resource
    private StudentService studentService;
    @Resource
    private StuAdminService stuAdminService;
    @Resource
    private AdminService adminService;

    /**
     * 学生信息员用户登录
     *
     * @param student   学生类对象(用户名、密码)
     * @param inputCode 图形验证码
     * @param request   请求体参数
     * @return 响应体封装类
     */
    @PostMapping("/stu/{inputCode}")
    public R<List<HashMap<String, Object>>> login(@RequestBody @Validated Student student,
                                                  @PathVariable String inputCode,
                                                  HttpServletRequest request) {
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        //查询账户名，为后续转存调用
        wrapper.lambda()
                .eq(Student::getUsername, student.getUsername())
                .eq(Student::getPassword, SaSecureUtil.sha256(SaSecureUtil.md5(student.getPassword())));
        //查询出多个结果抛出异常
        Student one = studentService.getOne(wrapper, true);

        String verifyCode = (String) request.getSession().getAttribute("verifyCode");

        //忽略字母大小写比对
        boolean flag = verifyCode.equalsIgnoreCase(inputCode);

        log.info("验证码为: {}", verifyCode);
        log.info("用户输入: {}", inputCode);
        log.info("验证码校对: {}", flag);
        if (!flag) {
            return new R<>(20043, "验证码错误");
        }
        if (one == null) {
            log.info("账号或密码错误: {}", (Object) null);
            return new R<>(20042, "账号或密码错误,请检查登录信息");
        } else {
            StpUtil.login(one.getUsername());
            log.info("登录设备类型： {}", StpUtil.getLoginDevice());
            log.info("权限信息: {}", StpUtil.getTokenInfo());

            final ArrayList<HashMap<String, Object>> list = new ArrayList<>();

            final HashMap<String, Object> map = new HashMap<>();
            map.put("sa-token", StpUtil.getTokenInfo().getTokenValue());
            map.put("info", one);

            list.add(map);

            return new R<>(20041, "登录成功", list);
        }
    }


    /**
     * 信息员组长用户登录
     *
     * @param stuAdmin  信息员组长类对象(用户名、密码)
     * @param inputCode 图形验证码
     * @param request   请求体参数
     * @return 响应体封装类
     */
    @PostMapping("/stuAdmin/{inputCode}")
    public R<List<HashMap<String, Object>>> login(@RequestBody @Validated StuAdmin stuAdmin,
                                                  @PathVariable String inputCode,
                                                  HttpServletRequest request) {
        LambdaQueryWrapper<StuAdmin> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .eq(StuAdmin::getUsername, stuAdmin.getUsername())
                .eq(StuAdmin::getPassword, SaSecureUtil.sha256(SaSecureUtil.md5(stuAdmin.getPassword())));
        //查询出多个结果抛出异常
        StuAdmin stuAdmin1 = stuAdminService.getOne(wrapper, true);

        String verifyCode = (String) request.getSession().getAttribute("verifyCode");

        //忽略字母大小写比对
        boolean flag = verifyCode.equalsIgnoreCase(inputCode);

        log.info("验证码为: {}", verifyCode);
        log.info("用户输入: {}", inputCode);
        log.info("验证码校对: {}", flag);
        if (!flag) {
            return new R<>(20043, "验证码错误");
        }
        if (stuAdmin1 == null) {
            log.info("账号或密码错误: {}", (Object) null);
            return new R<>(20042, "账号或密码错误,请检查账号信息");
        } else {
            log.info("登录成功: {}", stuAdmin1);
            StpUtil.login(stuAdmin1.getUsername());

            final ArrayList<HashMap<String, Object>> arrayList = new ArrayList<>();

            final HashMap<String, Object> map = new HashMap<>();
            map.put("sa-token", StpUtil.getTokenInfo().getTokenValue());
            map.put("info", stuAdmin1);

            arrayList.add(map);

            return new R<>(20041, "登录成功", arrayList);
        }
    }

    /**
     * 系统管理员登录
     *
     * @param admin     管理员用户
     * @param inputCode 验证码
     * @param request   请求体参数
     * @return R
     */
    @PostMapping("/admin/{inputCode}")
    public R<List<HashMap<String, Object>>> login(@RequestBody @Validated Admin admin,
                                                  @PathVariable String inputCode,
                                                  HttpServletRequest request) {
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        //查询账户名，为后续转存调用
        wrapper.eq(Admin::getUsername, admin.getUsername())
                .eq(Admin::getPassword, SaSecureUtil.sha256(SaSecureUtil.md5(admin.getPassword())));
        //查询出多个结果抛出异常
        Admin one = adminService.getOne(wrapper, true);

        String verifyCode = (String) request.getSession().getAttribute("verifyCode");

        //忽略字母大小写比对
        boolean flag = verifyCode.equalsIgnoreCase(inputCode);

        log.info("验证码为: {}", verifyCode);
        log.info("用户输入: {}", inputCode);
        log.info("验证码校对: {}", flag);
        if (!flag) {
            return new R<>(20043, "验证码错误");
        }
        if (one == null) {
            log.info("账号或密码错误: {}", (Object) null);
            return new R<>(20042, "账号或密码错误,请检查登录信息");
        } else {
            StpUtil.login(one.getUsername());
            log.info("登录成功: {}", one);
            log.info("权限信息:{}", StpUtil.getTokenInfo());

            final ArrayList<HashMap<String, Object>> list = new ArrayList<>();

            final HashMap<String, Object> map = new HashMap<>();
            map.put("sa-token", StpUtil.getTokenInfo().getTokenValue());
            map.put("info", one);

            list.add(map);

            return new R<>(20041, "登录成功", list);
        }
    }

}
