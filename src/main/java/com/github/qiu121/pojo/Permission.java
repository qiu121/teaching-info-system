package com.github.qiu121.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/04/03
 * <p>
 * 账户权限记录表
 */
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@RequiredArgsConstructor
public class Permission implements Serializable {
    private static final long serialVersionUID = 2228839092249057512L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 用户名
     */
    @NonNull
    private String username;
    /**
     * 权限等级(类型)
     */
    @NonNull
    private String type;
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
