package com.github.qiu121.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/4/12
 * @description 学院记录表
 * <p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class College implements Serializable {

    private static final long serialVersionUID = -3792243409603238170L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 学院名称
     */
    @NotBlank(message = "学院名称不可为空")
    private String name;
    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof College)) {
            return false;
        }
        College college = (College) o;
        return Objects.equals(getName(), college.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

}
