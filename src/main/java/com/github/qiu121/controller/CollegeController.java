package com.github.qiu121.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import com.github.qiu121.common.R;
import com.github.qiu121.common.exception.BusinessException;
import com.github.qiu121.dto.CollegeDTO;
import com.github.qiu121.entity.College;
import com.github.qiu121.service.CollegeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/04/18
 * @description 操作学院数据
 */
@Slf4j
@RestController
@SaCheckRole(value = {"stuAdmin", "admin"}, mode = SaMode.OR)
@RequestMapping("/colleges")
@Tag(name = "学院操作接口")
public class CollegeController {
    @Resource
    private CollegeService collegeService;

    private void collegeValidate(CollegeDTO college) {
        // final List<College> collegeList = collegeService.list();

        final List<CollegeDTO> collegeDTOList = collegeService.list().stream()
                .map(c -> {
                    CollegeDTO collegeDTO = new CollegeDTO();
                    // 将college对象的属性赋值给collegeDTO对象
                    collegeDTO.setName(c.getName());
                    return collegeDTO;
                }).collect(Collectors.toList());

        if (collegeDTOList.contains(college)) {
            log.info("新添加的学院记录是否重复: {}", collegeDTOList.contains(college));
            throw new BusinessException("该条数据已存在,请勿重复添加");
        }
    }

    @PostMapping("/add")
    @Operation(description = "新增学院信息", summary = "新增")
    public R<Boolean> addCollege(@RequestBody @Validated CollegeDTO collegeDTO) {
        collegeValidate(collegeDTO);

        College college = new College(collegeDTO);
        final boolean saved = collegeService.save(college);
        return saved ? new R<>(20011, "添加完成", true) :
                new R<>(20012, "添加失败", false);
    }

    @DeleteMapping("/remove/{id}")
    @Operation(description = "删除学院信息", summary = "删除")
    public R<Boolean> removeCollege(@PathVariable Long id) {
        final boolean removed = collegeService.removeById(id);
        return removed ? new R<>(20021, "删除完成", true) :
                new R<>(20022, "删除失败", false);
    }

    @PutMapping("/update")
    @Operation(description = "修改学院信息", summary = "修改")
    @Deprecated
    public R<Boolean> updateCollege(@RequestBody @Validated CollegeDTO collegeDTO) {
        collegeValidate(collegeDTO);

        College college = new College(collegeDTO);
        final boolean updated = collegeService.updateById(college);
        return updated ? new R<>(20031, "修改完成", true) :
                new R<>(20032, "修改失败", false);
    }

    @GetMapping("/list")
    @Operation(description = "列出所有学院信息", summary = "查询所有")
    public R<List<College>> listCollege() {
        final List<College> collegeList = collegeService.list();
        return new R<>(20040, "查询完成", collegeList);

    }
}
