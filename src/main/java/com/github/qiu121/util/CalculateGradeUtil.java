package com.github.qiu121.util;

import java.time.LocalDate;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/04/13
 * @description 计算学生年级
 */
public class CalculateGradeUtil {
    /**
     * 通过月份计算在学年的年数，判断年级
     *
     * @param enrollYear 入学年份
     * @return 入学至今的总时间(按月计)
     */
    public static int calculate(int enrollYear) {
        int enrollMonth = 9;

        int currentYear = LocalDate.now().getYear();
        int currentMonth = LocalDate.now().getMonthValue();

        int diffMonth = (currentYear - enrollYear) * 12 + (currentMonth - enrollMonth);

        return diffMonth / 12 + 1;
    }

    public static void main(String[] args) {
        final int calculate = calculate(2022);
        System.out.println(calculate);

        System.out.println(System.currentTimeMillis());

    }
}
