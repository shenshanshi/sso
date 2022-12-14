package com.dianxin.common.service.impl;

import com.dianxin.common.domain.User;
import com.dianxin.common.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {


    /**
     *  模拟数据
     */
    private static List<User> users;

    /**
     * 静态初始化
     */
    static {
        users = new ArrayList<>();
        users.add(new User(1L, "root", "123456"));
        users.add(new User(2L, "user_a", "123456"));
        users.add(new User(3L, "user_b", "123456"));
    }


    @Override
    public User selectUserByName(String userName) {
        if (StringUtils.isEmpty(userName)){
            return null;
        }
        for (User user: users){
            if (userName.equals(user.getUserName()))
                return user;
        }
        return null;
    }

    @Override
    public User selectUserById(Long userId) {

        if (userId == null){
            return null;
        }
        for (User user: users){
            if (userId.equals(user.getUserId()))
                return user;
        }
        return null;

    }
}
