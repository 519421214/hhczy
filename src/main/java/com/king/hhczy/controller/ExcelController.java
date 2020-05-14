package com.king.hhczy.controller;


import com.king.hhczy.entity.model.ExcelTest;
import com.pig4cloud.plugin.excel.annotation.ResponseExcel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * pig4cloud-excel导出实例
 * 基于Java的简单、省内存的读写Excel的开源项目。在尽可能节约内存的情况下支持读写百M的Excel。
 * 64M内存1分钟内读取75M(46W行25列)的Excel,当然还有急速模式能更快，但是内存占用会在100M多一点
 * @author ningjinxiang
 * @since 2019-08-08
 */
@RestController
@RequestMapping("/el")
public class ExcelController {
    /**
     * 单sheel
     * password:文档打开需要密码
     * 支持 alibaba/easyexcel 原生的配置注解
     * @return
     */
    @ResponseExcel(name = "文件名", sheet = "sheet1",password = "123456")
    @GetMapping("/export")
    public List<ExcelTest> e1() {
        return list();
    }

    /**
     * 多sheel
     * @return
     */
    @ResponseExcel(name = "文件名", sheet = {"sheet1","sheet2"})
    @GetMapping("/export2")
    public List<List<ExcelTest>> e12() {
        List<List<ExcelTest>> dataList = new ArrayList<>();
        dataList.add(list());
        dataList.add(list());
        return dataList;
    }

    /**
     * 生成数据
     * @return
     */
    private List<ExcelTest> list(){
        List<ExcelTest> dataList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            ExcelTest data = new ExcelTest();
            data.setString("tr1" + i);
            data.setDate(new Date());
            data.setDoubleData(i+0.01);
            dataList.add(data);
        }
        return dataList;
    }
}

