package com.github.qiu121.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.github.qiu121.entity.TeachInfo;
import com.github.qiu121.entity.TeachInfo2;
import com.github.qiu121.util.ExcelUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/3/22
 */
@RestController
@Slf4j
@SaCheckRole("admin")
@RequestMapping("/api/v1/excel")
@Tag(name = "Excel操作接口")
public class ExcelController {
    @PostMapping("/teachInfo")
    @Operation(description = "导出信息员提交信息", summary = "信息员反馈信息")
    public void exportTeachInfo(HttpServletResponse response, @RequestBody List<TeachInfo> teachInfoList) throws IOException {

        List<TeachInfo> list = new ArrayList<>(teachInfoList);

        response.setContentType("application/vnd.ms-excel");
        // response.setHeader("Content-Disposition", "attachment;filename=" + "test.xlsx");//或者文件名后缀为xlsx
        ExcelUtil.writeExcel(response, list, TeachInfo.class);
    }

    @PostMapping("/teachInfo2")
    @Operation(description = "导出组长提交信息", summary = "组长反馈信息")
    public void exportTeachInfo2(HttpServletResponse response, @RequestBody List<TeachInfo2> teachInfoList2) throws IOException {

        List<TeachInfo2> list = new ArrayList<>(teachInfoList2);

        response.setContentType("application/vnd.ms-excel");
        ExcelUtil.writeExcel(response, list, TeachInfo2.class);
    }

}
