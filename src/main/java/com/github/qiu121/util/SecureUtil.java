package com.github.qiu121.util;

import cn.dev33.satoken.secure.SaSecureUtil;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/04/19
 * @description 哈希算法加密封装
 */
public class SecureUtil {
    private static final String SALT = "9527"; // 加盐字符串

    public static String encrypt(String message) {
        final String s = SaSecureUtil.md5(SALT + message);//密码加盐
        return SaSecureUtil.sha256(s);
    }

    public static boolean verify(String oldPassword, String password) {
        return encrypt(oldPassword).equals(password);
    }

    public static void main(String[] args) {
        System.out.println(verify("12", "12"));
        System.out.println(encrypt("test"));
        System.out.println(encrypt("stuadmin"));
        System.out.println(encrypt("admin"));
        System.out.println(encrypt("string"));

    }
}
