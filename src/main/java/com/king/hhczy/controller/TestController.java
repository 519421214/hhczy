package com.king.hhczy.controller;

import com.king.hhczy.base.config.CityCodeConfig;
import com.king.hhczy.common.result.RespBody;
import com.king.hhczy.service.ITblAccountService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ningjinxiang
 * @ClassName: ApiController
 * @Description: 对外服务提供controller
 * @date 2019年3月29日
 */
@RestController
@RequestMapping(value = "/api/V1")
@Slf4j
@Validated
@Api(value = "测试层接口")
public class TestController {
    @Autowired
    private ITblAccountService accountService;
    //    @Autowired
//    private ResourceLoader resourceLoader;
    @Autowired
    private CityCodeConfig cityCodeConfig;

    @GetMapping("/test-cache")
    @Cacheable(value = "myCache", key = "#areaId")
    @ApiOperation("测试springCache")
    public RespBody testResult(Integer areaId) {
        List<Map<String, String>> list = cityCodeConfig.getList();
//        return "完全OJBK";
//        return testService.getAreaByAreaId(areaId);
        return null;
    }
    @GetMapping("/test-mybatis-plus")
    @ApiOperation("测试各种分页查询")
    public RespBody testMybatisPlus() {
        List<Map<String, String>> list = cityCodeConfig.getList();
//        return "完全OJBK";
        return accountService.listAccounts();
    }

    @GetMapping("/writeToFile")
    @ApiOperation("测试XX")
    public ResponseEntity<?> writeToFile(@ApiParam(value = "编号",required = true)Long idNum, String path, int size) {
//        String fileName = "abc.txt";
        List<String> idNums = new ArrayList<>();
        try {
            Path path1 = Paths.get(path);
            if (!Files.exists(path1)) {
                Files.createFile(path1);
            }
            for (int i = 0; i < size; i++) {
                idNums.add(Long.toString(idNum + i));
            }
            Files.write(path1, idNums);
            return ResponseEntity.ok(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        idNums.add("生成失败");
        return null;
    }
    @PostMapping(value="/personAdd",consumes="multipart/form-data")
    @ApiOperation("后台管理添加/修改人员")
    @ApiImplicitParams({
            @ApiImplicitParam(name="type",value="人员类型(暂时只支持添加工作人员，type:2)",required=true,paramType="form",dataType="int"),
            @ApiImplicitParam(name="name",value="人员名称",required=true,paramType="form"),
            @ApiImplicitParam(name="companyCode",value="公司编码",required=true,paramType="form"),
            @ApiImplicitParam(name="personCode",value="人员编码（修改时要传）",required=false,paramType="form")
    })
    public ResponseEntity personAdd(@ApiParam(value = "人员照片", required = false)@RequestParam(required=false) MultipartFile data , @RequestParam(required=true) Integer type,
                               @RequestParam(required=true) String name, @RequestParam(required=true) String companyCode, @RequestParam(required=false) String personCode) {
        return ResponseEntity.ok(type+name+companyCode+personCode);
    }
}
