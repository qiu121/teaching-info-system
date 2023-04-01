package com.github.qiu121.controller;

import com.github.qiu121.pojo.TeachInfo;
import com.github.qiu121.util.ExcelUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/3/22
 */
@RestController
@RequestMapping("/api/v1/excel")
public class ExcelController {
    @GetMapping("/download")
    public void testExcel(HttpServletResponse response) throws IOException {
        List<TeachInfo> list = new ArrayList<>();

        TeachInfo model = new TeachInfo();
        model.setCourseName("操作系统")
                .setTeacherName("邓田")
                .setSubmitPerson("admin")
                .setShouldArriveNum(49)
                .setActualArriveNum(49)
                .setClassLocation("至善楼3-110")
                //.setRecordTime("2023年3月15日一、二节")
                .setFeedbackGood("无迟到学生")
                .setFeedbackNotEnough("无")
                .setHopesAndSuggestions("无")
                .setCreateTime(LocalDateTime.now());
        System.out.println(LocalDateTime.now());

        list.add(model);

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + "test.xlsx");//或者文件名后缀为xlsx
        ExcelUtil.writeExcel(response, list);
    }

}
