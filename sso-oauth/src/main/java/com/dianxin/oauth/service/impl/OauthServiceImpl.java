package com.dianxin.oauth.service.impl;

import com.dianxin.common.domain.User;
import com.dianxin.common.service.UserService;
import com.dianxin.common.utils.AjaxResult;
import com.dianxin.common.utils.JwtUtils;
import com.dianxin.oauth.param.LoginParam;
import com.dianxin.oauth.service.OauthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class OauthServiceImpl implements OauthService {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public AjaxResult login(LoginParam loginParam) {

        User user = userService.selectUserByName(loginParam.getUserName());
        if (user == null){
            return AjaxResult.error("用户名不存在！");
        }

        if (!user.getPassword().equals(loginParam.getPassword())){
            return AjaxResult.error("密码错误！");

        }

        String token = JwtUtils.createToken(user.getUserId());
        String key = "aouth:token:" + user.getUserId();


//        Object result = redisTemplate.opsForValue().get(key);
//        if (result != null){
//            return AjaxResult.error("不可以重复登录");
//        }


        redisTemplate.opsForValue().set(key, token, JwtUtils.EXPIRE, TimeUnit.MILLISECONDS);

        return AjaxResult.success("登录成功").put("token", token);
    }

    @Override
    public AjaxResult logout() {

        Long userId = JwtUtils.getUserId();

        String key = "aouth:token:" + userId;

        redisTemplate.delete(key);
        return AjaxResult.success("退出登录成功！");
    }
}