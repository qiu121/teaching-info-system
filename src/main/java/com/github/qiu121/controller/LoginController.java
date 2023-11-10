package com.github.qiu121.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.qiu121.common.R;
import com.github.qiu121.dto.LoginDTO;
import com.github.qiu121.entity.Admin;
import com.github.qiu121.entity.StuAdmin;
import com.github.qiu121.entity.Student;
import com.github.qiu121.service.AdminService;
import com.github.qiu121.service.StuAdminService;
import com.github.qiu121.service.StudentService;
import com.github.qiu121.util.SecureUtil;
import com.github.qiu121.vo.AdminVo;
import com.github.qiu121.vo.StuAdminVo;
import com.github.qiu121.vo.StudentVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
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
@Tag(name = "用户登入接口")
public class LoginController {
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
     * @param session   session参数
     * @return 响应体封装类
     */
    @PostMapping("/stu/{inputCode}")
    @Operation(description = "学生信息员用户登入", summary = "信息员用户登入")
    public R<List<HashMap<String, Object>>> studentLogin(@RequestBody @Validated LoginDTO student,
                                                         @PathVariable String inputCode,
                                                         HttpSession session) {
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        // 查询账户名，为后续转存调用
        wrapper.lambda()
                .eq(Student::getUsername, student.getUsername())
                .eq(Student::getPassword, SecureUtil.encrypt(student.getPassword()));
        // 查询出多个结果抛出异常
        Student one = studentService.getOne(wrapper, true);

        String verifyCode = (String) session.getAttribute("verifyCode");

        // 忽略字母大小写比对
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
        }
        StpUtil.login(one.getUsername());
        log.info("登录设备类型： {}", StpUtil.getLoginDevice());
        log.info("权限信息: {}", StpUtil.getTokenInfo());

        // 将数据用VO对象重新赋值，响应返回
        final StudentVo studentVo = new StudentVo(one);

        final ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        final HashMap<String, Object> map = new HashMap<>();
        map.put("sa-token", StpUtil.getTokenInfo().getTokenValue());
        map.put("info", studentVo);

        list.add(map);
        return new R<>(20041, "登录成功", list);

    }


    /**
     * 信息员组长用户登录
     *
     * @param stuAdmin  信息员组长类对象(用户名、密码)
     * @param inputCode 图形验证码
     * @param session   session参数
     * @return 响应体封装类
     */
    @PostMapping("/stuAdmin/{inputCode}")
    @Operation(description = "信息员组长用户登入", summary = "组长用户登入")
    public R<List<HashMap<String, Object>>> studentAdminLogin(@RequestBody @Validated LoginDTO stuAdmin,
                                                              @PathVariable String inputCode,
                                                              HttpSession session) {
        LambdaQueryWrapper<StuAdmin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StuAdmin::getUsername, stuAdmin.getUsername())// 哈希算法加密
                .eq(StuAdmin::getPassword, SecureUtil.encrypt(stuAdmin.getPassword()));
        // 查询出多个结果抛出异常
        StuAdmin stuAdminOne = stuAdminService.getOne(wrapper, true);

        String verifyCode = (String) session.getAttribute("verifyCode");

        // 忽略字母大小写比对
        boolean flag = verifyCode.equalsIgnoreCase(inputCode);

        log.info("验证码为: {}", verifyCode);
        log.info("用户输入: {}", inputCode);
        log.info("验证码校对: {}", flag);
        if (!flag) {
            return new R<>(20043, "验证码错误");
        }
        if (stuAdminOne == null) {
            log.info("账号或密码错误: {}", (Object) null);
            return new R<>(20042, "账号或密码错误,请检查账号信息");
        }
        log.info("登录成功: {}", stuAdminOne);
        StpUtil.login(stuAdminOne.getUsername());

        final ArrayList<HashMap<String, Object>> arrayList = new ArrayList<>();

        final HashMap<String, Object> map = new HashMap<>();
        map.put("sa-token", StpUtil.getTokenInfo().getTokenValue());
        map.put("info", new StuAdminVo(stuAdminOne));

        arrayList.add(map);

        return new R<>(20041, "登录成功", arrayList);


    }

    /**
     * 系统管理员登录
     *
     * @param admin     管理员用户
     * @param inputCode 验证码
     * @param session   session参数
     * @return R
     */
    @PostMapping("/admin/{inputCode}")
    @Operation(description = "系统管理员用户登入", summary = "管理员登入")
    public R<List<HashMap<String, Object>>> administratorLogin(@RequestBody @Validated LoginDTO admin,
                                                               @PathVariable String inputCode,
                                                               HttpSession session) {
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        // 查询账户名，为后续转存调用
        wrapper.eq(Admin::getUsername, admin.getUsername())
                .eq(Admin::getPassword, SecureUtil.encrypt(admin.getPassword()));
        // 查询出多个结果抛出异常
        Admin adminOne = adminService.getOne(wrapper, true);

        String verifyCode = (String) session.getAttribute("verifyCode");

        // 忽略字母大小写比对
        boolean flag = verifyCode.equalsIgnoreCase(inputCode);

        log.info("验证码为: {}", verifyCode);
        log.info("用户输入: {}", inputCode);
        log.info("验证码校对: {}", flag);
        if (!flag) {
            return new R<>(20043, "验证码错误");
        }
        if (adminOne == null) {
            log.info("账号或密码错误: {}", (Object) null);
            return new R<>(20042, "账号或密码错误,请检查登录信息");
        }
        StpUtil.login(adminOne.getUsername());
        log.info("登录成功: {}", adminOne);
        log.info("权限信息:{}", StpUtil.getTokenInfo());

        final ArrayList<HashMap<String, Object>> list = new ArrayList<>();

        final HashMap<String, Object> map = new HashMap<>();
        map.put("sa-token", StpUtil.getTokenInfo().getTokenValue());
        map.put("info", new AdminVo(adminOne));

        list.add(map);
        return new R<>(20041, "登录成功", list);

    }

}
