package com.github.qiu121.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/07/05
 * @description 学院操作DTO对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "学院对象")
public class CollegeDTO {
    /**
     * 学院名称
     */
    @NotBlank(message = "学院名称不可为空")
    @Schema(description = "学院名称")
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CollegeDTO)) {
            return false;
        }
        CollegeDTO that = (CollegeDTO) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
