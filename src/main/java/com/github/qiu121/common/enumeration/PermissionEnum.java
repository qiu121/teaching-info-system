package com.github.qiu121.common.enumeration;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/04/21
 * @description 账户权限
 */
@RequiredArgsConstructor
public enum PermissionEnum {
    STU_PERMISSION("stu"),
    STU_ADMIN_PERMISSION("stuAdmin"),
    ADMIN_PERMISSION("admin");

    @NonNull
    @Getter
    private final String type;

    @Override
    public String toString() {
        return super.toString();
    }
}
