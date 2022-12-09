package com.dianxin.common.exceptionhandler;


import com.dianxin.common.constant.HttpStatus;
import com.dianxin.common.exception.NotLoginException;
import com.dianxin.common.exception.NotPermissionException;
import com.dianxin.common.exception.NotRoleException;
import com.dianxin.common.utils.AjaxResult;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;


@RestControllerAdvice
public class GlobalExceptionHandler
{

    /**
     * 权限码异常
     */
    @ExceptionHandler(NotLoginException.class)
    public AjaxResult handleNotLoginException(NotLoginException e, HttpServletRequest request)
    {

        return AjaxResult.error(HttpStatus.FORBIDDEN, "未登录，" + e.getMessage());
    }

    /**
     * 权限码异常
     */
    @ExceptionHandler(NotPermissionException.class)
    public AjaxResult handleNotPermissionException(NotPermissionException e, HttpServletRequest request)
    {
        return AjaxResult.error(HttpStatus.FORBIDDEN, "无权限：" + e.getMessage());
    }

    /**
     * 角色权限异常
     */
    @ExceptionHandler(NotRoleException.class)
    public AjaxResult handleNotRoleException(NotRoleException e, HttpServletRequest request)
    {
        return AjaxResult.error(HttpStatus.FORBIDDEN, "无角色：" + e.getMessage());
    }



    /**
     * 自定义验证异常
     */
    @ExceptionHandler(BindException.class)
    public AjaxResult handleBindException(BindException e)
    {
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return AjaxResult.error(message);
    }



}
