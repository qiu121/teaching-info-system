package com.github.qiu121.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/04/16
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestBodyData {
    /**
     * 信息员组长对象
     */
    private StuAdmin stuAdmin;
    /**
     * 提交信息对象(信息员)
     */
    private TeachInfo teachInfo;
}
