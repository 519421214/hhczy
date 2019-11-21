package jdk8;

import bean.Students;
import bean.User;
import com.alibaba.fastjson.JSONObject;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * 2、函数接口（@FunctionalInterface）只有一个实现方法，可以使用lambda表达式
 * （提倡接口的单一性，一个接口只做一件事情，清晰明了，接口间可以相互继承）
 * 3、接口的默认方法，接口可以增加一个实现方法而不用去改它的调用者的实现
 * 4/尽量使用方法引用System.out::println而不要写lambda表达式，避免生成lambda$0这样的函数，效率会快一点
 * 5/所有操作是链式调用 通过next指向下一个调用--针对无状态操作
 * 6、有状态操作会把无状态操作截断单独处理
 * 7、并行情况下
 */
@FunctionalInterface
interface DealNum{
    int dealResult(int x);

    default int add(int x, int y){
        return x + y;
    }
}

@FunctionalInterface
interface DealNum2 extends DealNum{

    default int add(int x, int y){
        return x + y;
    }
}

class Money{
    private int money;
    public Money(int money) {
        this.money = money;
    }

    /**
     * JDK默认会把当前实例传入到 非静态方法，参数名为this，位置是第一个
     * @param money
     */
    public void printMoney(Money this,Function<Integer,String> money){
        System.out.println("我的存款是:" + money.apply(this.money));
    }
}

public class StreamTest {
    public static void main(String[] args) throws Exception{

        collectStreamTest();
    }

    /**
     * 收集器测试
     * Collectors：收集器常用工具类
     */
    public static void collectStreamTest() {
        List<Students> students = Arrays.asList(new Students(1, "张三", 18),
                new Students(2, "张4", 26),
                new Students(2, "张5", 26),
                new Students(3, "张8", 16));
        //一般用法
        List<Integer> collect = students.stream().map(Students::getAge).collect(Collectors.toList());//还可以去重，排序等
        System.out.println(collect);
        //汇总信息，平均、最大、最小
        IntSummaryStatistics collect1 = students.stream().collect(Collectors.summarizingInt(Students::getAge));
        System.out.println(collect1);//IntSummaryStatistics{count=2, sum=44, min=18, average=22.000000, max=26}
        //分块 partitioningBy(一般是两大块 true和false)
        Map<Boolean, List<Students>> collect2 = students.stream().collect(Collectors.partitioningBy(s -> s.getAge() > 17));//成年与未成年分块
        System.out.println("分块："+ JSONObject.toJSON(collect2));
        //分组
        Map<Integer, List<Students>> collect3 = students.stream().collect(Collectors.groupingBy(Students::getAge));
        System.out.println("分组（根据年龄）："+ JSONObject.toJSON(collect3));
        Map<Integer, Long> collect4 = students.stream().collect(Collectors.groupingBy(Students::getAge, Collectors.counting()));
        System.out.println("分组（同龄人个数）："+ JSONObject.toJSON(collect4));

    }
    /**
     * 并行流实例测试
     */
    public static void parallelStreamTest() {
        //测试用例
        Consumer debug = x->{
            System.out.println(Thread.currentThread().getName()+":debug "+x);
//            System.out.println("debug "+x);
            try {
                TimeUnit.SECONDS.sleep(10);//休息3秒
            } catch (InterruptedException e) {
            }
        };
        //边界生成1到100数字并遍历,然后串行流执行,一次执行一个 peek中间操作(遇到终止操作前不执行) 与终止操作用法一样
//        IntStream.range(1,100).peek(debug::accept).count();
        //并行流一次执行的线程数默认是机器的核心数(cpu个数)
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "100");//设置线程个数
        IntStream.range(1,100).parallel().forEach(debug::accept);
        //串行流 sequential，与并行流parallel同时存在时，啥流与最后一次为准，不能同时存在(中间操作却不创建流，只修改head里面的一个并行标志)
        //串行流是主线程main，并行流是用jdk自带默认的线程ForkJoinPool.commonPool

        //自己创建线程池，防止其他并行任务被阻塞
        ForkJoinPool myPool = new ForkJoinPool(20);//20个并行任务
        myPool.submit(()->IntStream.range(1,100).parallel().forEach(debug::accept));
        myPool.shutdown();//关闭释放线程池
        //主线程 线程保护
        synchronized (myPool){
            try {
                myPool.wait();
            } catch (InterruptedException e) {
            }
        }
    }
    /**
     * stream常用方法 3.3
     */
    public static void streamProTest() {
        //中间操作：无状态操作（一般一个入参）（map/mapflat/filter/peek/unordered）-前后无依赖、有状态操作（一般两个入参）(distinct/sorted/limit/skip)-依赖计算完毕
        String str = "my name is 007";
        //filter:过滤掉长度小于3的；map 对数组的值转换输出，此处转为int
        Stream.of(str.split(" ")).filter(s->s.length()>2).map(s -> s.length()).forEach(System.out::println);
        //flatMap
        Stream.of(str.split(" ")).flatMap(s -> s.chars().boxed()).forEach(i->{//boxed 装箱
            System.out.println((char)i.intValue());//intValue 拆箱
        });
        //peek 用于debug，是个中间操作，和foreach功能一样，forEach是终止操作
        System.out.println("-----------peek-----------");
        Stream.of(str.split(" ")).peek(System.out::println);

        //终止操作：非短路操作、短路操作（拿到一个值就可以往下进行-结束流 findFirst findAny xxxMatch）
        //并行乱序
        str.chars().parallel().forEach(i-> System.out.print((char)i));//char 字符代码int流
        //解决并行乱序
        System.out.println();
        str.chars().parallel().forEachOrdered(i-> System.out.print((char)i));//char 字符代码int流
        //收集器 collect/toArray
        Stream.of(str.split(" ")).collect(Collectors.toList());//转list,收集到list

        //reduce的使用，入出都为String 拼接字符串
        Optional<String> letters = Stream.of(str.split(" ")).reduce((s, s2) -> s + "#" + s2);
        System.out.println(letters.orElse(""));//若用get(),空值报错，起始不会出现空样
        //另一种常用写法
        String reduce = Stream.of(str.split(" ")).reduce("", (s, s2) -> s + "#" + s2);//直接给一个初始值，不用判空,弊端：有时候前面会拼一个空样
        String reduce_ = Stream.of(str.replaceAll(" ","#")).reduce("", String::concat);//直接给一个初始值，不用判空
        System.out.println(reduce+":"+reduce_);
        //reduce计算所有单词总长度
        Integer reduce1 = Stream.of(str.split(" ")).map(String::length).reduce(0, Integer::sum);//直接给一个初始值0，不用判空
        System.out.println(reduce1);
        //短路操作实例
    }
    /**
     * Stream jdk8最大的亮点，高级迭代器，不是数据结构，不是集合，不存放数据，只注重怎么高效处理数据，流水线式处理数据
     *
     */
    public static void streamTest() {
        //创建流 创建-中间-终止 必须要终止，否则会报错
        //从集合创建
        List<Object> list = new ArrayList<>();
        list.stream();
        list.parallelStream();//并行流
        //从数组创建
        Arrays.stream(new int[]{1,2,3});
        //创建数字流
        IntStream.of(1, 2, 3);
        System.out.println("边界流，求和："+IntStream.rangeClosed(1, 100).sum());//从1加到100
        //使用random创建一个无限流
        new Random().ints().limit(10);//限制生成最大个数10
        //自己产生流
        Random random = new Random();
        Stream.generate(() -> random.nextInt()).limit(20);
        //==============================================流创建完毕===========================
        int[] num = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        System.out.println("求和：" + IntStream.of(num).sum());
        System.out.println("并行流（多线程多人同时访问）求和：" + IntStream.of(num).parallel().sum());
        //map 中间操作（返回stream的操作）sum 终止操作
        //stream惰性求值：惰性求值：终止操作没有被调用下，中间操作不会执行
        IntStream intStream = IntStream.of(num).map(x -> x * 10);//x -> x * 10 不会执行
        System.out.println("中间操作被执行 求和：" + intStream.sum());
    }
    public static void functionTest(){
        DealNum result = x -> x % 2;
        System.out.println(result.dealResult(2));
        //无参构造方法引用
//        Supplier<Money> aNew = Money::new;
//        Money money = new Money(99999999);
        Function<Integer, Money> money = Money::new;
        Function<Integer, String> myMoney = new DecimalFormat("#,###")::format;//数字格式转换
        money.apply(99999999);
        //用类名+方法名的形式可以省掉->,取而代之::（也可以实例+方法）
//        //函数接口链式操作
        money.apply(99999999).printMoney(myMoney.andThen(x -> "￥" + x));
//        money.printMoney(myMoney);
//        //函数接口链式操作
//        money.printMoney(myMoney.andThen(x -> "￥" + x));
    }
    //去重测试
    public static void qc(){
        List<User> distinctList = new ArrayList();
        User user1 = new User();
        user1.setId(111);
        distinctList.add(user1);
        User user2 = new User();
        user2.setId(222);
        distinctList.add(user2);
        User user3 = new User();
        user3.setId(333);
        distinctList.add(user3);
        User user4 = new User();
        user4.setId(333);
        distinctList.add(user4);
        //list是需要去重的list，返回值是去重后的list
        List<User> res = distinctList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getId()))), ArrayList::new));
        //筛选
        res = distinctList.stream().filter(ls->ls.getId().equals(222)).collect(Collectors.toList());

        System.out.println(res);
    }
}
