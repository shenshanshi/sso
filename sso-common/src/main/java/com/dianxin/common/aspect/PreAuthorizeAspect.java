package com.dianxin.common.aspect;


import com.dianxin.common.annotation.Logical;
import com.dianxin.common.annotation.RequiresLogin;
import com.dianxin.common.annotation.RequiresPermissions;
import com.dianxin.common.annotation.RequiresRoles;
import com.dianxin.common.exception.NotLoginException;
import com.dianxin.common.exception.NotPermissionException;
import com.dianxin.common.exception.NotRoleException;
import com.dianxin.common.service.PermissionService;
import com.dianxin.common.service.RoleService;
import com.dianxin.common.utils.JwtUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 基于 Spring Aop 的注解鉴权
 */
@Aspect
@Component
public class PreAuthorizeAspect
{

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 构建
     */
    public PreAuthorizeAspect()
    {
    }

    /**
     * 定义AOP签名 (切入所有使用鉴权注解的方法)
     */
    public static final String POINTCUT_SIGN = " @annotation(com.dianxin.common.annotation.RequiresLogin) || "
            + "@annotation(com.dianxin.common.annotation.RequiresPermissions) || "
            + "@annotation(com.dianxin.common.annotation.RequiresRoles)";

    /**
     * 声明AOP签名
     */
    @Pointcut(POINTCUT_SIGN)
    public void pointcut()
    {
    }

    /**
     * 环绕切入
     *
     * @param joinPoint 切面对象
     * @return 底层方法执行后的返回值
     * @throws Throwable 底层方法抛出的异常
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable
    {
        // 注解鉴权
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        checkMethodAnnotation(signature.getMethod());
        try
        {
            // 执行原有逻辑
            Object obj = joinPoint.proceed();
            return obj;
        }
        catch (Throwable e)
        {
            throw e;
        }
    }

    /**
     * 对一个Method对象进行注解检查
     */
    public void checkMethodAnnotation(Method method) throws NotLoginException {
        // 校验 @RequiresLogin 注解
        RequiresLogin requiresLogin = method.getAnnotation(RequiresLogin.class);
        if (requiresLogin != null)
        {

            checkLogin();
//            System.out.println("----------登录校验");
        }

        // 校验 @RequiresRoles 注解
        RequiresRoles requiresRoles = method.getAnnotation(RequiresRoles.class);
        if (requiresRoles != null)
        {
            checkRole(requiresRoles);
//            System.out.println("----------角色校验");

        }

        // 校验 @RequiresPermissions 注解
        RequiresPermissions requiresPermissions = method.getAnnotation(RequiresPermissions.class);
        if (requiresPermissions != null)
        {
            checkPermi(requiresPermissions);
//            System.out.println("----------权限校验");
        }
    }




    private void checkLogin() throws NotLoginException {

        String jwtToken = JwtUtils.getToken();
        if (jwtToken == null){
            throw new NotLoginException("token为空");
        }
        Object result = redisTemplate.opsForValue().get(jwtToken);
        if (result == null){
             throw new NotLoginException("token无效");
        }

    }

    private void checkRole(RequiresRoles requiresRoles) {
        checkLogin();
        if (requiresRoles.logical() == Logical.AND){
            checkRoleAnd(requiresRoles.value());
        }else{
            checkRoleOr(requiresRoles.value());
        }

    }

    private void checkRoleOr(String... roles) {
        List<String> roleList = roleService.selectRolesByUserId(JwtUtils.getUserId());
        for(String role : roles){

            if (roleList.contains(role)){
                return;
            }

        }
        if (roles.length > 0){
            throw new NotRoleException(roles);
        }


    }

    private void checkRoleAnd(String... roles) {
        List<String> roleList = roleService.selectRolesByUserId(JwtUtils.getUserId());
        for(String role : roles){

            if (!roleList.contains(role)){
                throw new NotRoleException(roles);
            }

        }
    }


    private void checkPermi(RequiresPermissions requiresPermissions) {
        checkLogin();
        if (requiresPermissions.logical() == Logical.AND){
            checkPermiAnd(requiresPermissions.value());
        }else{
            checkPermiOr(requiresPermissions.value());
        }
    }

    private void checkPermiOr(String... permissions) {
        List<String> permissionList = permissionService.selectPermissionsByUserId(JwtUtils.getUserId());
        for(String permission : permissions){

            if (permissionList.contains(permission)){
                return;
            }

        }
        if (permissions.length > 0){
            throw new NotPermissionException(permissions);
        }
    }

    private void checkPermiAnd(String... permissions) {
        List<String> permissionList = permissionService.selectPermissionsByUserId(JwtUtils.getUserId());
        for(String permission : permissions){

            if (!permissionList.contains(permission)){
                throw new NotPermissionException(permissions);
            }

        }
    }
}
