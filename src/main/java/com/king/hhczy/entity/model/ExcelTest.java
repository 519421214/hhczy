package com.king.hhczy.entity.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.util.Date;

@ContentRowHeight(30)// 表体行高
@HeadRowHeight(20)// 表头行高
@ColumnWidth(35)// 列宽
@Data
public class ExcelTest {
    /**
     * 单独设置该列宽度
     * converter：可对值进行另外处理，这里是--字符串前面加上"自定义："三个字
     */
    @ColumnWidth(50)
//    @ExcelProperty(value = "字符串标题",converter = CustomStringStringConverter.class)
    @ExcelProperty("字符串标题")
    private String string;
    /**
     * 年月日时分秒格式
     */
    @DateTimeFormat("yyyy年MM月dd日HH时mm分ss秒")
    @ExcelProperty(value = "日期标题")
    private Date date;
    /**
     * 格式化百分比
     */
    @NumberFormat("#.##%")
    @ExcelProperty("数字标题")
    private Double doubleData;
    /**
     * 忽略这个字段
     */
    @ExcelIgnore
    private String ignore;
}
