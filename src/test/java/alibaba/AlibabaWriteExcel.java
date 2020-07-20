package alibaba;

import bean.*;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.util.FileUtils;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.merge.LoopMergeStrategy;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.king.hhczy.entity.model.alibaba.excel.ImageModel;
import com.sun.scenario.effect.ImageData;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.function.Function;

/**
 * 常见写法整理
 * @author NingJinxiang
 */

/**
 * 文档：https://www.yuque.com/easyexcel/doc/write
 * https://github.com/alibaba/easyexcel
 * 官方说明：
 * Java解析、生成Excel比较有名的框架有Apache poi、jxl。但他们都存在一个严重的问题就是非常的耗内存，
 * poi有一套SAX模式的API可以一定程度的解决一些内存溢出的问题，但POI还是有一些缺陷，比如07版Excel解压缩以及解压后存储都是在内存中完成的，内存消耗依然很大。
 * easyexcel重写了poi对07版Excel的解析，能够原本一个3M的excel用POI sax依然需要100M左右内存降低到几M，并且再大的excel不会出现内存溢出，
 * 03版依赖POI的sax模式。在上层做了模型转换的封装，让使用者更加简单方便
 */
public class AlibabaWriteExcel {
    private static String rootPath = "D:\\";

    public static void main(String[] args) throws Exception {
        annotationStyleWrite();
    }
    /**
     * 最简单的写
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link }
     * <p>
     * 2. 直接写即可
     */
    public static void simpleWrite() {
        // 写法1
        Function<String, String> fileName = x -> rootPath + "simpleWrite" + x + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        //ExcelTestModel 通过注解设定格式、样式，表头配置在类中,registerWriteHandler:Excel 表格样式
        EasyExcel.write(fileName.apply("1"), ExcelTestModel.class).sheet("模板1").registerWriteHandler(createTableStyle()).doWrite(data2());
        //与上面比多了图片导出
        EasyExcel.write(fileName.apply("1"), Students.class).sheet("模板1").registerWriteHandler(createTableStyle()).doWrite(data());

        //head：设置表头；registerWriteHandler：自适应列宽,自定义表头
        EasyExcel.write(fileName.apply("2")).head(head()).sheet("模板1").registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).doWrite(data());

        // 写法2(可以写多sheet)
        // 这里 需要指定写用哪个class去写
        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(fileName.apply("3"), ExcelTestModel.class).build();
            //这个遍历几次就有几个sheet begin----
            WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
            excelWriter.write(data(), writeSheet);
            //这个遍历几次就有几个sheet end----
        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    /**
     * 根据参数只导出指定列（可以要求导出哪些字段，不要哪些字段）
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link Students}
     * <p>
     * 2. 根据自己或者排除自己需要的列
     * <p>
     * 3. 直接写即可
     *
     * @since 2.1.1
     */
    public static void excludeOrIncludeWrite() {
        String fileName = rootPath + "excludeOrIncludeWrite" + System.currentTimeMillis() + ".xlsx";

        // 根据用户传入字段 假设我们要忽略 date
        Set<String> excludeColumnFiledNames = new HashSet<String>();
        excludeColumnFiledNames.add("date");
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName, Students.class).excludeColumnFiledNames(excludeColumnFiledNames).sheet("模板")
                .doWrite(data());

        fileName = rootPath + "excludeOrIncludeWrite" + System.currentTimeMillis() + ".xlsx";
        // 根据用户传入字段 假设我们只要导出 date
        Set<String> includeColumnFiledNames = new HashSet<String>();
        includeColumnFiledNames.add("date");
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName, Students.class).includeColumnFiledNames(includeColumnFiledNames).sheet("模板")
                .doWrite(data());
    }
    /**
     * 复杂头写入
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link ComplexHeadData}
     * <p>
     * 2. 使用{@link }注解指定复杂的头
     * <p>
     * 3. 直接写即可
     */
    public static void complexHeadWrite() {
        String fileName = rootPath + "complexHeadWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        ArrayList<ComplexHeadData> complexHeadDatas = new ArrayList<>();
        ComplexHeadData complexHeadData = new ComplexHeadData();
        complexHeadData.setString0("0");
        complexHeadData.setString1("1");
        complexHeadData.setString2("2");
        complexHeadData.setString3("3");
        complexHeadData.setString4("4");
        complexHeadDatas.add(complexHeadData);
        EasyExcel.write(fileName, ComplexHeadData.class).sheet("模板").doWrite(complexHeadDatas);
    }
    /**
     * 重复多次写入，一列一列地写，边查数据库边写
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link Device}
     * <p>
     * <p>
     * 3. 直接调用二次写入即可
     */

    public static void repeatedWrite() {
        // 方法1 如果写到同一个sheet
        String fileName = rootPath + "repeatedWrite1" + System.currentTimeMillis() + ".xlsx";
        ExcelWriter excelWriter = null;
        try {
            // 这里 需要指定写用哪个class去写
            excelWriter = EasyExcel.write(fileName, DemoData.class).build();
            // 这里注意 如果同一个sheet只要创建一次
            WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
            // 去调用写入,这里我调用了五次，实际使用时根据数据库分页的总的页数来
            for (int i = 0; i < 5; i++) {
                // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
                List<Students> data = data();
                excelWriter.write(data, writeSheet);
            }
        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }

        // 方法2 如果写到不同的sheet 同一个对象
        fileName = rootPath + "repeatedWrite" + System.currentTimeMillis() + ".xlsx";
        try {
            // 这里 指定文件
            excelWriter = EasyExcel.write(fileName, DemoData.class).build();
            // 去调用写入,这里我调用了五次，实际使用时根据数据库分页的总的页数来。这里最终会写到5个sheet里面
            for (int i = 0; i < 5; i++) {
                // 每次都要创建writeSheet 这里注意必须指定sheetNo 而且sheetName必须不一样
                WriteSheet writeSheet = EasyExcel.writerSheet(i, "模板" + i).build();
                // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
                List<Students> data = data();
                excelWriter.write(data, writeSheet);
            }
        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }

        // 方法3 如果写到不同的sheet 不同的对象
        fileName = rootPath + "repeatedWrite" + System.currentTimeMillis() + ".xlsx";
        try {
            // 这里 指定文件
            excelWriter = EasyExcel.write(fileName).build();
            // 去调用写入,这里我调用了五次，实际使用时根据数据库分页的总的页数来。这里最终会写到5个sheet里面
            for (int i = 0; i < 5; i++) {
                // 每次都要创建writeSheet 这里注意必须指定sheetNo 而且sheetName必须不一样。这里注意DemoData.class 可以每次都变，我这里为了方便 所以用的同一个class 实际上可以一直变
                WriteSheet writeSheet = EasyExcel.writerSheet(i, "模板" + i).head(DemoData.class).build();
                // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
                List<Students> data = data();
                excelWriter.write(data, writeSheet);
            }
        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    /**
     * 图片导出
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link ImageData}
     * <p>
     * 2. 直接写即可
     */

    public static void imageWrite() throws Exception {
        String fileName = rootPath + "imageWrite" + System.currentTimeMillis() + ".xlsx";
        // 如果使用流 记得关闭
        InputStream inputStream = null;
        try {
            List<ImageModel> list = new ArrayList<ImageModel>();
            ImageModel imageData = new ImageModel();
            list.add(imageData);
            String imagePath = rootPath + "converter" + File.separator + "img.jpg";//File.separator:只是一条斜杠/
            // 放入五种类型的图片 实际使用只要选一种即可（下面5种set都是导出图片）
            imageData.setByteArray(FileUtils.readFileToByteArray(new File(imagePath)));
            imageData.setFile(new File(imagePath));
            imageData.setString(imagePath);
            inputStream = FileUtils.openInputStream(new File(imagePath));
            imageData.setInputStream(inputStream);
            //来自网上的图片，存到excel的url字段，这里并不是跳转
            imageData.setUrl(new URL(
                    "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1418098423,3040751320&fm=26&gp=0.jpg"));
            EasyExcel.write(fileName, ImageModel.class).sheet().doWrite(list);//官方的ImageData，是自定义那个，不是包里的
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    /**
     * 根据模板写入，测试发现只是复制表头模板样式
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link Device}
     * <p>
     * 2. 使用{@link //ExcelProperty}注解指定写入的列
     * <p>
     * 3. 使用withTemplate 写取模板
     * <p>
     * 4. 直接写即可
     */

    public static void templateWrite() {
        String templateFileName = rootPath + "converter" + File.separator + "demo.xlsx";
        String fileName = rootPath + "templateWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName, Students.class).withTemplate(templateFileName).sheet().doWrite(data());
    }

    /**
     * 注解形式自定义样式
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link DemoStyleData}
     * <p>
     * 3. 直接写即可
     *
     * @since 2.2.0-beta1
     */

    public static void annotationStyleWrite() {
        String fileName = rootPath + "annotationStyleWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName, DemoStyleData.class).sheet("模板").doWrite(data3());
    }

    /**
     * 拦截器形式自定义样式
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link Students}
     * <p>
     * 2. 创建一个style策略 并注册
     * <p>
     * 3. 直接写即可
     */

    public static void handlerStyleWrite() {
        String fileName = rootPath + "handlerStyleWrite" + System.currentTimeMillis() + ".xlsx";
        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 背景设置为红色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short) 20);
        headWriteCellStyle.setWriteFont(headWriteFont);
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.头默认了 FillPatternType所以可以不指定
        contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        // 背景绿色
        contentWriteCellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        WriteFont contentWriteFont = new WriteFont();
        // 字体大小
        contentWriteFont.setFontHeightInPoints((short) 20);
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
        HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName, Students.class).registerWriteHandler(horizontalCellStyleStrategy).sheet("模板")
                .doWrite(data());
    }

    /**
     * 合并单元格
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link Students} {@link Device}
     * <p>
     * 2. 创建一个merge策略 并注册
     * <p>
     * 3. 直接写即可
     *
     * @since 2.2.0-beta1
     */

    public static void mergeWrite() {
        // 方法1 注解
        String fileName = rootPath + "mergeWrite" + System.currentTimeMillis() + ".xlsx";
        // 在DemoStyleData里面加上ContentLoopMerge注解
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName, Device.class).sheet("模板").doWrite(data());

        // 方法2 自定义合并单元格策略
        fileName = rootPath + "mergeWrite" + System.currentTimeMillis() + ".xlsx";
        // 每隔2行会合并 把eachColumn 设置成 3 也就是我们数据的长度，所以就第一列会合并。当然其他合并策略也可以自己写
        LoopMergeStrategy loopMergeStrategy = new LoopMergeStrategy(2, 0);
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName, Students.class).registerWriteHandler(loopMergeStrategy).sheet("模板").doWrite(data());
    }

    /**
     * 使用table去写入
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link Students}
     * <p>
     * 2. 然后写入table即可
     */

    public static void tableWrite() {
        String fileName = rootPath + "tableWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里直接写多个table的案例了，如果只有一个 也可以直一行代码搞定，参照其他案例
        // 这里 需要指定写用哪个class去写
        ExcelWriter excelWriter = EasyExcel.write(fileName, Students.class).build();
        // 把sheet设置为不需要头 不然会输出sheet的头 这样看起来第一个table 就有2个头了
        WriteSheet writeSheet = EasyExcel.writerSheet("模板").needHead(Boolean.FALSE).build();
        // 这里必须指定需要头，table 会继承sheet的配置，sheet配置了不需要，table 默认也是不需要
        WriteTable writeTable0 = EasyExcel.writerTable(0).needHead(Boolean.TRUE).build();
        WriteTable writeTable1 = EasyExcel.writerTable(1).needHead(Boolean.TRUE).build();
        // 第一次写入会创建头
        excelWriter.write(data(), writeSheet, writeTable0);
        // 第二次写如也会创建头，然后在第一次的后面写入数据
        excelWriter.write(data(), writeSheet, writeTable1);
        /// 千万别忘记finish 会帮忙关闭流
        excelWriter.finish();
    }

    /**
     * 动态头，实时生成头写入
     * <p>
     * 思路是这样子的，先创建List<String>头格式的sheet仅仅写入头,然后通过table 不写入头的方式 去写入数据
     *
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link Students}
     * <p>
     * 2. 然后写入table即可
     */

    public static void dynamicHeadWrite() {
        String fileName = rootPath + "dynamicHeadWrite" + System.currentTimeMillis() + ".xlsx";
        EasyExcel.write(fileName)
                // 这里放入动态头
                .head(head()).sheet("模板")
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())//自适应列宽
                // 当然这里数据也可以用 List<List<String>> 去传入
                .doWrite(data());
    }

    /**
     * 自动列宽(不太精确)
     * <p>
     * 这个目前不是很好用，比如有数字就会导致换行。而且长度也不是刚好和实际长度一致。 所以需要精确到刚好列宽的慎用。 当然也可以自己参照
     * {@link LongestMatchColumnWidthStyleStrategy}重新实现.
     * <p>
     * poi 自带{@link SXSSFSheet#autoSizeColumn(int)} 对中文支持也不太好。目前没找到很好的算法。 有的话可以推荐下。
     *
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link LongestMatchColumnWidthData}
     * <p>
     * 2. 注册策略{@link LongestMatchColumnWidthStyleStrategy}
     * <p>
     * 3. 直接写即可
     */

//    public static void longestMatchColumnWidthWrite() {
//        String fileName =
//                rootPath + "longestMatchColumnWidthWrite" + System.currentTimeMillis() + ".xlsx";
//        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
//        EasyExcel.write(fileName, LongestMatchColumnWidthData.class)
//                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).sheet("模板").doWrite(dataLong());
//    }

    /**
     * 下拉，超链接等自定义拦截器（上面几点都不符合但是要对单元格进行操作的参照这个）
     * <p>
     * demo这里实现2点。1. 对第一行第一列的头超链接到:https://github.com/alibaba/easyexcel 2. 对第一列第一行和第二行的数据新增下拉框，显示 测试1 测试2
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link Students}
     * <p>
     * 2. 注册拦截器 {@link CustomCellWriteHandler} {@link CustomSheetWriteHandler}
     * <p>
     * 2. 直接写即可
     */

//    public static void customHandlerWrite() {
//        String fileName = rootPath + "customHandlerWrite" + System.currentTimeMillis() + ".xlsx";
//        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
//        EasyExcel.write(fileName, Students.class).registerWriteHandler(new CustomSheetWriteHandler())
//                .registerWriteHandler(new CustomCellWriteHandler()).sheet("模板").doWrite(data());
//    }

    /**
     * 插入批注
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link Students}
     * <p>
     * 2. 注册拦截器 {@link CommentWriteHandler}
     * <p>
     * 2. 直接写即可
     */
//
//    public static void commentWrite() {
//        String fileName = rootPath + "commentWrite" + System.currentTimeMillis() + ".xlsx";
//        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
//        // 这里要注意inMemory 要设置为true，才能支持批注。目前没有好的办法解决 不在内存处理批注。这个需要自己选择。
//        EasyExcel.write(fileName, Students.class).inMemory(Boolean.TRUE).registerWriteHandler(new CommentWriteHandler())
//                .sheet("模板").doWrite(data());
//    }

    /**
     * 可变标题处理(包括标题国际化等)
     * <p>
     * 简单的说用List<List<String>>的标题 但是还支持注解
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link Device}
     * <p>
     * 2. 直接写即可
     */

    public static void variableTitleWrite() {
        // 写法1
        String fileName = rootPath + "variableTitleWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName, Device.class).head(variableTitleHead()).sheet("模板").doWrite(data());
    }

    /**
     * 不创建对象的写
     */

    public static void noModelWrite() {
        // 写法1
        String fileName = rootPath + "noModelWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName).head(head()).sheet("模板").doWrite(dataList());
    }

//    private List<LongestMatchColumnWidthData> dataLong() {
//        List<LongestMatchColumnWidthData> list = new ArrayList<LongestMatchColumnWidthData>();
//        for (int i = 0; i < 10; i++) {
//            LongestMatchColumnWidthData data = new LongestMatchColumnWidthData();
//            data.setString("测试很长的字符串测试很长的字符串测试很长的字符串" + i);
//            data.setDate(new Date());
//            data.setDoubleData(1000000000000.0);
//            list.add(data);
//        }
//        return list;
//    }

    private static List<List<String>> variableTitleHead() {
        List<List<String>> list = new ArrayList<List<String>>();
        List<String> head0 = new ArrayList<String>();
        head0.add("string" + System.currentTimeMillis());
        List<String> head1 = new ArrayList<String>();
        head1.add("number" + System.currentTimeMillis());
        List<String> head2 = new ArrayList<String>();
        head2.add("date" + System.currentTimeMillis());
        list.add(head0);
        list.add(head1);
        list.add(head2);
        return list;
    }

    /***
     * 设置 excel 的样式
     * @return
     */
    private static WriteHandler createTableStyle() {
        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 背景设置为红色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.PINK.getIndex());
        // 设置字体
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short) 20);
        headWriteCellStyle.setWriteFont(headWriteFont);
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.头默认了 FillPatternType所以可以不指定
        contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        // 背景绿色
        contentWriteCellStyle.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.getIndex());

        WriteFont contentWriteFont = new WriteFont();
        // 字体大小
        contentWriteFont.setFontHeightInPoints((short) 20);
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        // 设置边框的样式
        contentWriteCellStyle.setBorderBottom(BorderStyle.DASHED);
        contentWriteCellStyle.setBorderLeft(BorderStyle.DASHED);
        contentWriteCellStyle.setBorderRight(BorderStyle.DASHED);
        contentWriteCellStyle.setBorderTop(BorderStyle.DASHED);

        // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
        HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
        return horizontalCellStyleStrategy;
    }

    private static List<List<String>> head() {
        //如果每一列的相同行数的内容相同，将会自动合并单元格。通过这个规则，我们创建复杂的表头。
        List<List<String>> list = new ArrayList<List<String>>();
        List<String> head0 = new ArrayList<String>();
        head0.add("字符串");
        head0.add("字符串");
        List<String> head1 = new ArrayList<String>();
        head1.add("字符串");
        head1.add("数字");
        List<String> head2 = new ArrayList<String>();
        head2.add("年龄");
        List<String> head3 = new ArrayList<String>();
        head3.add("创建日期");
        list.add(head0);
        list.add(head1);
        list.add(head2);
        list.add(head3);
        return list;
    }

    private static List<List<Object>> dataList() {
        List<List<Object>> list = new ArrayList<List<Object>>();
        for (int i = 0; i < 10; i++) {
            List<Object> data = new ArrayList<Object>();
            data.add("字符串" + i);
            data.add(new Date());
            data.add(0.56);
            list.add(data);
        }
        return list;
    }

    private static List<Students> data() {
        List<Students> list = new ArrayList<Students>();
        for (int i = 0; i < 1; i++) {
            String imagePath = rootPath + "converter" + File.separator + "img.jpg";//File.separator:只是一条斜杠/
            Students data = new Students(i, imagePath,"宁" + i, i + 1, new Date());//不认localdatetime,DATE数据会被默认转格式yyyy-MM-dd hh:mm:ss
            list.add(data);
        }
        return list;
    }

    private static List<ExcelTestModel> data2() {
        List<ExcelTestModel> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ExcelTestModel data = new ExcelTestModel("宁" + i, new Date(), (double) i, ExcelTestEnum.Man);//不认localdatetime
            list.add(data);
        }
        return list;
    }
    private static List<DemoStyleData> data3() {
        List<DemoStyleData> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DemoStyleData data = new DemoStyleData("宁" + i, new Date(), (double) i);//不认localdatetime
            list.add(data);
        }
        return list;
    }

}
