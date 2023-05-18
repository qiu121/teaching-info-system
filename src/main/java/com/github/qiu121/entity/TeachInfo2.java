package com.github.qiu121.entity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/4/10
 * <p>
 * 教学信息记录表(组长提交) <p></p>
 * 为什么组长提交的本就是TeachInfo里面属性内容，还要再新建一个类?
 * 为了判断对象是否相等(各属性的值都相同，id、createTime、updateTime等字段除外)，
 * 而两种情况下的比较标准不同、其实就是重写的equal()方法、hashCode()方法不同
 */
@Getter
@Setter
@ExcelIgnoreUnannotated
@ColumnWidth(15)
@ContentRowHeight(30)
@Entity
@org.hibernate.annotations.Table(appliesTo = "teach_info2", comment = "教学信息记录表(组长提交)")
public class TeachInfo2 extends AbstractBaseTimeEntity implements Serializable {
    private static final long serialVersionUID = 2848040276717059468L;

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    /**
     * 信息员组长账号
     */
    @ExcelProperty("信息员组长")
    @ApiModelProperty("组长账号")
    @Column(nullable = false, length = 15)
    private String submitPerson;
    /**
     * 信息员组长姓名
     */
    @ExcelProperty("姓名")
    @ApiModelProperty("姓名")
    @Column(length = 10)
    private String submitPersonName;
    /**
     * 信息员组长所在学院
     */
    @ExcelProperty("学院")
    @ApiModelProperty("学院")
    @Column(nullable = false, length = 15)
    private String submitPersonCollege;
    /**
     * 信息员所在组长班级
     */
    @ExcelProperty("班级")
    @ApiModelProperty("班级")
    @Column(length = 15)
    private String submitPersonClass;
    /**
     * 信息反馈-好的方面
     */
    @NotBlank(message = "信息反馈-好的方面未填写")
    @ExcelProperty({"信息反馈", "好的方面"})
    @ColumnWidth(30)
    @Column(nullable = false, columnDefinition = "TINYTEXT COMMENT '信息反馈-好的方面'")
    private String feedbackGood;
    /**
     * 信息反馈-不足之处
     */
    @NotBlank(message = "信息反馈-不足之处未填写")
    @ExcelProperty({"信息反馈", "不足之处"})
    @ColumnWidth(30)
    @Column(nullable = false, columnDefinition = "TINYTEXT COMMENT '信息反馈-不足之处'")
    private String feedbackNotEnough;
    /**
     * 希望和建议
     */
    @NotBlank(message = "希望和建议未填写")
    @ExcelProperty("希望和建议")
    @ColumnWidth(30)
    @Column(nullable = false, columnDefinition = "TINYTEXT COMMENT '希望和建议'")
    private String hopesAndSuggestions;

    /**
     * 创建时间
     */
    @ExcelProperty("提交时间")
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
        if (!(o instanceof TeachInfo2)) {
            return false;
        }
        TeachInfo2 that = (TeachInfo2) o;
        return Objects.equals(getSubmitPerson(), that.getSubmitPerson())
                && Objects.equals(getSubmitPersonName(), that.getSubmitPersonName())
                && Objects.equals(getSubmitPersonCollege(), that.getSubmitPersonCollege())
                && Objects.equals(getSubmitPersonClass(), that.getSubmitPersonClass())
                && Objects.equals(getFeedbackGood(), that.getFeedbackGood())
                && Objects.equals(getFeedbackNotEnough(), that.getFeedbackNotEnough())
                && Objects.equals(getHopesAndSuggestions(), that.getHopesAndSuggestions());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSubmitPerson(), getSubmitPersonName(),
                getSubmitPersonCollege(), getSubmitPersonClass(),
                getFeedbackGood(), getFeedbackNotEnough(),
                getHopesAndSuggestions());
    }
}
