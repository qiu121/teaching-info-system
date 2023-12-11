package com.github.qiu121.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.qiu121.common.R;
import com.github.qiu121.common.exception.BusinessException;
import com.github.qiu121.common.exception.DuplicateException;
import com.github.qiu121.entity.TeachInfo;
import com.github.qiu121.service.TeachInfoService;
import com.github.qiu121.util.RedisClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.github.qiu121.util.Constant.ADMIN_TEACH_INFO_LIST;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/3/14
 * @description 教学信息记录(信息员)
 */
@Slf4j
@RestController
@RequestMapping("/feedback/stu")
@Tag(name = "信息员提交反馈信息操作接口")
public class TeachInfoController {
    @Resource
    private TeachInfoService teachInfoService;
    @Resource
    RedisClient<IPage<TeachInfo>> redisClient;

    /**
     * 教学信息反馈提交(信息员)
     *
     * @param teachInfo 教学信息对象
     * @return R
     */
    @PostMapping("/add")
    @SaCheckRole("stu")
    @Operation(description = "提交反馈信息", summary = "提交")
    public R<String> addTeachInfo(@RequestBody @Validated TeachInfo teachInfo) {
        teachInfoValidate(teachInfo);

        boolean flag = teachInfoService.save(teachInfo);
        return flag ? new R<>(20011, "提交成功") :
                new R<>(20012, "提交失败");
    }

    /**
     * 动态条件查询所有教学反馈信息(信息员提交)
     *
     * @param teachInfo 提交数据对象(教师姓名、课程名、上课地点)
     * @return 集合对象
     */
    @PostMapping("/listAll/{currentNum}/{pageSize}")
    @SaCheckRole("admin")
    @Operation(description = "分页查询所有反馈信息", summary = "分页查询")
    public R<IPage<TeachInfo>> listAllTeachInfo(@RequestBody TeachInfo teachInfo,
                                                @PathVariable long currentNum,
                                                @PathVariable long pageSize) throws JsonProcessingException {
        log.info(String.valueOf(teachInfo));
        // 数据缓存处理
        TypeReference<IPage<TeachInfo>> typeReference = new TypeReference<IPage<TeachInfo>>() {
        };
        IPage<TeachInfo> iPage = redisClient.get(ADMIN_TEACH_INFO_LIST, typeReference);
        if (iPage != null) {
            return new R<>(20040, "查询完成", iPage);
        }

        QueryWrapper<TeachInfo> wrapper = new QueryWrapper<>();
        if (teachInfo != null) {
            wrapper.select("*").lambda()
                    .like(StringUtils.isNotBlank(teachInfo.getSubmitPersonCollege()), TeachInfo::getSubmitPersonCollege, teachInfo.getSubmitPersonCollege())

                    .like(StringUtils.isNotBlank(teachInfo.getTeacherName()), TeachInfo::getTeacherName, teachInfo.getTeacherName())
                    .like(StringUtils.isNotBlank(teachInfo.getCourseName()), TeachInfo::getCourseName, teachInfo.getCourseName())
                    .like(StringUtils.isNotBlank(teachInfo.getClassLocation()), TeachInfo::getClassLocation, teachInfo.getClassLocation());
        }

        iPage = teachInfoService.page(new Page<>(currentNum, pageSize), wrapper);
        redisClient.set(ADMIN_TEACH_INFO_LIST, iPage, 1L, TimeUnit.MINUTES);
        return new R<>(20040, "查询完成", iPage);
    }

    /**
     * 列出当前用户的提交历史
     *
     * @param username 信息员用户
     * @return 封装集合
     */
    @GetMapping("/list/{username}")
    @SaCheckRole("stu")
    @Operation(description = "列出当前用户的提交记录", summary = "查询当前")
    public R<List<TeachInfo>> listTeachInfo(@PathVariable String username) {
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
     * @return R<TeachInfo>
     */
    @GetMapping("/get/{id}")
    @SaCheckRole("stu")
    @Operation(description = "查询反馈信息", summary = "查询")
    public R<TeachInfo> getTeachInfo(@PathVariable Long id) {
        TeachInfo info = teachInfoService.getById(id);
        return new R<>(20040, "查询完成", info);
    }

    /**
     * 根据id删除
     *
     * @param id 教学信息记录id
     * @return R<Boolean>
     */
    @DeleteMapping("/remove/{id}")
    @SaCheckRole(value = {"stu", "admin"}, mode = SaMode.OR)
    @Operation(description = "删除反馈信息", summary = "删除")
    public R<Boolean> removeTeachInfo(@PathVariable Long id) {
        final boolean remove = teachInfoService.removeById(id);
        log.info("删除完成：{}", remove);
        return remove ? new R<>(20021, "删除成功") :
                new R<>(20022, "删除失败,该数据不存在!");// 好像只有多线程下才会出现(multithreading)
    }

    /**
     * 批量删除 提交的教学信息(信息员)
     *
     * @param idList id集合
     * @return R
     */
    @DeleteMapping("/removeBatch/{idList}")
    @Operation(description = "批量删除反馈信息", summary = "批量删除")
    @SaCheckRole(value = {"stu", "admin"}, mode = SaMode.OR)
    public R<Boolean> removeBatchTeachInfo(@PathVariable Long[] idList) {
        final boolean remove = teachInfoService.removeByIds(Arrays.asList(idList));
        log.info("批量删除完成：{}", remove);
        log.info("删除数据: {}", (Object) idList);
        return remove ? new R<>(20021, "删除成功") :
                new R<>(20022, "删除失败!对应数据不存在或已被删除");
    }

    /**
     * 修改教学信息反馈
     *
     * @param teachInfo 教学信息对象(学生提交)--必须包含id
     * @return R<Boolean>
     */
    @PutMapping("/update")
    @SaCheckRole("stu")
    @Operation(description = "修改反馈信息", summary = "修改")
    public R<?> updateTeachInfo(@RequestBody @Validated TeachInfo teachInfo) {

        teachInfoValidate(teachInfo);

        final LambdaUpdateWrapper<TeachInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(TeachInfo::getId, teachInfo.getId())
                .set(TeachInfo::getCourseName, teachInfo.getCourseName())// 课程名称
                .set(TeachInfo::getTeacherName, teachInfo.getTeacherName())// 教师姓名
                .set(TeachInfo::getShouldArriveNum, teachInfo.getShouldArriveNum())// 实到人数
                .set(TeachInfo::getActualArriveNum, teachInfo.getActualArriveNum())// 应到人数

                .set(TeachInfo::getClassLocation, teachInfo.getClassLocation())// 上课地点
                .set(TeachInfo::getRecordClassDate, teachInfo.getRecordClassDate())// 记录时间-日期
                .set(TeachInfo::getRecordCourseNum, teachInfo.getRecordCourseNum())// 记录时间-上课节次
                .set(TeachInfo::getFeedbackGood, teachInfo.getFeedbackGood())// 信息反馈，好的方面
                .set(TeachInfo::getFeedbackNotEnough, teachInfo.getFeedbackNotEnough())// 信息反馈，不足之处
                .set(TeachInfo::getHopesAndSuggestions, teachInfo.getHopesAndSuggestions());// 希望和建议


        boolean update = teachInfoService.update(wrapper);
        log.info("修改完成：{}", update);
        return update ? new R<>(20031, "修改成功") :
                new R<>(20032, "修改失败");
    }

    /**
     * 数据校验提取方法
     *
     * @param teachInfo 教学信息对象
     */
    private void teachInfoValidate(@Validated @RequestBody TeachInfo teachInfo) {
        if (teachInfo.getActualArriveNum() > teachInfo.getShouldArriveNum()) {
            throw new BusinessException("实到人数不可 大于 应到人数");
        }

        final List<TeachInfo> list = teachInfoService.list();
        if (list.contains(teachInfo)) {
            log.info("是否为同一条反馈信息(信息员): {}", list.contains(teachInfo));
            throw new DuplicateException("记录重复，请勿重复操作");
        }
    }

}
