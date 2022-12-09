package com.dianxin.common.service.impl;

import com.dianxin.common.service.PermissionService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PermissionServiceImpl implements PermissionService {

    private static Map<Long, List<String>> permissions;

    static {
        permissions = new HashMap<Long, List<String>>();
        permissions.put(1L, Arrays.asList("root", "perm_a", "perm_b"));
        permissions.put(2L, Arrays.asList("perm_a"));
        permissions.put(3L, Arrays.asList("perm_b"));

    }

    @Override
    public List<String> selectPermissionsByUserId(Long UserId) {
        return permissions.get(UserId);
    }
}
