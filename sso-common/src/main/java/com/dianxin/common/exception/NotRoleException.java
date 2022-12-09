package com.dianxin.common.exception;

import org.apache.tomcat.util.buf.StringUtils;

import java.util.Arrays;

/**
 * 未能通过的角色认证异常
 */
public class NotRoleException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public NotRoleException(String role)
    {
        super(role);
    }

    public NotRoleException(String[] roles)
    {
        super(StringUtils.join(Arrays.asList(roles), ','));
    }
}