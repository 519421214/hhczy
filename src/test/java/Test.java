import com.alibaba.fastjson.JSONObject;
import com.king.hhczy.common.util.FilesUtils;
import com.king.hhczy.common.util.Log;
import com.king.hhczy.entity.domain.TblAccount;
import lombok.Cleanup;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.URI;
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
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Test {

    public static void main(String[] args) throws InterruptedException {
//        String ip = "192.168.2.1";
        //
//        if (!IpV4Util.isSameAddress("192.168.3.1", ip.trim(),"255.255.255.0")) {
//            System.out.println(12345678);
//        }
//        Pattern compile = Pattern.compile("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$");
//        System.out.println(compile.matcher("192.168.2.1").matches());
        //还可以这么写
//        List<Integer> cost = Lists.newArrayList(1, 3, 7, 9, 34);
//        searchFileByContent("C:\\Users\\ningjinxiang\\Desktop\\sac\\log", "checkinSync");
//        System.out.println(LocalDate.of(2019, 2, 1).lengthOfMonth());
//        Path path = Paths.get("C:\\Users\\ningjinxiang\\Desktop\\sac\\tencent\\cert\\15558554155303.jpg");
//        try {
//            BASE64Encoder encoder = new BASE64Encoder();
//            System.out.println(encoder.encode(Files.readAllBytes(path)));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        // 加密
//        System.out.println(StringUtils.countOccurrencesOf("[asss,sadd,22266666666666666666666,,]", ","));
//        Integer a = 0;
//        Integer b = null;
//        BiFunction<Integer,Integer,Integer> initVal = (x,y) -> Optional.ofNullable(x).orElse(y);
//        System.out.println(initVal.apply(a, 1)+":"+initVal.apply(b, 10));
//        List a = null;
//        if (a.isEmpty()) {
//            System.out.println("null");
//        }
        //今年
//        String s = decodeString("049053056049052049050052049054053");
//        System.out.println(s);
//        Path path = Paths.get("G:\\1.log");
//        try {
//            List<String> vals = Files.readAllLines(path,StandardCharsets.UTF_8);
//            for (String val : vals) {
//                if (val.length()<10) {
//                    continue;
//                }
////                System.out.println("'"+decodeString(val)+"',");
//                System.out.println(decodeString(val));
//            }
//        } catch (IOException e) {
//
//        }
//        "admin".chars().boxed().forEach(System.out::println);
//        String a = "123456789";
//        System.out.println(String.copyValueOf(a.toCharArray(), 9, 3));
//        System.out.println(a.chars().parallel().filter(x->x>127||x<0).findFirst().orElse(0));
//        System.out.println(a.chars().boxed().map(String::valueOf).reduce("",(s1, s2) -> s1 + "0" + s2));
//        System.out.println((int)65297);
//        System.out.println(encodeString("15017607945"));
//        System.out.println(a.charAt(0));
//        System.out.println(decodeString("049053048054050052049057057057048055049049048051051055"));
//        System.out.println(encodeString("666666666666666666"));
//        System.out.println("052053050049050054049057055049048056050050048051052052");
//        searchFileByContent2("C:\\Users\\ningjinxiang\\Desktop\\sac\\log","00000000973E1007");
//        System.out.println(LocalDateTime.ofInstant(Instant.ofEpochMilli(1564364487290L), ZoneId.systemDefault()));
//        optionalTest();
        FilesAndPaths();
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

    //内容检索
    private static void searchFileByContent(String path, String words) {
        Path filesPath = Paths.get(path);
        try {
            DirectoryStream<Path> files = Files.newDirectoryStream(filesPath);
            for (Path file : files) {
                List<String> lines = Files.readAllLines(filesPath.resolve(file.getFileName()), StandardCharsets.UTF_8);
                for (String line : lines) {
                    if (line.contains(words)) {
                        System.out.println(file.getFileName());
                        break;
//                        if (line.contains("[{")) {
//                            List<JSONObject> list = JSONObject.parseObject(line.substring(line.indexOf("[{")), List.class);
//                            for (JSONObject o : list) {
//                                System.out.println(o.toJSONString());
//                                System.out.println("================================================================");
//                            }
//                        }
                    }
                }
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
                            if (eventType != 24 && eventType != 11 && o.getString("villageCode").equals("XS_SZFT_10010036") && o.getString("eventFile") == null && o.getInteger("eventTime") > 1566748800 && o.getInteger("eventTime") < 1566835200) {
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
        System.out.println(Instant.now().toEpochMilli());//获取当前时间戳1
        System.out.println(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());//获取当前时间戳2
        System.out.println(LocalDate.now().minusDays(30).toString());//30天前的日期
        System.out.println(Timestamp.valueOf(LocalDateTime.now().minusDays(30)));//30天前的日期的TimeStamp
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
            if (StringUtils.isEmpty(source)) {
                return "";
            }
            StringBuilder builder = new StringBuilder();
            source = source.trim();
            int length = source.length();
            for (int i = 0; i < length; i = i + 3) {
                String numStr = source.substring(i, length >= (i + 3) ? (i + 3) : length);
                char charAt = (char) (Integer.valueOf(numStr)).intValue();
                builder.append(charAt);
            }
            return builder.toString();
        } else {
            return source;
        }

    }

    /**
     * Files/Paths练习
     * 参考：https://www.cnblogs.com/digdeep/p/4478734.html
     */
    public static void FilesAndPaths() {
        //路径拼接方法
        Path path1 = Paths.get("G:/","Xmp");
        Path path2 = Paths.get("G:/Xmp");
        URI u = URI.create("file:///G:/Xmp");//创建UIR供paths使用，并不是本地创建
        Path path3 = Paths.get(u);
        Path path4 = FileSystems.getDefault().getPath("G:/", "Xmp");

        try {
            Path path5 = Paths.get("G:/","Xmp","dd.txt");//这种写法就不用加斜杠
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
            DirectoryStream<Path> files = Files.newDirectoryStream(Paths.get("G:/","Xmp"));
            //文件内容读取
            //比下面newBufferedReader耗时，不过此方法可以用并行处理提高效率
            List<String> lines1 = Files.readAllLines(path1.resolve("log.log"),StandardCharsets.UTF_8);//resolve作用：path拼接字符串返回path
            for (String line1 : lines1) {
//                System.out.println(line1);
            }
            //拿到BufferedReader，处理比上面快一点
            BufferedReader br = Files.newBufferedReader(path1.resolve("log.log"), StandardCharsets.UTF_8);
            String line2 = null;
            while ((line2 = br.readLine())!=null){
//                System.out.println(line2);
            }
            //写文件测试
            Files.write(path1.resolve("copy1.log"), lines1,StandardCharsets.UTF_8);
            BufferedWriter bw = Files.newBufferedWriter(path1.resolve("copy2.log"), StandardCharsets.UTF_8);
            bw.write("测试文件写操作");bw.newLine();//换行
            bw.flush();bw.close();

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
            Files.copy(System.in, path1.resolve("log_copy2.log"), StandardCopyOption.REPLACE_EXISTING);//
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
}
