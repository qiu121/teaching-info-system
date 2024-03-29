package com.github.qiu121.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/07/05
 * @description 信息员、信息员组长(学生类)DTO对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "学生信息员用户对象")
public class StudentDTO {

    private Long id;

    @NotBlank(message = "用户名不可为空")
    @Schema(description = "用户名")
    private String username;
    /**
     * 用户密码
     */
    @NotBlank(message = "密码不可为空")
    @Schema(description = "密码")
    private String password;
    /**
     * 信息员所在学院
     */
    @Schema(description = "学院")
    private String college;
    /**
     * 层次级别(本、专)
     */
    @Schema(description = "学历层次")
    private String educationLevel;
    /**
     * 入学年份(年级)
     */
    @Schema(description = "入学年份")
    private Integer enrollmentYear;
    /**
     * 信息员所在班级
     */
    @Schema(description = "所在班级")
    private String className;
    /**
     * 信息员姓名
     */
    @Schema(description = "姓名")
    private String name;

}
