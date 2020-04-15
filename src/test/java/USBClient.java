import bean.Device;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Slf4j
public class USBClient {
    public static void main(String[] args) throws IOException {
        List<Device> devices = findDevices();
        portForwardBydevice(devices.get(0));

        Socket echoSocket;
        BufferedOutputStream  out;
        BufferedInputStream in;

        try {
            echoSocket = new Socket("127.0.0.1", devices.get(0).getPort());
            out = new BufferedOutputStream (echoSocket.getOutputStream());
            in = new BufferedInputStream(echoSocket.getInputStream());
            String readMsg = "";
            while (true) {
                // 将要返回的数据发送给pc
                try {
                    if (!echoSocket.isConnected()) {
                        break;
                    }
                    readMsg = readMsgFromSocket(in);
                    if (readMsg.length() == 0) {
                        break;
                    }
                    log.info(readMsg);
                    //todo 测试，console输入
                    Scanner sc = new Scanner(System.in);
                    out.write((sc.nextLine()).getBytes());
                    out.flush();
                } catch (IOException e) {
                    log.error("设备已断开");
                    e.printStackTrace();
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    //一个读取输入流的方法
    public static String readMsgFromSocket(InputStream in) {
        String msg = "";
        byte[] tempbuffer = new byte[1024];
        try {
            int numReadedBytes = in.read(tempbuffer, 0, tempbuffer.length);
            if (numReadedBytes==-1) {
                return "";
            }
            msg = new String(tempbuffer, 0, numReadedBytes, "utf-8");
        } catch (Exception e) {
            log.error("设备已断开");
            e.printStackTrace();
        }
        return msg;
    }
    public static List<Device> findDevices(){
        List<Device>devices = new ArrayList<Device>();
        String str = null;
        int port = 15000;
        Process process = null;
        Device device = null;
        String[] deviceStr = new String[2];
        List<String> lines=new ArrayList<String>();
        try {
            process = Runtime.getRuntime().exec("adb devices");
            InputStream in = process.getInputStream();
            BufferedReader read=new BufferedReader(new InputStreamReader(in));

            while ((str=read.readLine())!=null){
                lines.add(str);
                System.out.println(str);
            }
            for(int i=1;i<lines.size()-1;i++){
                str = lines.get(i);
                deviceStr = str.split("\t");
                if ("device".equals(deviceStr[1])) {
                    device = new Device();
                    device.setDeviceId(deviceStr[0]);
                    device.setState(deviceStr[1]);
                    // 目前先指定port；
                    device.setPort(port);
                    port++;
                    devices.add(device);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return devices;
    }
    public static void portForwardBydevice(Device device){
        String a = null;
        String b = null;
        String c = null;
        String d = null;

        a = "adb -s " + device.getDeviceId()+ " shell am broadcast -a NotifyServiceStop";
        b = "adb -s " + device.getDeviceId() + " forward tcp:"+device.getPort()+" tcp:1080";
        c = "adb -s " + device.getDeviceId()+ " shell am broadcast -a NotifyServiceStart";
        d= "adb -s " + device.getDeviceId()+ " shell am start -n com.newland.realmobiledetection/com.newland.realmobiledetection.system.activity.WelcomeActivity";
        log.error("......device...a.."+a);
        log.error("......device...b.."+b);
        log.error("......device...c.."+c);
        log.error("......device...d.."+d);
        try {
            Runtime.getRuntime().exec(d);
            Thread.sleep(500);
            Runtime.getRuntime().exec(a);
            Thread.sleep(500);
            Runtime.getRuntime().exec(b);
            Thread.sleep(500);
            Runtime.getRuntime().exec(c);
            Thread.sleep(500);
            log.error("端口映射完成。。");
        } catch (IOException e) {

            log.error("与手机通信异常"+e.getMessage());
        }catch (InterruptedException e) {
            log.error("线程中断异常"+e.getMessage());
        }

    }
}
