package bean;

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
public class ExcelTestModel {
    /**
     * 单独设置该列宽度
     */
    @ColumnWidth(50)
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
    @ExcelProperty(value = "枚举类",converter = DemoEnumConvert.class)
    private ExcelTestEnum demoEnum;
    /**
     * 忽略这个字段
     */
    @ExcelIgnore
    private String ignore;

    public ExcelTestModel(String string, Date date, Double doubleData, ExcelTestEnum demoEnum) {
        this.string = string;
        this.date = date;
        this.doubleData = doubleData;
        this.demoEnum = demoEnum;
    }
}
