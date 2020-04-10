package bean;

import lombok.Data;

@Data
public class Device {
    private String deviceId;
    private String state;
    private Integer port;
}
