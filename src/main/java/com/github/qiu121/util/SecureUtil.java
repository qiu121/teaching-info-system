package com.github.qiu121.util;

import cn.dev33.satoken.secure.SaSecureUtil;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/04/19
 * @description 哈希算法加密封装
 */
public class SecureUtil {
    public static String encrypt(String message) {
        final String s = SaSecureUtil.md5(message);
        return SaSecureUtil.sha256(s);
    }
}
