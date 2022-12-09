package com.dianxin.common.exception;

import org.apache.tomcat.util.buf.StringUtils;

import java.util.Arrays;

/**
 * 未能通过的权限认证异常
 */
public class NotPermissionException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public NotPermissionException(String permission)
    {
        super(permission);
    }

    public NotPermissionException(String[] permissions)
    {
        super(StringUtils.join(Arrays.asList(permissions), ','));
    }
}
