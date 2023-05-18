package com.github.qiu121.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.qiu121.entity.Admin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/04/16
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminVo implements Serializable {
    private static final long serialVersionUID = -8156685488883719669L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 用户名
     */
    private String username;
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

    public AdminVo(Admin admin) {
        BeanUtils.copyProperties(admin, this);
    }
}
