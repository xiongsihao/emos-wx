package com.xsh.emos.wx.controller;

import com.xsh.emos.wx.common.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : xsh
 * @create : 2022-01-10 - 3:20
 * @describe:
 */
@RestController
@RequestMapping("/test")
@Api("测试Web接口")
public class TestController {

    @GetMapping("/sayHello")
    @ApiOperation("最简单的测试方法")
    public R sayHello(){
        return R.ok().put("data","HelloWorld");
    }
}
