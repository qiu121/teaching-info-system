package com.github.qiu121.util;

import cn.dev33.satoken.secure.SaSecureUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/3/15
 */
@SuppressWarnings({"all"})
public class SHAUtil {

    /**
     * SHA256加密
     *
     * @param strText 要加密的字符串
     * @return 加密后的字符串
     */
    public static String SHA256(final String strText) {
        //返回值
        String strResult = null;

        //是否是有效字符串
        if (strText != null && strText.length() > 0) {
            try {
                //创建加密对象
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                //传入要加密的字符串
                messageDigest.update(strText.getBytes());
                //得到 byte 类型结果
                byte[] byteBuffer = messageDigest.digest();

                //将 byte 转换为 string
                StringBuilder strHexString = new StringBuilder();
                //遍历
                for (byte b : byteBuffer) {
                    String hex = Integer.toHexString(0xff & b);
                    if (hex.length() == 1) {
                        strHexString.append('0');
                    }
                    strHexString.append(hex);
                }
                strResult = strHexString.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return strResult;
    }

    public static void main(String[] args) {
        System.out.println("SHA256加密== " + SHA256("123456"));
        System.out.println("SHA256加密== " + SaSecureUtil.sha256("123456"));
    }
}
