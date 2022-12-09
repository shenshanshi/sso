package com.dianxin.common.service;

import java.util.List;

public interface PermissionService {
    public List<String> selectPermissionsByUserId(Long UserId);
}
