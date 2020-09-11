package com.king.hhczy.controller;

import com.king.hhczy.common.result.BaseResultCode;
import com.king.hhczy.common.result.RespBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ningjinxiang
 * @ClassName: ApiController
 * @Description: 对外服务提供controller
 * @date 2019年3月29日
 */
@RestController
@RequestMapping
@Slf4j
@Validated
public class ApiController {
    @GetMapping("/message")
    public String message() {
        return "http://192.168.119.34:6222/gmvcs/uom/device/workstation/unauthorized/rest/updateRegistrationStrategy";
    }
    @PostMapping("/oauth/authentication-code")
    public RespBody oauth(@RequestBody Map val) {
        RespBody respBody = new RespBody();
        Map<String, String> map = new HashMap<>();
        map.put("auth_key","7208a45616b4d0a659469eb34e4664f2");
        respBody.setData(map);
        respBody.result(BaseResultCode.SUCCESS);
        return respBody;
    }
    @PostMapping("/api/v1/4/site/police")
    public RespBody police(@RequestBody Map val) {
        RespBody respBody = new RespBody();
        List<Map> maps = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("alarm","116025");
        map.put("username","冉涛");
        map.put("password","8615fb5fa217052117e2566bd62f73ac");
        map.put("identitycard","512201******191317");
        map.put("orgcode",val.get("org_code"));
        map.put("orgname","重庆市万州区公安局交通巡逻警察支队");
        maps.add(map);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("alarm","116025");
        map2.put("username","冉涛");
        map2.put("password","8615fb5fa217052117e2566bd62f73ac");
        map2.put("identitycard","512201******191317");
        map2.put("orgcode",val.get("org_code"));
        map2.put("orgname","重庆市万州区公安局交通巡逻警察支队2");
        maps.add(map2);
        respBody.setData(maps);
        respBody.result(BaseResultCode.SUCCESS);
        return respBody;
    }
    @PostMapping("/api/v1/4/site/lawCamera")
    public RespBody lawCamera(@RequestBody Map val) {
        RespBody respBody = new RespBody();
        List<Map> maps = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("no","123456");
        map.put("orgcode","500107810000");
        map.put("orgname","重庆市万州区公安局交通巡逻警察支队");
        map.put("alarm","300918");
        map.put("username","冉涛");
        maps.add(map);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("no","654321");
        map2.put("orgcode","500107810000");
        map2.put("orgname","重庆市万州区公安局交通巡逻警察支队");
        map2.put("alarm","300918");
        map2.put("username","冉涛");
        maps.add(map2);
        respBody.setData(maps);
        respBody.result(BaseResultCode.SUCCESS);
        return respBody;
    }
}
