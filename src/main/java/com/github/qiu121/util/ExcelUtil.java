package com.github.qiu121.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.github.qiu121.pojo.TeachInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/3/22
 */

public class ExcelUtil {
    /**
     * 导出Excel
     *
     * @param response 返回对象
     * @param list     Excel表中的记录
     */
    public static void writeExcel(HttpServletResponse response, List<TeachInfo> list) throws IOException {

        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build();
        //定义工作表对象
        WriteSheet sheet = EasyExcel.writerSheet(0, "sheet").head(TeachInfo.class).build();
        //向Excel文件中写入数据
        excelWriter.write(list, sheet);
        //关闭输出流
        excelWriter.finish();
    }
}

