package com.dianxin.common.service;

import com.dianxin.common.domain.User;

public interface UserService {

    public User selectUserByName(String userName);
    public User selectUserById(Long userId);

}
