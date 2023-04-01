package com.github.qiu121.config;

import cn.dev33.satoken.stp.StpInterface;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.qiu121.pojo.Student;
import com.github.qiu121.service.AdminService;
import com.github.qiu121.service.StudentService;
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
    private AdminService adminService;
    @Resource
    private StudentService studentService;
    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {

        LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Student::getPermissions)
                .eq(Student::getUsername, loginId.toString());
        Student one =studentService .getOne(wrapper);

        ArrayList<String> list = new ArrayList<>();
        list.add(one.getPermissions());
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

