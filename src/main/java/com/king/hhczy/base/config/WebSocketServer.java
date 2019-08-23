package com.king.hhczy.base.config;

import com.king.hhczy.common.util.SpringContextUtils;
import com.king.hhczy.service.ITblAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: liaoshiyao
 * @Date: 2019/1/11 11:48
 * @Description: websocket 服务类
 */

/**
 *
 * @ServerEndpoint 这个注解有什么作用？
 * 多例##
 * 这个注解用于标识作用在类上，它的主要功能是把当前类标识成一个WebSocket的服务端
 * 注解的值用户客户端连接访问的URL地址
 *
 */

@Slf4j
@Component
@ServerEndpoint("/websocket/{sid}")
public class WebSocketServer {

    /**
     *  与某个客户端的连接对话，需要通过它来给客户端发送消息
     */
    private Session session;
    //接收sid
    private String sid="";
    /**
     *  用于存所有的连接服务的客户端，这个对象存储是安全的
     */
    private static ConcurrentHashMap<String,WebSocketServer> webSocketSet = new ConcurrentHashMap<>();


    @OnOpen
    public void OnOpen(Session session, @PathParam(value = "sid") String sid){
        this.session = session;
        this.sid=sid;
        // name是用来表示唯一客户端，如果需要指定发送，需要指定发送通过name来区分
        webSocketSet.put(sid,this);
        log.info("[WebSocket] 连接成功，当前连接人数为：={}",webSocketSet.size());
    }
    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void OnClose(){
        webSocketSet.remove(this.sid);
        log.info("[WebSocket] 退出成功，当前连接人数为：={}",webSocketSet.size());
    }

    /**
     * 收到客户端消息后调用的方法
     * maxMessageSize:字节数
     * 如下限制1M
     * @param message 客户端发送过来的消息*/
    @OnMessage(maxMessageSize = 1000000)
    public void onMessage(String message, Session session) {
        log.info("收到来自窗口"+sid+"的信息:"+message);
        //群发消息
        webSocketSet.forEach((k,v)->{
            try {
                v.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    /**
     * 接收消息的多态性
     * @param message
     * @param session
     */
    @OnMessage(maxMessageSize = 100000000)
    public void onMessage(byte[] message, Session session) {
        //由于webSocket是多例的（spring是单例），service直接@AutoWired第二个人进来会拿不到，可用以下方法或者service声明为静态
        ITblAccountService service = SpringContextUtils.getBean(ITblAccountService.class);
    }
    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
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
     * */
    public static void sendInfo(String message,@PathParam("sid") String sid) throws IOException {
        log.info("推送消息到窗口"+sid+"，推送内容:"+message);
        for (Map.Entry<String, WebSocketServer> entry : webSocketSet.entrySet()) {
            WebSocketServer v = entry.getValue();
            try {
                //这里可以设定只推送给这个sid的，为null则全部推送
                if (sid == null) {
                    v.sendMessage(message);
                } else if (v.sid.equals(sid)) {
                    v.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }

}