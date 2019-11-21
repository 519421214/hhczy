package com.king.hhczy.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class ApiController {
}
