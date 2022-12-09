package com.dianxin.oauth.service;

import com.dianxin.common.utils.AjaxResult;
import com.dianxin.oauth.param.LoginParam;

public interface OauthService {

    AjaxResult login(LoginParam loginParam);

    AjaxResult logout();
}
