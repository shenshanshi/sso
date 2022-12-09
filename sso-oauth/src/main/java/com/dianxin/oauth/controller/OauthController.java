package com.dianxin.oauth.controller;

import com.dianxin.common.utils.AjaxResult;
import com.dianxin.oauth.param.LoginParam;
import com.dianxin.oauth.service.OauthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OauthController {


    @Autowired
    private OauthService oauthService;

    @PostMapping("/login")
    public AjaxResult login(@RequestBody @Validated LoginParam loginParam){


        AjaxResult loginResult = oauthService.login(loginParam);

        return loginResult;
    }

    @PostMapping("/logout")
    public AjaxResult logout(){

        AjaxResult logoutResult = oauthService.logout();

        return logoutResult;
    }





}
