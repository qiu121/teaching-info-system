package com.github.qiu121.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.qiu121.common.R;
import com.github.qiu121.common.enumeration.EducationLevelEnum;
import com.github.qiu121.common.exception.DuplicateException;
import com.github.qiu121.common.exception.NotFoundException;
import com.github.qiu121.dto.RequestBodyData;
import com.github.qiu121.entity.StuAdmin;
import com.github.qiu121.entity.Student;
import com.github.qiu121.entity.TeachInfo;
import com.github.qiu121.entity.TeachInfo2;
import com.github.qiu121.service.StudentService;
import com.github.qiu121.service.TeachInfoService;
import com.github.qiu121.service.TeachInfoService2;
import com.github.qiu121.util.CalculateGradeUtil;
import com.github.qiu121.util.RedisClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.github.qiu121.util.Constant.ADMIN_TEACH_INFO_LIST2;
import static com.github.qiu121.util.Constant.STU_ADMIN_TEACH_INFO_LIST;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/4/10
 * @description 教学信息记录(信息员组长)
 */
@Slf4j
@RestController
@RequestMapping("/feedback/stuAdmin")
@Tag(name = "组长提交反馈信息操作接口")
public class TeachInfoController2 {
    @Resource
    private TeachInfoService teachInfoService;
    @Resource
    private TeachInfoService2 teachInfoService2;
    @Resource
    private StudentService studentService;
    @Resource
    private RedisClient<IPage<TeachInfo2>> teachInfo2RedisClient;
    @Resource
    private RedisClient<Page<TeachInfo>> teachInfoRedisClient;

    /**
     * 信息员组长提交
     *
     * @param teachInfo2 教学信息对象
     * @return R
     */
    @PostMapping("/add")
    @SaCheckRole("stuAdmin")
    @Operation(description = "组长提交反馈信息", summary = "组长提交")
    public R<String> addTeachInfo2(@RequestBody @Validated TeachInfo2 teachInfo2) {
        teacherInfoValidate2(teachInfo2);

        final boolean save = teachInfoService2.save(teachInfo2);
        return save ? new R<>(20011, "提交成功") :
                new R<>(20012, "提交失败");
    }

    /**
     * 根据 id查询 教学信息(组长提交)
     *
     * @param id 记录主键
     * @return R
     */
    @GetMapping("/get/{id}")
    @SaCheckRole("stuAdmin")
    @Operation(description = "查询反馈信息", summary = "查询")
    public R<TeachInfo2> getTeachInfo2(@PathVariable Long id) {
        final TeachInfo2 teachInfo2 = teachInfoService2.getById(id);
        return new R<>(20040, "查询完成", teachInfo2);
    }

    /**
     * 根据 id删除
     *
     * @param id 记录主键
     * @return R 封装响应对象
     */
    @DeleteMapping("/remove/{id}")
    @SaCheckRole(value = {"stuAdmin", "admin"}, mode = SaMode.OR)
    @Operation(description = "删除反馈信息", summary = "删除")
    public R<String> removeTeachInfo2(@PathVariable Long id) {
        final boolean removed = teachInfoService2.removeById(id);
        return removed ?
                new R<>(20021, "删除完成") :
                new R<>(20022, "删除失败，该记录不存在或已删除");

    }

    /**
     * 根据 id数组，批量删除
     *
     * @param idArray 主键数组
     * @return R
     */
    @DeleteMapping("/removeBatch/{idArray}")
    @SaCheckRole(value = {"stuAdmin", "admin"}, mode = SaMode.OR)
    @Operation(description = "批量删除反馈信息", summary = "批量删除")
    public R<String> removeBatchTeachInfo2(@PathVariable Long[] idArray) {
        final boolean batchRemoved = teachInfoService2.removeBatchByIds(Arrays.asList(idArray));
        log.info("批量删除完成：{}", batchRemoved);
        log.info("删除数据: {}", (Object) idArray);
        return batchRemoved ?
                new R<>(20021, "删除成功") :
                new R<>(20022, "删除失败,记录不存在或已删除");
    }

    /**
     * 修改教学信息(组长提交)
     *
     * @param teachInfo2 教学信息对象(组长提交)
     * @return R
     */
    @PutMapping("/update")
    @SaCheckRole("stuAdmin")
    @Operation(description = "修改反馈信息", summary = "修改")
    public R<String> updateTeachInfo2(@RequestBody @Validated TeachInfo2 teachInfo2) {

        teacherInfoValidate2(teachInfo2);

        final LambdaUpdateWrapper<TeachInfo2> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(TeachInfo2::getId, teachInfo2.getId())
                .set(TeachInfo2::getFeedbackGood, teachInfo2.getFeedbackGood())
                .set(TeachInfo2::getFeedbackNotEnough, teachInfo2.getFeedbackNotEnough())
                .set(TeachInfo2::getHopesAndSuggestions, teachInfo2.getHopesAndSuggestions());
        final boolean updateFlag = teachInfoService2.update(wrapper);

        return updateFlag ? new R<>(20031, "修改完成") :
                new R<>(20032, "修改失败");
    }

    /**
     * 数据记录重复验证，提取方法
     *
     * @param teachInfo 教学信息(组长提交)
     */
    private void teacherInfoValidate2(TeachInfo2 teachInfo) {

        List<TeachInfo2> list = teachInfoService2.list();
        if (list.contains(teachInfo)) {
            log.info("是否为同一条反馈信息(组长): {}", list.contains(teachInfo));
            throw new DuplicateException("该条数据已存在,请勿重复添加");
        }
    }

    /**
     * 动态条件分页查询信息员提交的数据,(该业务实现为此项目最复杂，没有之一)
     * eg:
     * 本科一(A2022)  -> 本科一(A2022)、专科一(B2022)
     * 本科二(A2021)  -> 本科二(A2021)、专科二(B2021)
     * 本科三(A2020)  -> 本科三(A2020)、专升本(S2022)
     * <p>
     * 根据阿里开发手册，不推荐使用多表查询
     *
     * @param requestBodyData 信息员组长对象、教学信息提交(信息员)
     * @param currentNum      当前页号
     * @param pageSize        每页条数
     * @return R
     */
    @PostMapping("/selectAllByPermission/{currentNum}/{pageSize}")
    @SaCheckRole("stuAdmin")
    @Operation(description = "分页查询特定反馈信息", summary = "分页查询特定")
    public R<IPage<TeachInfo>> selectList(@RequestBody RequestBodyData requestBodyData,
                                          @PathVariable long currentNum,
                                          @PathVariable long pageSize) throws JsonProcessingException {

        final StuAdmin stuAdmin = requestBodyData.getStuAdmin();
        final TeachInfo teachInfo = requestBodyData.getTeachInfo();

        // 通过提交信息,查询信息员用户
        final LambdaQueryWrapper<TeachInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(TeachInfo::getSubmitPerson)
                .eq(TeachInfo::getSubmitPersonCollege, stuAdmin.getCollege());// 筛查同学院提交的信息

//        final ArrayList<String> submitPersonUsernameList = new ArrayList<>();
//        for (TeachInfo info : teachInfoService.list(wrapper)) {
//            submitPersonUsernameList.add(info.getSubmitPerson());
//        }

        // Stream API重构上述代码
        final List<String> submitPersonUsernameList = teachInfoService.list(wrapper)
                .stream()
                .map(TeachInfo::getSubmitPerson)
                .collect(Collectors.toList());


        // 通过信息员用户名，查询信息员信息
        if (CollectionUtils.isEmpty(submitPersonUsernameList)) {
            // 按照提交人学院查询，如果没有查询到提交的信息只可能是  -> 没有信息
            throw new NotFoundException("查询完成，当前数据为空");
        }
        final LambdaQueryWrapper<Student> studentWrapper = new LambdaQueryWrapper<>();
        studentWrapper.select(Student::getEnrollmentYear, Student::getEducationLevel)
                .in(Student::getUsername, submitPersonUsernameList);
        final List<Student> studentList = studentService.list(studentWrapper);


        // TODO 中间过程情况查询为空的解决，会出现断链(单表查询嵌套问题)
        // 能从提交信息里查询出提交人账户，但在信息员用户无法查询出 上述提交人账户；即存在这个账号的提交,账户表没有这个账号！！
        if (CollectionUtils.isEmpty(studentList)) {
            throw new NotFoundException("查询完成，当前数据为空");
        }
        // 筛查年级、学历层次符合条件的信息员对象(AI:sob:😭)
        LambdaQueryWrapper<Student> mainWrapper = new LambdaQueryWrapper<>();
        mainWrapper.select(Student::getUsername)
                .eq(Student::getCollege, stuAdmin.getCollege());
        mainWrapper.nested(qw -> {
            for (Student student : studentList) {
                qw.or(wq -> {
                    // 专升本
                    if (Objects.equals(student.getEducationLevel(), EducationLevelEnum.COLLEGE_TO_UNDER_GRADUATE.getName())
                            && CalculateGradeUtil.calculate(stuAdmin.getEnrollmentYear()) == 3) {
                        wq.eq(Student::getEnrollmentYear, stuAdmin.getEnrollmentYear() + 2);
                    } else { // 本科、专科
                        wq.eq(Student::getEnrollmentYear, stuAdmin.getEnrollmentYear());
                    }
                });
            }
        });

        // 将满足条件的信息员对象，用新的集合处理信息员对象用户名集合
//        final ArrayList<String> studentNameList = new ArrayList<>();
//        for (Student student : studentService.list(mainWrapper)) {
//            studentNameList.add(student.getUsername());
//        }
        // Stream API
        final List<String> studentNameList = studentService.list(mainWrapper)
                .stream()
                .map(Student::getUsername)
                .collect(Collectors.toList());


        // 通过满足条件的信息员的用户名，查询提交信息
        final LambdaQueryWrapper<TeachInfo> teachInfoWrapper = new LambdaQueryWrapper<>();

        // 判空，不为空，则加入条件构造；为空条件无效，查询所有
        teachInfoWrapper.in(!CollectionUtils.isEmpty(studentNameList), TeachInfo::getSubmitPerson, studentNameList);

        // 可以同通过，教师姓名、课程名、上课地点筛查
        teachInfoWrapper.like(StringUtils.isNotBlank(teachInfo.getSubmitPersonCollege()), TeachInfo::getSubmitPersonCollege, teachInfo.getSubmitPersonCollege())
                .like(StringUtils.isNotBlank(teachInfo.getTeacherName()), TeachInfo::getTeacherName, teachInfo.getTeacherName())
                .like(StringUtils.isNotBlank(teachInfo.getCourseName()), TeachInfo::getCourseName, teachInfo.getCourseName())
                .like(StringUtils.isNotBlank(teachInfo.getClassLocation()), TeachInfo::getClassLocation, teachInfo.getClassLocation());

        // 访问Redis缓存数据，如果存在，直接返回；如果不存在，访问数据库并将数据同步到Redis缓存
        TypeReference<Page<TeachInfo>> typeReference = new TypeReference<Page<TeachInfo>>() {
        };
        Page<TeachInfo> infoPage = teachInfoRedisClient.get(STU_ADMIN_TEACH_INFO_LIST, typeReference);
        if (infoPage != null) {
            return new R<>(20040, "查询完成", infoPage);
        }

        infoPage = teachInfoService.page(new Page<>(currentNum, pageSize), teachInfoWrapper);
        teachInfoRedisClient.set(STU_ADMIN_TEACH_INFO_LIST, infoPage, 1L, TimeUnit.MINUTES);
        if (CollectionUtils.isEmpty(studentNameList)) {
            infoPage = null;
        }
        // code==20040 && data ！= null ,
        return new R<>(20040, "查询完成", infoPage);

    }

    /**
     * 列出，提交记录(信息员组长)
     *
     * @param username 信息员组长用户名
     * @return R
     */
    @GetMapping("/list/{username}")
    @SaCheckRole("stuAdmin")
    @Operation(description = "列出当前组长用户的提交记录", summary = "查询当前")
    public R<List<TeachInfo2>> listTeachInfo2(@PathVariable String username) {
        final LambdaQueryWrapper<TeachInfo2> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TeachInfo2::getSubmitPerson, username);

        log.info("查询结果为:{}", teachInfoService2.list(wrapper));
        return new R<>(20040, "查询完成", teachInfoService2.list(wrapper));

    }

    /**
     * 动态条件查询教学提交信息(组长)
     *
     * @param teachInfo2 教学信息对象(组长)--提交人学院、班级、姓名
     * @param currentNum 当前页
     * @param pageSize   每页条数
     * @return R
     */
    @PostMapping("/listAll/{currentNum}/{pageSize}")
    @SaCheckRole("admin")
    @Operation(description = "分页查询所有反馈信息", summary = "分页查询")
    public R<IPage<TeachInfo2>> listAll(@RequestBody TeachInfo2 teachInfo2,
                                        @PathVariable long currentNum,
                                        @PathVariable long pageSize) throws JsonProcessingException {
        TypeReference<IPage<TeachInfo2>> typeReference = new TypeReference<IPage<TeachInfo2>>() {
        };
        IPage<TeachInfo2> iPage = teachInfo2RedisClient.get(ADMIN_TEACH_INFO_LIST2, typeReference);
        if (iPage != null) {
            return new R<>(20040, "查询完成", iPage);
        }

        LambdaQueryWrapper<TeachInfo2> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(teachInfo2.getSubmitPersonCollege()),
                        TeachInfo2::getSubmitPersonCollege, teachInfo2.getSubmitPersonCollege())
                .like(StringUtils.isNotBlank(teachInfo2.getSubmitPersonName()),
                        TeachInfo2::getSubmitPersonName, teachInfo2.getSubmitPersonName())
                .like(StringUtils.isNotBlank(teachInfo2.getSubmitPersonClass()),
                        TeachInfo2::getSubmitPersonClass, teachInfo2.getSubmitPersonClass());

        iPage = teachInfoService2.page(new Page<>(currentNum, pageSize), wrapper);
        teachInfo2RedisClient.set(ADMIN_TEACH_INFO_LIST2, iPage, 1L, TimeUnit.MINUTES);
        return new R<>(20040, "查询完成", iPage);
    }
}
