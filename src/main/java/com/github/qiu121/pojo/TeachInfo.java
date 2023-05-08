package com.github.qiu121.pojo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/3/14
 * <p>
 * 教学信息记录表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ExcelIgnoreUnannotated
@ColumnWidth(15)
@ContentRowHeight(30)
public class TeachInfo implements Serializable {
    private static final long serialVersionUID = 2689684323939293112L;

    /**
     * 主键
     */
    private Long id;
    /**
     * 信息员账号
     */
    @ExcelProperty("信息员")
    private String submitPerson;
    /**
     * 信息员姓名
     */
    @ExcelProperty("姓名")
    private String submitPersonName;
    /**
     * 信息员所在学院
     */
    @ExcelProperty("学院")
    private String submitPersonCollege;
    /**
     * 信息员所在班级
     */
    @ExcelProperty("班级")
    private String submitPersonClass;
    /**
     * 课程名称
     */
    @ExcelProperty("课程名称")
    @NotBlank(message = "课程名称未填写")
    private String courseName;
    /**
     * 教师姓名
     */
    @ExcelProperty("教师姓名")
    @NotBlank(message = "教师姓名未填写")
    private String teacherName;
    /**
     * 应到人数
     */
    @Positive
    @ExcelProperty("应到人数")
    private Integer shouldArriveNum;
    /**
     * 实到人数
     */
    @Positive
    @ExcelProperty("实到人数")
    private Integer actualArriveNum;
    /**
     * 上课地点
     */
    @ExcelProperty("上课地点")
    @NotBlank(message = "上课地点未填写")
    private String classLocation;
    /**
     * 记录时间-日期
     */
    @ExcelProperty({"记录时间", "上课日期"})
    private LocalDate recordClassDate;

    /**
     * 记录时间-上课节次
     */
    @ExcelProperty({"记录时间", "节次"})
    private String recordCourseNum;
    /**
     * 信息反馈-好的方面
     */
    @ExcelProperty({"信息反馈", "好的方面"})
    @ColumnWidth(30)
    private String feedbackGood;
    /**
     * 信息反馈-不足之处
     */
    @ExcelProperty({"信息反馈", "不足之处"})
    @ColumnWidth(30)
    private String feedbackNotEnough;
    /**
     * 希望和建议
     */
    @ExcelProperty("希望和建议")
    @ColumnWidth(30)
    private String hopesAndSuggestions;
    /**
     * 创建时间
     */
    @ExcelProperty("提交时间")
    @DateTimeFormat("yyyy年MM月dd日HH时mm分ss秒")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TeachInfo)) {
            return false;
        }
        TeachInfo teachInfo = (TeachInfo) o;
        return Objects.equals(getSubmitPerson(), teachInfo.getSubmitPerson())
                && Objects.equals(getSubmitPersonName(), teachInfo.getSubmitPersonName())
                && Objects.equals(getSubmitPersonCollege(), teachInfo.getSubmitPersonCollege())
                && Objects.equals(getSubmitPersonClass(), teachInfo.getSubmitPersonClass())
                && Objects.equals(getCourseName(), teachInfo.getCourseName())
                && Objects.equals(getTeacherName(), teachInfo.getTeacherName())
                && Objects.equals(getShouldArriveNum(), teachInfo.getShouldArriveNum())
                && Objects.equals(getActualArriveNum(), teachInfo.getActualArriveNum())
                && Objects.equals(getClassLocation(), teachInfo.getClassLocation())
                && Objects.equals(getRecordClassDate(), teachInfo.getRecordClassDate())
                && Objects.equals(getRecordCourseNum(), teachInfo.getRecordCourseNum())
                && Objects.equals(getFeedbackGood(), teachInfo.getFeedbackGood())
                && Objects.equals(getFeedbackNotEnough(), teachInfo.getFeedbackNotEnough())
                && Objects.equals(getHopesAndSuggestions(), teachInfo.getHopesAndSuggestions());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSubmitPerson(),
                getSubmitPersonName(), getSubmitPersonCollege(),
                getSubmitPersonClass(), getCourseName(),
                getTeacherName(), getShouldArriveNum(),
                getActualArriveNum(), getClassLocation(),
                getRecordClassDate(), getRecordCourseNum(),
                getFeedbackGood(), getFeedbackNotEnough(),
                getHopesAndSuggestions());

    }
}
