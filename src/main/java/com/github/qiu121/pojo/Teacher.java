package com.github.qiu121.pojo;

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
public class Teacher implements Serializable{
    private static final long serialVersionUID = 1L;

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
     * 用户权限
     */
    private String permissions;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
