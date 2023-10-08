package com.github.qiu121.common.enumeration;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/04/14
 * @description 学历层次
 */
@RequiredArgsConstructor
public enum EducationLevelEnum {
    UNDER_GRADUATE("本科"),
    JUNIOR_COLLEGE("专科"),
    COLLEGE_TO_UNDER_GRADUATE("专升本"),
    GRADUATE("研究生"),
    DOCTORAL("博士");

    @NonNull
    @Getter
    private final String name;

    @Override
    public String toString() {
        return super.toString();
    }
}
