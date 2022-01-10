package com.xsh.emos.wx.controller;

import com.xsh.emos.wx.common.util.R;
import com.xsh.emos.wx.controller.form.TestSayHelloForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author : xsh
 * @create : 2022-01-10 - 3:20
 * @describe:
 */
@RestController
@RequestMapping("/test")
@Api("测试Web接口")
public class TestController {

    @PostMapping("/sayHello")
    @ApiOperation("最简单的测试方法")
    public R sayHello(@Valid @RequestBody TestSayHelloForm form){
        return R.ok().put("data","Hello,".concat(form.getName()));
    }
}
