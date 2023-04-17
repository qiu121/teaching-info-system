package com.github.qiu121.vo;

import com.github.qiu121.pojo.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/3/14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentVo implements Serializable {
    private static final long serialVersionUID = -5226463491045623482L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 用户名
     */
    private String username;
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

    public StudentVo(Student student) {
        BeanUtils.copyProperties(student, this);
    }

}
