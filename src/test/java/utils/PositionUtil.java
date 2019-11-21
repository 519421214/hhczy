package utils;

/**
 * 各地图API坐标系统比较与转换;
 * WGS84坐标系：即地球坐标系，国际上通用的坐标系。设备一般包含GPS芯片或者北斗芯片获取的经纬度为WGS84地理坐标系,
 * 谷歌地图采用的是WGS84地理坐标系（中国范围除外）;
 * GCJ02坐标系：即火星坐标系，是由中国国家测绘局制订的地理信息系统的坐标系统。由WGS84坐标系经加密后的坐标系。
 * 谷歌中国地图和搜搜中国地图采用的是GCJ02地理坐标系; BD09坐标系：即百度坐标系，GCJ02坐标系经加密后的坐标系;
 * 搜狗坐标系、图吧坐标系等，估计也是在GCJ02基础上加密而成的。
 */
public class PositionUtil {

    private static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

    /**
     * 中国正常坐标系GCJ02协议的坐标，转到 百度地图对应的 BD09 协议坐标
     * @param lon 经度
     * @param lat 维度
     * @return double[0]：lon经度 double[1]：lat维度
     */
    public static double[] gcj02ToBd09(double lon, double lat) {
        double x = lon, y = lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        lon = z * Math.cos(theta) + 0.0065;
        lat = z * Math.sin(theta) + 0.006;
        return new double[]{lon,lat};
    }

    /**
     * 百度地图对应的 BD09 协议坐标，转到 中国正常坐标系GCJ02协议的坐标
     * @param lon 经度
     * @param lat 维度
     * @return double[0]：lon经度 double[1]：lat维度
     */
    public static double[] bd09ToGcj02(double lon, double lat) {
        double x = lon - 0.0065, y = lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        lon = z * Math.cos(theta);
        lat = z * Math.sin(theta);
        return new double[]{lon,lat};
    }
}