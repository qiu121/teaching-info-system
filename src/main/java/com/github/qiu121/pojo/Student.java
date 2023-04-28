package com.github.qiu121.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/3/14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Student implements Serializable {
    private static final long serialVersionUID = -5226463491045623482L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不可为空")
    private String username;
    /**
     * 用户密码
     */
    @NotBlank(message = "密码不可为空")
    private String password;
    /**
     * 信息员所在学院
     */
    private String college;
    /**
     * 层次级别(本、专)
     */
    private String educationLevel;
    /**
     * 入学年份(年级)
     */
    private Integer enrollmentYear;
    /**
     * 信息员所在班级
     */
    private String className;
    /**
     * 信息员姓名
     */
    private String name;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
