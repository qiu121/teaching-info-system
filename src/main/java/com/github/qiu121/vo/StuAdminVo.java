package com.github.qiu121.vo;

import com.github.qiu121.pojo.StuAdmin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/04/16
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class StuAdminVo implements Serializable {
    private static final long serialVersionUID = -1715396131537477L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 学院
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
     * 所在班级
     */
    private String className;
    /**
     * 姓名
     */
    private String name;

    public StuAdminVo(StuAdmin stuAdmin) {
        BeanUtils.copyProperties(stuAdmin, this);
    }

}
