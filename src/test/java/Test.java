import alibaba.DemoDataListener;
import bean.DemoData;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.king.hhczy.base.constant.CacheConstants;
import com.king.hhczy.common.util.FilesUtils;
import com.king.hhczy.common.util.Log;
import com.king.hhczy.entity.domain.TblAccount;
import lombok.Cleanup;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Test {

    public static void main(String[] args) throws Exception {
        alrr();
//        double a = 62.21345856456465;
//        System.out.println(String.format("%.5f",a));
//        Map<String, Object> data = new HashMap<>();
//        List data = new ArrayList<>();
//        data.add(1);
//        data.add("李武");
//        data.add(10);
//        // 写法1
//        String fileName = "D:\\simpleWrite" + System.currentTimeMillis() + ".xlsx";
//        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭-
//        // 如果这里想使用03 则 传入excelType参数即可
//        EasyExcel.write(fileName, Students.class).sheet("模板").doWrite(data);

//        // 写法2
//        fileName = TestFileUtil.getPath() + "simpleWrite" + System.currentTimeMillis() + ".xlsx";
//        // 这里 需要指定写用哪个class去写
//        ExcelWriter excelWriter = EasyExcel.write(fileName, DemoData.class).build();
//        WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
//        excelWriter.write(data(), writeSheet);
//        /// 千万别忘记finish 会帮忙关闭流
//        excelWriter.finish();
//        searchFileByContent3("D:\\log2");
//        MethodUtil.executeTargrtMethod(Test.class, "testSout");//反射，取代if/else
    }
    private static String createTranData(String subCmdType, String rootName , Object inputMap){
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("<SubCmdType>");
        stringBuilder.append(subCmdType);
        stringBuilder.append("</SubCmdType>");

        stringBuilder.append("<" + rootName + ">");
        if(inputMap instanceof Map){
            Gson gson = new Gson();
            Map mapVal = gson.fromJson(gson.toJson(inputMap), Map.class);
            mapVal.forEach((k,v)->{
                stringBuilder.append("<" + k + ">");
                if (v instanceof Map) {
                    Map mapVal2 = gson.fromJson(gson.toJson(v), Map.class);
                    mapVal2.forEach((k2,v2)->{
                        stringBuilder.append("<" + k2 + ">" +v2 +"</" + k2 + ">" );
                    });
                }else {
                    stringBuilder.append(v);
                }
                stringBuilder.append("<" + k + ">");
            });
        }else {
            stringBuilder.append(""+inputMap);
        }


        stringBuilder.append("</" + rootName + ">");
        return stringBuilder.toString();
    }
    /*
     * 中文转unicode编码
     */
    public static String gbEncoding(final String gbString) {
        char[] utfBytes = gbString.toCharArray();
        String unicodeBytes = "";
        for (int i = 0; i < utfBytes.length; i++) {
            String hexB = Integer.toHexString(utfBytes[i]);
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            unicodeBytes = unicodeBytes + "\\u" + hexB;
        }
        return unicodeBytes;
    }
    public void testSout() {
        System.out.println("来了老弟：");
    }
    //二进制
    public static void base64Test() {
        byte[] bytes1 = new byte[]{67, 97, 112, 116, 117, 114, 101, 0, 126, 1, 0, 0, 105, 0, 0, 0, 123, 34, 100, 101, 118, 105, 99, 101, 115, 73, 100, 34, 58, 34, 55, 56, 67, 65, 56, 51, 52, 48, 48, 53, 49, 57, 48, 48, 48, 48, 48, 48, 48, 48, 34, 44, 34, 118, 105, 100, 101, 111, 78, 111, 34, 58, 51, 44, 34, 99, 104, 97, 110, 110, 101, 108, 78, 97, 109, 101, 34, 58, 34, 104, 105, 107, 95, 50, 51, 51, 34, 44, 34, 116, 114, 97, 99, 107, 73, 100, 34, 58, 54, 48, 49, 44, 34, 101, 110, 116, 101, 114, 84, 105, 109, 101, 115, 116, 97, 109, 112, 34, 58, 48, 44, 34, 99, 97, 116, 99, 104, 84, 105, 109, 101, 115, 116, 97, 109, 112, 34, 58, 49, 53, 54, 51, 52, 57, 56, 56, 49, 56, 57, 55, 54, 44, 34, 108, 101, 97, 118, 101, 84, 105, 109, 101, 115, 116, 97, 109, 112, 34, 58, 48, 44, 34, 102, 97, 99, 101, 65, 116, 116, 114, 34, 58, 123, 34, 97, 103, 101, 34, 58, 50, 52, 44, 34, 103, 101, 110, 100, 101, 114, 34, 58, 49, 44, 34, 109, 105, 110, 111, 114, 105, 116, 121, 34, 58, 102, 97, 108, 115, 101, 44, 34, 108, 101, 102, 116, 69, 121, 101, 111, 99, 99, 34, 58, 102, 97, 108, 115, 101, 44, 34, 114, 105, 103, 104, 116, 69, 121, 101, 111, 99, 99, 34, 58, 102, 97, 108, 115, 101, 44, 34, 109, 111, 117, 116, 104, 111, 99, 99, 34, 58, 102, 97, 108, 115, 101, 44, 34, 104, 101, 97, 100, 119, 101, 97, 114, 34, 58, 50, 44, 34, 110, 111, 115, 101, 111, 99, 99, 34, 58, 102, 97, 108, 115, 101, 44, 34, 98, 101, 97, 114, 100, 34, 58, 48, 44, 34, 108, 101, 102, 116, 69, 121, 101, 67, 108, 111, 115, 101, 34, 58, 102, 97, 108, 115, 101, 44, 34, 114, 105, 103, 104, 116, 69, 121, 101, 67, 108, 111, 115, 101, 34, 58, 102, 97, 108, 115, 101, 44, 34, 109, 111, 117, 116, 104, 83, 116, 97, 116, 117, 115, 34, 58, 116, 114, 117, 101, 125, 44, 34, 99, 97, 112, 116, 117, 114, 101, 70, 97, 99, 101, 34, 58, 50, 51, 49, 50, 55, 125};
        byte[] clone = new byte[bytes1.length - 16];
        System.arraycopy(bytes1, 16, clone, 0, bytes1.length - 16);
        System.out.println(Arrays.toString(clone));
        try {
            //主要用于处理byte转int
            ByteBuffer bb = ByteBuffer.wrap(bytes1, 8, 4);
//            System.out.println(bb.getInt());
            //不能写在一起测
            ByteBuffer bbb = ByteBuffer.wrap(bytes1, 12, 4);
//            bbb.order(ByteOrder.LITTLE_ENDIAN);//倒叙，小周
//            System.out.println(bbb.getInt());
//            System.out.println(new String(bytes1,0,8,"UTF-8"));//ISO-8859-1,UTF-8,gbk
//            System.out.println(byteArrayToInt(bytes2));//ISO-8859-1,UTF-8,gbk
//            System.out.println(byte4ToInt(bytes1,8));//ISO-8859-1,UTF-8,gbk
//            System.out.println(Arrays.toString(intToByteArray(2113994752)));//ISO-8859-1,UTF-8,gbk
//            System.out.println(byte4ToInt(bytes1,8));//ISO-8859-1,UTF-8,gbk
//            System.out.println(bytesToHexString(bytes2));//ISO-8859-1,UTF-8,gbk
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String key = new String();
    static int ii = 0;

    //    定时任务测试(线程)
    public static void myTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("------定时任务,0秒后开始执行，每1秒执行一次--------");
                if (num() % 3 == 0) {
                    try {
                        synchronized (key) {
                            System.out.println("等待");
                            key.wait();
                        }
                    } catch (InterruptedException e) {
                    }
                }
            }
        }, 0, 5000);
    }

    public static int num() {
        return ii++;
    }

    //内容检索2
    private static void searchFileByContent3(String path) {
        Path filesPath = Paths.get(path);
        try {
            DirectoryStream<Path> files = Files.newDirectoryStream(filesPath);
            for (Path file : files) {
                List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
                for (String line : lines) {
                    if (line.contains("eventSync")) {
                        System.out.println(file.getFileName());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //内容检索
    private static void searchFileByContent(String path, String words) {
        Path filesPath = Paths.get(path);
        try {
            DirectoryStream<Path> files = Files.newDirectoryStream(filesPath);
            for (Path file : files) {
                List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
                String dc = lines.stream().map(x -> x.split("编码")[1]).map(y -> y.split("，")[0]).collect(Collectors.toSet())
                        .stream().reduce("", (s1, s2) -> "'" + s1 + "," + "'" + s2 + "'");
                System.out.println(dc);

//                for (String line : lines) {
//                    if (line.contains(words)) {
//                        System.out.println(file.getFileName());
//                        break;
////                        if (line.contains("[{")) {
////                            List<JSONObject> list = JSONObject.parseObject(line.substring(line.indexOf("[{")), List.class);
////                            for (JSONObject o : list) {
////                                System.out.println(o.toJSONString());
////                                System.out.println("================================================================");
////                            }
////                        }
//                    }
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void searchFileByContent2(String path, String words) {
        //所在文件夹路径
        Path filesPath = Paths.get(path);
        String cardNo = "\"eventType\":";
        words = cardNo;

        Map<String, Integer> map = new HashMap<>();
        Map<String, Integer> map2 = new HashMap<>();
        int count = 0;
        try {
            DirectoryStream<Path> files = Files.newDirectoryStream(filesPath);
            for (Path file : files) {
                System.out.println("##开始检索日志文件：" + file.getFileName());
                List<String> lines = Files.readAllLines(filesPath.resolve(file.getFileName()), StandardCharsets.UTF_8);
                for (String line : lines) {
                    if (line.contains(words) && !line.contains("批量保存事件文件存储服务入参")) {
                        List<JSONObject> list = JSONObject.parseObject(line.substring(line.indexOf("[{")), List.class);
                        for (JSONObject o : list) {
                            Integer eventType = o.getInteger("eventType");
                            //XS_SZFT_10010040 上梅林；XS_SZFT_10010034 石厦东村；XS_SZFT_10010036 石厦西村
                            if (eventType != 24 && eventType != 11 && o.getString("villageCode").equals("XS_SZFT_10010046") && o.getString("eventFile") == null) {
//                            if (eventType == 1 && o.getString("cardNo").equals(cardNo) && o.getString("eventFile") == null) {
//                                String fileName = file.getFileName().toString();
//                                String strFile = fileName.substring(0, fileName.indexOf("."));
//                                if (map.get(strFile + "fzp") != null) {
//                                    map.put(strFile + "fzp", map.get(strFile + "fzp") + 1);
//                                    if (eventType > 0 && eventType < 7) {
//                                        if (map2.get(strFile + "km") != null) {
//                                            map2.put(strFile + "km", map2.get(strFile + "km") + 1);
//                                        } else {
//                                            map2.put(strFile + "km", 1);
//                                        }
//                                    }
//                                } else {
//                                    map.put(strFile + "fzp", 1);
//                                }
                                count++;
                                String key = o.getString("certificateNo") + o.getString("eventTime");
                                if (map.get(key) == null) {
                                    map.put(key, 1);
                                }
                                String key2 = o.getString("eventTime");
                                if (map2.get(key2) == null) {
                                    map2.put(key2, 1);
                                }
                                System.out.println(o.toJSONString());
                                System.out.println("================================================================");
                            }
                        }
//                        break;
                    }
                }
            }
//            System.out.println("统计日期：2019-07-26~2019-08-02");
//            map.forEach((k, v) -> {
//                System.out.println("捕获到非抓拍事件，日期" + k + "共 " + v + "条记录");
//            });
//            map2.forEach((k, v) -> {
//                System.out.println("捕获到开门事件，日期" + k + "共 " + v + "条记录");
//            });
            Log.info("检索到总数：{}，唯一key1：{}，唯一key2：{}", count, map.size(), map2.size());
            //打开的Stream，需要关闭。（否则，linux下会造成： too many open files）
            files.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void writeToFile(Long idNum, String path, int size) {
        try {
            Path path1 = Paths.get(path);
            if (!Files.exists(path1)) {
                Files.createFile(path1);
            }
            List<String> idNums = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                idNums.add(Long.toString(idNum + i));
            }
            Files.write(path1, idNums);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //springboot新特性
    private static void springboot2() {
        //IntStream int流
        int[] num = new int[100000000];
        for (int i = 0; i < num.length; i++) {
            num[i] = ((int) Math.floor(Math.random() * 10) % 2 == 0 ? 1 : -1) * ((int) Math.floor(Math.random() * 10)) * ((int) Math.floor(Math.random() * 100));
        }
        long b = System.currentTimeMillis();
        int asInt = 0;
        int s = 10;//循环次数
        for (int i = 0; i < s; i++) {
//            asInt = IntStream.of(num).parallel().min().getAsInt();//函数式编程，parallel：并行，用于处理多线程
            int min = Integer.MAX_VALUE;
            for (int i1 : num) {
                if (i1 < min) {
                    min = i1;
                }
            }
            asInt = min;
        }
        System.out.println("得到最小值：" + asInt);
        long e = System.currentTimeMillis();
        for (int i = 0; i < s; i++) {
            asInt = IntStream.of(num).min().getAsInt();//函数式编程，parallel：并行，用于处理多线程
        }
        System.out.println("得到最小值：" + asInt);
        long f = System.currentTimeMillis();
        for (int i = 0; i < s; i++) {
            asInt = IntStream.of(num).parallel().min().getAsInt();//函数式编程，parallel：并行，用于处理多线程
        }
        System.out.println("得到最小值：" + asInt);
        long g = System.currentTimeMillis();
        System.out.println("数据总数：" + num.length);
        System.out.println("普通for循环耗时：" + (e - b));
        System.out.println("普通流处理耗时：" + (f - e));
        System.out.println("并行流处理耗时：" + (g - f));
        //lambada表达式 创建多线程
//        new Thread(() -> {
//            System.out.println("创建多线程");
//            try {
//                Thread.sleep(2000);
//                System.out.println("休息完毕");
//            } catch (InterruptedException e) {
//            }
//        }).start();
//        System.out.println("主线程");
        //获取List ID最大的一条
        List<Integer> m = new ArrayList<>();
        m.add(1);
        m.add(1);
        m.add(5);
        m.add(2);
        System.out.println(m.parallelStream().max(Comparator.comparing(Integer::intValue)).get());
    }


    //csv?????gbk
//    private final static String NEW_LINE_SEPARATOR = "\n";
    //????
    private final static String FILE_PATH = "D:/csv_test";
    //????
    private final static String POSTFIX = ".csv";
    //????????
    private final static Integer LIMIT_NUM = 2;

    //jdk8

    //jdk8时间测试
    private static void dateTest() {
        //该月有几天
        System.out.println(LocalDate.of(2019, 2, 1).lengthOfMonth());//2019年2月有几天
        System.out.println(LocalDate.now().getMonthValue());//获取当前月
        System.out.println(LocalDate.now().withMonth(1).getMonthValue());//获取指定月份
        System.out.println(LocalDate.now().minusMonths(1).getMonthValue());//上一月
        System.out.println(LocalDate.now().plusMonths(1).getMonthValue());//下一月
        LocalDate now = LocalDate.now();
        LocalDate date = LocalDate.parse("2018-12-20");
        LocalTime now1 = LocalTime.now();
        LocalDateTime now2 = LocalDateTime.now();
        String yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
        String format = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        String format1 = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        //计算时间差，时差，时间差
        LocalDateTime now3 = LocalDateTime.now();
        System.out.println("计算两个时间的差：");
        LocalDateTime end = LocalDateTime.now();
        Duration duration = Duration.between(now3, end);
        long days = duration.toDays(); //相差的天数
        long hours = duration.toHours();//相差的小时数
        long minutes = duration.toMinutes();//相差的分钟数
        long millis = duration.toMillis();//相差毫秒数
        long nanos = duration.toNanos();//相差的纳秒数
        System.out.println(now3);
        System.out.println(end);
        System.out.println("发送短信耗时【 " + days + "天：" + hours + " 小时：" + minutes + " 分钟：" + millis + " 毫秒：" + nanos + " 纳秒】");
        System.currentTimeMillis();//获取当前时间戳0
        System.out.println(Instant.now().toEpochMilli());//获取当前时间戳1
        System.out.println(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());//获取当前时间戳2
        System.out.println(LocalDate.now().minusDays(30).toString());//30天前的日期
        System.out.println(Timestamp.valueOf(LocalDateTime.now().minusDays(30)));//30天前的日期的TimeStamp
        //判断是不是同一天，两个日期是否相等
        LocalDate date1 = LocalDate.parse("2019-02-20");
        System.out.println(LocalDate.now().isEqual(date1));
    }

    private static void test4() {
        //https://blog.csdn.net/u011055819/article/details/80070429
        LocalDate now = LocalDate.now();
        LocalDate date = LocalDate.parse("2018-12-20");
        //?????
        LocalTime now1 = LocalTime.now();
        //??????
        LocalDateTime now2 = LocalDateTime.now();

        //lambda(???????????)
        Runnable r = () -> System.out.println("hello world");//?????
        r.run();
        Consumer<String> con = (x) -> System.out.println(x);
        Consumer<String> con2 = x -> System.out.println(x);//????
        con.accept("886");
        con2.accept("886");
        Comparator<Integer> com = (x, y) -> Integer.compare(x, y);
        System.out.println(com.compare(500, 500));
        new Thread(() -> System.out.println("????")).start();
        List<String> list = Arrays.asList("a", "b", "c", "d");
        list.forEach(n -> System.out.println(n));
        list.forEach(System.out::println);
        List<Double> doubles = Arrays.asList(10.0, 20.0, 30.0);
        doubles.stream().map(x -> x + x * 0.5).forEach(System.out::println);
    }

    //数字比较测试
    private static void test5() {
        Integer a = 1;
        Integer b = 2;
        Integer c = 3;
        Integer d = 3;
        Integer e = 321;
        Integer f = 321;
        Long g = 3L;
        Long h = 2L;
        System.out.println(c == d);//true
        System.out.println(e == f);//false
        System.out.println(c == (a + b));//true
        System.out.println(c.equals(a + b));//true
        System.out.println(g == (a + b));//true
        System.out.println(g.equals(a + b));//false
        System.out.println(g.equals(a + h));//true
    }

    //jdk8 Optional测试
    private static void optionalTest() {
        Optional<String> empty = Optional.empty();
//        empty.get();
        TblAccount account = null;
        Optional<TblAccount> tblArea1 = Optional.ofNullable(account);
        //简化if-else
        String s = tblArea1.map(TblAccount::getAccount).map(String::toUpperCase).orElse("空空的");
        System.out.println(s);

    }

    private static void test2() {
        File file = new File("E:\\????.txt");
        Properties properties = System.getProperties();
        if (file.exists()) {
            try {
                InputStreamReader reader = new InputStreamReader(new FileInputStream(file), "gbk");
//                try (BufferedReader bufferedReader = new BufferedReader(reader)){ //这样写也可以自动关流
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                @Cleanup BufferedReader bufferedReader = new BufferedReader(reader);//@Cleanup:自动关闭资源
                StringBuilder result = new StringBuilder();
                String partResult;
                while ((partResult = bufferedReader.readLine()) != null) {
                    result.append("'," + partResult);
                }
//                System.out.println(result.toString());
                String subVal = result.toString().replaceAll("\\s*=\\s*", ":'");
                String[] splitVal = (subVal.substring(1) + "'").split(",<\\+\\+\\+>',");
//                System.out.println(Arrays.toString(splitVal));
                Map<String, List> mapResult = new HashMap<>();
                for (String s : splitVal) {
                    if (s != null && !"".equals(s)) {
                        JSONObject jsonObject = JSONObject.parseObject("{" + s + "}");
                        String jzmc = jsonObject.getString("????");
                        if (mapResult.get(jzmc) != null) {
                            mapResult.get(jzmc).add(jsonObject.getString("????"));
                        } else {
                            List<Object> list = new ArrayList<>();
                            list.add(jsonObject.getString("????"));
                            mapResult.put(jzmc, list);
                        }
                    }
                }

                for (Map.Entry<String, List> stringList : mapResult.entrySet()) {
                    System.out.println("???" + stringList.getKey() + ",?????(" + stringList.getValue().size() + "?)??????" + String.join(",", stringList.getValue()) + "?");
                }

                reader.close();
                bufferedReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    static boolean securityString = true;

    public static String encodeString(String source) {// 加密
        if (securityString) {
            if (StringUtils.isEmpty(source)) {
                return "";
            }
            return source.chars().boxed().map(x -> String.format("%03d", x)).reduce("", String::concat);
//            return source.chars().boxed().map(String::valueOf).reduce("", (s1, s2) -> String.format("%03d",s1)+String.format("%03d",s2));
        } else {
            return source;
        }
    }

    public static String decodeString(String source) {// 解密
        if (securityString) {
            StringBuilder builder = new StringBuilder();
            source = source.trim();
            int length = source.length();
            for (int i = 0; i < length; i = i + 3) {
                String numStr = source.substring(i, length >= (i+3) ? (i+3):length);
                char charAt= (char)(Integer.valueOf(numStr)).intValue();
                builder.append(charAt);
            }
            return builder.toString();
        }else {
            return source;
        }
    }

    /**
     * Files/Paths练习
     * 参考：https://www.cnblogs.com/digdeep/p/4478734.html
     */
    public static void FilesAndPaths() {
        //路径拼接方法
        Path path1 = Paths.get("G:/", "Xmp");
        Path path2 = Paths.get("G:/Xmp");
        URI u = URI.create("file:///G:/Xmp");//创建UIR供paths使用，并不是本地创建
        Path path3 = Paths.get(u);
        Path path4 = FileSystems.getDefault().getPath("G:/", "Xmp");

        try {
            Path path5 = Paths.get("G:/", "Xmp", "dd.txt");//这种写法就不用加斜杠
            //文件夹、文件均属判断范围
            if (!Files.exists(path5)) {
                //创建文件前先建文件夹
                Files.createDirectories(path5.getParent());
                //创建文件，不能创建文件夹
                Files.createFile(path5);
            }
            //创建文件夹
            //Files.createDirectory()//目录必须存在，否则抛异常
            //目录存在就跳过
            //System.getProperty("user.dir") source:vdu_sz; jar:bin 都指向工作环境路径
            Files.createDirectories(Paths.get(System.getProperty("user.dir")).getParent());
            //读取文件夹的文件
            DirectoryStream<Path> files = Files.newDirectoryStream(Paths.get("G:/", "Xmp"));
            //文件内容读取
            //比下面newBufferedReader耗时，不过此方法可以用并行处理提高效率
            List<String> lines1 = Files.readAllLines(path1.resolve("log.log"), StandardCharsets.UTF_8);//resolve作用：path拼接字符串返回path
            for (String line1 : lines1) {
//                System.out.println(line1);
            }
            //拿到BufferedReader，处理比上面快一点
            BufferedReader br = Files.newBufferedReader(path1.resolve("log.log"), StandardCharsets.UTF_8);
            String line2 = null;
            while ((line2 = br.readLine()) != null) {
//                System.out.println(line2);
            }
            //写文件测试
            Files.write(path1.resolve("copy1.log"), lines1, StandardCharsets.UTF_8);
            BufferedWriter bw = Files.newBufferedWriter(path1.resolve("copy2.log"), StandardCharsets.UTF_8);
            bw.write("测试文件写操作");
            bw.newLine();//换行
            bw.flush();
            bw.close();

            //遍历文件夹测试
            //单目录遍历
            DirectoryStream<Path> paths1 = Files.newDirectoryStream(path1);
            Stream<Path> paths2 = Files.list(path1);
            Iterator<Path> iteratorPaths2 = paths2.iterator();
            //整个目录遍历
            List<Path> allFilesPaths = FilesUtils.getAllFilesPaths("G:/Xmp");
            //文件复制
            //从文件复制到输出流
            Files.copy(path1.resolve("log.log"), System.out);//打印到控制台
            //从文件复制到文件
            Files.copy(path1.resolve("log.log"), path1.resolve("log_copy1.log"), StandardCopyOption.REPLACE_EXISTING);//存在则替换
            //从输入流复制到文件
            //从控制台输入获取
            Files.copy(System.in, path1.resolve("log_copy2.log"), StandardCopyOption.REPLACE_EXISTING);//存在则覆盖
            //读取和设置文件权限：省略

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void ImageTest() {

    }

    public static void StringTest() {
        //截最后一个.的前段
        org.apache.commons.lang3.StringUtils.substringBeforeLast("a.jpg", ".");
        //截最后一个.的后段
        org.apache.commons.lang3.StringUtils.substringAfterLast("a.jpg", ".");
    }

    //关于线程安全
    public static void threadFafe() {
        List l = Collections.synchronizedList(new ArrayList<>());//List线程安全,写快读慢，在并行流有用到
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();//有读写分离锁,websocket有实例
        //java 线程安全的全局计数器-AtomicInteger
        AtomicInteger atomicInteger = new AtomicInteger(0);
        System.out.println(atomicInteger.incrementAndGet());
        System.out.println(atomicInteger.decrementAndGet());

    }

    //关于jdk8一些方法
    public static void jdk8() {
        //补足 前面补0
        String.format("%07d", 1);//用0补足7位
        String.format("%.5f", 11.46546465);//保留5位小数
        //list截取
        List<Object> list = new ArrayList<>();
        list.subList(0, 10);//list截取0-10个记录
    }
    //URL下载图片
    public static void urlDownloadPic(String source) {
        try {
            URL url = new URL(source);
            URLConnection con = url.openConnection();
            try (InputStream inputStream = con.getInputStream()){
                if (!Files.exists(Paths.get("D:\\ABCd.jpg"))) {
                    Files.copy(inputStream, Paths.get("D:\\ABCd.jpg"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //模拟大乐透
    public static void dlt(List<int[]> as, List<int[]> bs) {
        int bugNum = 0;
        int getOne = 0, getTwo = 0, getThree = 0, getFour = 0, getFive = 0, getSix = 0, getSeven = 0, getEight = 0, getNine = 0;
        while (true) {
            Set<Integer> lotteryNumbers1 = new HashSet<>();
            Set<Integer> lotteryNumbers2 = new HashSet<>();
            //生成1-35的随机数
            while (true) {
                int lotteryNumber = (int) (Math.random() * 35 + 1);
                lotteryNumbers1.add(lotteryNumber);
                if (lotteryNumbers1.size() == 5) {
                    break;
                }
            }
            //生成1-12的随机数
            while (true) {
                int lotteryNumber = (int) (Math.random() * 12 + 1);
                lotteryNumbers2.add(lotteryNumber);
                if (lotteryNumbers2.size() == 2) {
                    break;
                }
            }
            for (int i = 0; i < as.size(); i++) {
                bugNum++;
                //中几个
                long sum1 = Arrays.stream(as.get(i)).filter(lotteryNumbers1::contains).count();
                long sum2 = Arrays.stream(bs.get(i)).filter(lotteryNumbers2::contains).count();
                if (sum2 == 2) {
                    if (sum1 == 5) getOne++;
                    else if (sum1 == 4) getFour++;
                    else if (sum1 == 3) getSix++;
                    else if (sum1 == 2) getEight++;
                    else getNine++;
                } else if (sum2 == 1) {
                    if (sum1 == 5) getTwo++;
                    else if (sum1 == 4) getFive++;
                    else if (sum1 == 3) getEight++;
                    else if (sum1 == 2) getNine++;
                } else {
                    if (sum1 == 5) getThree++;
                    else if (sum1 == 4) getSeven++;
                    else if (sum1 == 3) getNine++;
                }

                System.out.println("花费:" + (bugNum * 2) + ",一等奖：" + getOne + " 注,二等奖：" + getTwo + " 注,三等奖：" + (getThree * 10000) + "元,四等奖："
                        + (getFour * 3000) + "元,五等奖：" + (getFive * 300) + "元,六等奖：" + getSix * 200 + "元,七等奖：" + getSeven * 100 + "元,八等奖：" + getEight * 15 + "元,九等奖：" + getNine * 5 + "元");
            }
        }
    }

    private static void alrr() throws IOException {
        EasyExcel.read("D:\\2.xlsx", DemoData.class, new DemoDataListener()).sheet(0).
                headRowNumber(1).doRead();

        int total = 0,uTotal = 0,oTotal = 0,ouTotal = 0;
        Path path = Paths.get("D:\\info.log");
        BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8);
        String vals = "";
        String lineVal = null;
        while ((lineVal = br.readLine()) !=null){
            System.out.println(lineVal);
            vals += lineVal;
        }
        JSONObject jsonVal = JSONObject.parseObject(vals);
        JSONArray list = jsonVal.getJSONObject("data").getJSONArray("list");
        List<Map> list2 = list.toJavaList(Map.class);
        Map<String, String> org = new HashMap<>();
        for (Map o : list2) {
            Object czhr = o.get("czhr");
            Object czhdw = o.get("czhdw");
            if (czhr !=null&&!"NULL".equals(czhr.toString().toUpperCase())) {
                uTotal++;
                org.put(czhdw.toString(), "1");
                if (czhdw !=null&&!"NULL".equals(czhdw.toString().toUpperCase())) {
                    ouTotal++;
                }
            }
            if (czhdw !=null&&!"NULL".equals(czhdw.toString().toUpperCase())) {
                oTotal++;
            }
        }
        total = list.size();
        System.out.println("统计日期：2020年8月19日");
        System.out.println("统计结果：");
        System.out.println("警情总数："+total+",其中包含处警人有："+uTotal+",包含处警部门有："+oTotal+",包含处警部门及处警人员有："+ouTotal);
        org.forEach((k,v)->{
            System.out.println(k+":"+CacheConstants.CACHE.get(k));
        });
    }
}
