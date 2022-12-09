package com.dianxin.oauth.param;


import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class LoginParam {

    @NotBlank(message = "用户名不能为空")
    @Length(min = 2, max = 10, message = "用户名长度为2-10个字符")
    private String userName;

    @NotBlank(message = "密码不能为空")
    @Length(min = 6, max = 16, message = "密码长度为6-10个字符")
    private String password;
}
