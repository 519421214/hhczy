package utils;

import org.junit.Test;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * @author ningjinxiang
 */
public class IpV4Util {

    public static final String DEFAULT_SUBNET_MASK_A = "255.0.0.0";
    public static final String DEFAULT_SUBNET_MASK_B = "255.255.0.0";
    public static final String DEFAULT_SUBNET_MASK_C = "255.255.255.0";
    public static final String TYPE_IP_A = "A";
    public static final String TYPE_IP_B = "B";
    public static final String TYPE_IP_C = "C";
    public static final String TYPE_IP_D = "D";
    public static final String TYPE_IP_LOCATE = "locate";
    //  @Test
    //  public void test() {
    //      System.out.println(isSameAddress("192.168.1.1", "192.168.1.5", DEFAULT_SUBNET_MASK_C));
    //
    //  }
    @Test
    public void test() {
//        String ip_a="127.0.01.15";
//        String ip_c="192.168.1.1";
//        String binaryIp = getBinaryIp("192.168.1.1");
//        System.out.println(getIpType(ip_a));
        System.out.println(isSameAddress("192.168.56.97", "192.168.51.66"));

    }
    public static boolean isSameAddress(String resourceIp, String requestIp) {
        if (!isIp(resourceIp)||!isIp(requestIp)) {
            return false;
        }
        if (getIpType(resourceIp).equals(getIpType(requestIp))){
            return isSameAddress(resourceIp,requestIp,getIpDefaultMask(getIpType(resourceIp)));
        }
        return false;
    }
    //通过ip类型，获取默认IP子网掩码
    private static String getIpDefaultMask(String ipType){
        switch (ipType){
            case TYPE_IP_A:return DEFAULT_SUBNET_MASK_A;
            case TYPE_IP_B:return DEFAULT_SUBNET_MASK_B;
            case TYPE_IP_C:return DEFAULT_SUBNET_MASK_C;
            default:return "没有对应的mask地址";
        }
    }

    public static boolean isSameAddress(String resourceIp, String requestIp, String subnetMask) {
        if (!isIp(resourceIp)||!isIp(requestIp)||!isIp(subnetMask)) {
            return false;
        }
        String resourceAddr = getAddrIp(resourceIp, subnetMask);
        String subnetMaskAddr = getAddrIp(requestIp, subnetMask);
        if (resourceAddr.equals(subnetMaskAddr)) {
            return true;
        }
        return false;
    }
    //获取ip的二进制字符串
    private static String getBinaryIp(String data) {
        String[] datas = data.split("\\.");
        String binaryIp = "";
        for (String ipStr : datas) {
            long signIp = Long.parseLong(ipStr);
            String binary = Long.toBinaryString(signIp);
            long binaryInt = Long.parseLong(binary);
            binaryIp += String.format("%08d", binaryInt);
        }
        return binaryIp;
    }
    //获取ip的地址位
    private static String getAddrIp(String ip, String subnetMask) {
        StringBuilder addrIp = new StringBuilder();
        String binaryIp = getBinaryIp(ip);
        String binarySubnetMask = getBinaryIp(subnetMask);
        for (int i = 0; i < 32; i++) {
            byte ipByte = Byte.parseByte(String.valueOf(binaryIp.charAt(i)));
            byte subnetMaskByte = Byte.parseByte(String.valueOf(binarySubnetMask.charAt(i)));
            addrIp.append(ipByte & subnetMaskByte);
        }
        return addrIp.toString();
    }

    public static String getIpType(String ip) {
        String binaryIp = getBinaryIp(ip);
        System.out.println(binaryIp);
        if (ip.startsWith("127")){
            return TYPE_IP_LOCATE;
        }
        if (binaryIp.startsWith("0")){
            return TYPE_IP_A;
        }
        if (binaryIp.startsWith("10")){
            return TYPE_IP_B;
        }
        if (binaryIp.startsWith("110")){
            return TYPE_IP_C;
        }
        if (binaryIp.startsWith("1110")){
            return TYPE_IP_D;
        }
        return  "无效ip异常";
    }
    private static boolean isIp(String ip){
        if (StringUtils.hasText(ip)) {
            Pattern compile = Pattern.compile("^(\\d{1,3}\\.){3}\\d{1,3}$");
            return compile.matcher(ip).matches();
        }
        return false;
    }
}
