package com.king.hhczy.base.config;

import com.king.hhczy.common.util.SpringContextUtils;
import com.king.hhczy.common.util.WordsReading;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Auther: liaoshiyao
 * @Date: 2019/1/11 11:48
 * @Description: websocket 服务类
 */

/**
 * @ServerEndpoint 这个注解有什么作用？
 * 多例##
 * 这个注解用于标识作用在类上，它的主要功能是把当前类标识成一个WebSocket的服务端
 * 注解的值用户客户端连接访问的URL地址
 */

@Slf4j
@Component
@ServerEndpoint("/gp-read/{sid}")
public class WebSocketGsServer {

    /**
     * 与某个客户端的连接对话，需要通过它来给客户端发送消息
     */
    private Session session;
    //接收sid
    private String sid = "";
    private String host = "";
    /**
     * 用于存所有的连接服务的客户端，这个对象存储是安全的
     */
    private static ConcurrentHashMap<String, WebSocketGsServer> webSocketSet = new ConcurrentHashMap<>();

    //数据来源地址
    private String impUrl = "http://hq.sinajs.cn/list=";
    private Timer timer = new Timer();

    @OnOpen
    public void OnOpen(Session session, @PathParam("sid") String sid) {
        this.session = session;
        this.sid = sid;
        // name是用来表示唯一客户端，如果需要指定发送，需要指定发送通过name来区分
        webSocketSet.put(session.getId(), this);
        host = "http://"+session.getRequestURI().getHost()+":9099/file/";
        log.info("[WebSocket] 连接成功，当前连接人数为：={}", webSocketSet.size());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void OnClose() {
        webSocketSet.remove(session.getId());
        timer.cancel();
        log.info("[WebSocket] 退出成功，当前连接人数为：={}", webSocketSet.size());
    }

    /**
     * 收到客户端消息后调用的方法
     * maxMessageSize:字节数
     * 如下限制1M
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage(maxMessageSize = 1000000)
    public void onMessage(String message, Session session) {
        timer.cancel();
        timer = new Timer();
        log.info("收到来自窗口" + sid + "的信息:" + message);
        if (message.startsWith("[订阅]")) {
            autoRead(message);
        } else {
            sendInfo(message, null);
        }
    }

    /**
     * 定时播报
     * codes:逗号分开
     */
    public void autoRead(String inputCodes) {
        inputCodes = inputCodes.replace("[订阅]", "").replace("，", ",");

        RestTemplate restTemplate = SpringContextUtils.getBean(RestTemplate.class);
        Function<String, String> changeCode = x -> x.startsWith("6") ? ("sh" + x) : ("sz" + x);
        List<String> collect = Arrays.stream(inputCodes.split(",")).map(changeCode::apply).collect(Collectors.toList());

        String codesStr = collect.stream().reduce((s1, s2) -> s1 + "," + s2).orElse(null);
        //给个订阅提示
        StringBuffer sb = new StringBuffer("成功订阅：");
        String forObject = restTemplate.getForObject(impUrl + "sh000001," + codesStr, String.class);
        Arrays.stream(forObject.split(";")).map(x -> x.substring(x.indexOf("\"") + 1).split(",")).forEach(y -> {
            sb.append(y[0] + "，");
        });
        sendInfo("[system]" + sb.toString(), null);

        sendAudio(sid+"订阅成功");

        DecimalFormat df = new DecimalFormat("0.00");
        //缓存
        Map<String, Double> cacheHis = new HashMap<>();
        //开启定时任务,执行定时播报
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                String forObject = restTemplate.getForObject(impUrl + "sh000001," + codesStr, String.class);
                StringBuffer sayWords = new StringBuffer();
                Arrays.stream(forObject.split(";")).map(x -> x.substring(x.indexOf("\"") + 1).split(",")).forEach(y -> {
                    if (y.length < 5) return;
                    //获取盈亏占比
                    double ratio = Math.random() * 0.194 - 0.097;//测试用 todo
//                    double ratio = (Double.valueOf(y[3]) / Double.valueOf(y[2])) - 1;//正式用
                    //缓存点数
                    String name = y[0];//公司名
                    if (cacheHis.get(name) == null) {
                        cacheHis.put(name, ratio);
                    }
                    Double szzs = cacheHis.get(name);
                    double ratio_ = ratio - szzs;
                    if ("上证指数".equals(name)) {
                        if (cacheHis.get("aCur") == null) {
                            cacheHis.put("aCur", Double.parseDouble(y[3]));
                        }
                        if (ratio_ > 0.002) {
                            sayWords.append("拉升了，拉升了，大盘拉升了。");
                            cacheHis.put(name, ratio);
                        } else if (ratio_ < -0.002) {
                            cacheHis.put(name, ratio);
                            sayWords.append("跳水啦，崩盘啦，快跑。");
                        }
                        if (ratio > 0) {
                            if (szzs < 0) sayWords.append("牛逼，大盘翻红了。");
                        } else if (ratio < 0) {
                            if (szzs > 0) sayWords.append("握肏，大盘绿了。");
                        }
                        //突破提示
                        int curA = (int) Math.floor(Double.parseDouble(y[3]) / 100);
                        int hisA = (int) Math.floor(cacheHis.get("aCur") / 100);
                        int re = curA - hisA;
                        if (re != 0) {
                            if (re > 0) {
                                sayWords.append("大盘突破").append(curA * 100).append("点啦啦啦。");
                            } else {
                                sayWords.append("大盘跌破").append(hisA * 100).append("点，靠靠靠。");
                            }
                            cacheHis.put("aCur", Double.parseDouble(y[3]));
                        }
                    } else {
                        //--------------------其他----------------------
                        String ds = df.format(ratio * 100);
                        String curGj = y[3].substring(0, y[3].indexOf(".") + 3);//当前股价
                        if (ratio_ > 0.02) {
                            cacheHis.put(name, ratio);
                            sayWords.append(name + " 升天了。当前涨跌" + (ratio > 0 ? ("百分之" + ds) : ("负百分之" + ds.substring(1))) + "，价格为" + curGj + "元。");
                        } else if (ratio_ > 0.01) {
                            cacheHis.put(name, ratio);
                            sayWords.append(name + " 拉升了。当前涨跌" + (ratio > 0 ? ("百分之" + ds) : ("负百分之" + ds.substring(1))) + "，当前价格为" + curGj + "元。");
                        } else if (ratio_ < -0.02) {
                            cacheHis.put(name, ratio);
                            sayWords.append("请注意，" + name + "跳水啦。当前涨跌" + (ratio > 0 ? ("百分之" + ds) : ("负百分之" + ds.substring(1))) + "，价格为" + curGj + "元。");
                        } else if (ratio_ < -0.01) {
                            cacheHis.put(name, ratio);
                            sayWords.append("请注意，" + name + "下滑了。当前涨跌" + (ratio > 0 ? ("百分之" + ds) : ("负百分之" + ds.substring(1))) + "，价格为" + curGj + "元。");
                        }
                        if (cacheHis.get(name) != ratio) {
                            if (ratio > 0.097) {
                                sayWords.append("牛掰，" + name + "涨停了。");
                            } else if (ratio < -0.097) {
                                sayWords.append("他妈的，" + name + " 跌停了！！");
                            }else if (cacheHis.get(name)>0.097&&ratio<cacheHis.get(name)){
                                sayWords.append("靠，" + name + " 板不住，破板了！！");
                            }
                            cacheHis.put(name, ratio);
                        }
                        if (ratio > 0) {
                            if (szzs < 0) {
                                sayWords.append("牛逼plus，" + name + "翻红了。");
                                cacheHis.put(name, ratio);
                            }
                        } else if (ratio < 0) {
                            if (szzs > 0) {
                                sayWords.append("卧槽，" + name + "绿了。");
                                cacheHis.put(name, ratio);
                            }
                        }
                    }
                });
                if (sayWords.length() > 0) {
                    sendAudio(sayWords.toString());
                }
            }
        }, 0, 5000);
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        timer.cancel();
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        //同步发送
        this.session.getBasicRemote().sendText(message);
//        session.getAsyncRemote().sendText(message);//异步发送不阻塞
        //getBasicRemote().sendText(message);这个是同步发送
    }

    /**
     * 群发自定义消息
     */
    public void sendInfo(String message, String sid) {
        log.info("推送消息到窗口" + sid + "，推送内容:" + message);
        for (Map.Entry<String, WebSocketGsServer> entry : webSocketSet.entrySet()) {
            WebSocketGsServer v = entry.getValue();
            try {
                //这里可以设定只推送给这个sid的，为null则全部推送
                if (sid == null) {
                    v.sendMessage(message);
                } else if (v.session.getId().equals(sid)) {
                    v.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }

    /**
     * 群发带表示音频播放，线消息后音频
     */
    public void sendAudio(String message) {
        sendInfo("[system]" +"【"+sid+"】"+ message, null);
        String audioPath = WordsReading.build(message,host);
        sendInfo("[audio]" + audioPath, session.getId());
    }

}