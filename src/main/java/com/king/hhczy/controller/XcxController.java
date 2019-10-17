package com.king.hhczy.controller;

import com.king.hhczy.common.result.BaseResultCode;
import com.king.hhczy.common.result.RespBody;
import com.king.hhczy.entity.model.PostList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ningjinxiang
 * @ClassName: ApiController
 * @Description: 对外服务提供controller
 * @date 2019年3月29日
 */
@RestController
@RequestMapping(value = "/xcx/V1")
@Slf4j
@Validated
@Api(value = "小程序测试层接口")
public class XcxController {

    @GetMapping("/test1")
    @ApiOperation("小程序测试数据")
    public RespBody testResult(Integer num) {
        RespBody respBody = new RespBody();
        respBody.result(BaseResultCode.SUCCESS);
        List<PostList> postlists = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            PostList postList = new PostList();
            postList.setId(i);
            postList.setAvatar("/images/zst.jpg");
            postList.setDateTime(LocalDateTime.now());
            postList.setTitle("无题"+i);
            postList.setContent("经济殴打哈我的护腕开的户我看开始号地块吴爱华昆山底开挖开挖多少瓦库电话看开挖电话看");
            postList.setCollection(112);
            postList.setReading(200);
            postList.setImgSrc("/images/圆形图片.png");
            postlists.add(postList);
        }
        respBody.setData(postlists);
        return respBody;
    }

    private void PostList() {
    }

}
