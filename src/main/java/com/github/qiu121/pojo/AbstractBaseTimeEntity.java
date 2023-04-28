package com.github.qiu121.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/04/13
 * @description
 */
@MappedSuperclass//@MappedSuperclass的类将不是一个完整的实体类，将不会映射到数据库表
@EntityListeners(value = AuditingEntityListener.class)
@Getter
@Setter
public class AbstractBaseTimeEntity {
    @CreatedDate
    @Column(nullable = false, updatable = false,columnDefinition = "DATETIME " +
            "DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @LastModifiedDate
    @Column(nullable = false,columnDefinition = "DATETIME" +
            " DEFAULT CURRENT_TIMESTAMP NOT NULL " +
            "ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}