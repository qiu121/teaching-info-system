package com.github.qiu121.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.qiu121.common.R;
import com.github.qiu121.pojo.TeachInfo;
import com.github.qiu121.service.StudentService;
import com.github.qiu121.service.TeachInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/3/14
 */
@Slf4j
@RestController
@RequestMapping("/feedback")
public class TeachInfoController {
    @Resource
    private TeachInfoService teachInfoService;
    @Resource
    private StudentService studentService;

    /**
     * 教学信息反馈提交
     *
     * @param teachInfo 教学信息对象
     * @return R
     */

    @PostMapping("/add")
    public R<String> submit(@RequestBody TeachInfo teachInfo) {
        boolean flag = teachInfoService.save(teachInfo);
        R<String> r = new R<>();
        return flag ? (r.setCode(20011).setMsg("提交成功"))
                : (r.setCode(20012).setMsg("提交失败"));
    }

    /**
     * 动态条件查询所有教学反馈信息
     *
     * @param teachInfo 提交数据对象(教师姓名、课程名、上课地点)
     * @return 集合对象
     */
    @PostMapping("/selectAll/admin/{currentNum}/{pageSize}")
    public R<IPage<TeachInfo>> selectList(@RequestBody @Validated TeachInfo teachInfo,
                                          @PathVariable long currentNum, @PathVariable long pageSize) {

        IPage<TeachInfo> page = new Page<>(currentNum, pageSize);

        QueryWrapper<TeachInfo> wrapper = new QueryWrapper<>();
        wrapper.select("*").lambda()
                .like(StringUtils.isNotBlank(teachInfo.getTeacherName()), TeachInfo::getTeacherName, teachInfo.getTeacherName())
                .like(StringUtils.isNotBlank(teachInfo.getCourseName()), TeachInfo::getCourseName, teachInfo.getCourseName())
                .like(StringUtils.isNotBlank(teachInfo.getClassLocation()), TeachInfo::getClassLocation, teachInfo.getClassLocation());
        IPage<TeachInfo> iPage = teachInfoService.page(page, wrapper);
        return new R<>(20040, "查询完成", iPage);

    }

    /**
     * 列出当前用户的提交历史
     *
     * @param username 信息员用户
     * @return 封装集合
     */
    @GetMapping("/list/stu/{username}")
    public R<List<TeachInfo>> list(@PathVariable String username) {
        LambdaQueryWrapper<TeachInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TeachInfo::getSubmitPerson, username);

        List<TeachInfo> list = teachInfoService.list(wrapper);
        log.info("查询结果为:{}", list);
        return new R<>(20040, "查询完成", list);

    }

    /**
     * 根据id查询(编辑预填)
     *
     * @param id 教学信息记录id
     * @return TeachInfo
     */
    @GetMapping("/edit/stu/{id}")
    public R<TeachInfo> editTeachInfo(@PathVariable Integer id) {
        TeachInfo info = teachInfoService.getById(id);
        return new R<>(20041, "查询成功", info);
    }

    /**
     * 根据id删除
     *
     * @param id 教学信息记录id
     * @return R<Boolean>
     */

    @DeleteMapping("/remove/stu/{id}")
    public R<Boolean> removeTeachInfo(@PathVariable Integer id) {
        final boolean remove = teachInfoService.removeById(id);
        log.info("删除完成：{}", remove);
        return remove ? new R<>(20021, "删除成功", true) :
                new R<>(20022, "删除失败", false);
    }

    /**
     * 修改教学信息反馈
     *
     * @param teachInfo 教学信息对象(必须包含id)
     * @return R<Boolean>
     */
    @PutMapping("/update/stu")
    public R<Boolean> updateTeachInfo(@RequestBody TeachInfo teachInfo) {
        boolean update = teachInfoService.updateById(teachInfo);
        log.info("修改完成：{}", update);
        return update ? new R<>(20031, "修改成功", true) :
                new R<>(20032, "修改失败", false);
    }

}
