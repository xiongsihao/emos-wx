package com.xsh.emos.wx.controller.form;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author : xsh
 * @create : 2022-01-10 - 22:37
 * @describe:
 */
@ApiModel
@Data
public class TestSayHelloForm {

    // \\u4e00-\\u9fa5为简体中文字符范围;{2,15}表示最少两个最多15个
    @NotBlank
    @Pattern(regexp = "^[\\u4e00-\\u9fa5]{2,15}$")
    private String name;
}
