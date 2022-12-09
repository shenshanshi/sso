package com.dianxin.common.service;

import java.util.List;

public interface RoleService  {
    public List<String> selectRolesByUserId(Long userId);
}
