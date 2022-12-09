package com.dianxin.user.controller;

import com.dianxin.common.annotation.RequiresLogin;
import com.dianxin.common.annotation.RequiresPermissions;
import com.dianxin.common.annotation.RequiresRoles;
import com.dianxin.common.utils.AjaxResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class MyTestController {


    @RequiresLogin
    @GetMapping("/login")
    public AjaxResult login(){
        return AjaxResult.success("登录认证成功");
    }

    @RequiresRoles("role_a")
    @GetMapping("/role")
    public AjaxResult role(){
        return AjaxResult.success("角色认证成功");
    }

    @RequiresPermissions("perm_b")
    @GetMapping("/perm")
    public AjaxResult perm(){
        return AjaxResult.success("权限认证成功");
    }

}
