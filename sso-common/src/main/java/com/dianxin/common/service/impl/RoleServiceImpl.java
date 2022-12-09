package com.dianxin.common.service.impl;

import com.dianxin.common.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoleServiceImpl implements RoleService {

    private static Map<Long, List<String>> roles;

    static {
        roles = new HashMap<>();
        roles.put(1L, Arrays.asList("root", "role_a", "role_b"));
        roles.put(2L, Arrays.asList("role_a"));
        roles.put(3L, Arrays.asList("role_b"));

    }


    @Override
    public List<String> selectRolesByUserId(Long userId) {
        return roles.get(userId);
    }
}
