package com.dianxin.user.controller;

import com.dianxin.common.annotation.RequiresLogin;
import com.dianxin.common.domain.User;
import com.dianxin.common.service.UserService;
import com.dianxin.common.utils.AjaxResult;
import com.dianxin.common.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequiresLogin
    @GetMapping
    public AjaxResult info(){

        Long userId = JwtUtils.getUserId();
        User user = userService.selectUserById(userId);
        return AjaxResult.success(user);

    }




}
