package com.calm.permission;

import android.content.Context;
import android.util.Log;

import com.calm.permission.activity.PermissionActivity;
import com.calm.permission.annotations.PermissionApply;
import com.calm.permission.annotations.PermissionFailed;
import com.calm.permission.annotations.PermissionForever;
import com.calm.permission.interfaces.PermissionCallback;
import com.calm.permission.utils.PermissionUtil;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import androidx.fragment.app.Fragment;

/**
 * Create by Calm
 * 2020/6/12 15:37
 * 权限申请切入点
 */
@Aspect
public class PermissionAspect {
    @Pointcut("execution(@com.calm.permission.annotations.PermissionApply * * (..)) " +
            "&& @annotation(permissions)")
    public void pointMethod(PermissionApply permissions){}
    @Around("pointMethod(permissions)")
    public void permissionApply(final ProceedingJoinPoint joinPoint, PermissionApply permissions){
        Context context = null;
        //获取到切入的对象
        final Object aThis = joinPoint.getThis();
        if(aThis instanceof Context){
            context = (Context) aThis;
        }else if(aThis instanceof Fragment){
            context = ((Fragment)aThis).getContext();
        }
        //不符合需要切入的条件，执行原方法
        if(context == null || permissions == null ||
                permissions.value() == null || permissions.value().length == 0){
            try {
                joinPoint.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return;
        }
        //获取到注解中的数据 要申请的权限数组
        String[] values = permissions.value();
        //请求码
        final int requestCode = permissions.requestCode();
        PermissionActivity.launchActivity(context, values, requestCode, new PermissionCallback() {
            @Override
            public void permissionApplySuccess() {
                /**
                 * 申请权限成功，执行原程序逻辑
                 * 注意这里有个坑，因为用了try-catch 如果原程序里面的异常在这里被捕获了，
                 * 不会被Thread.UncaughtExceptionHandler这个类接管到了这是第一点
                 * 第二点是如果原程序的逻辑如果本来会明显出现崩溃情况的，由于这里try-catch的原因，
                 * 不会崩溃了，可能会对测试结果产生一些影响
                 */
                try {
                    joinPoint.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            @Override
            public void permissionApplyFailed() {
                PermissionUtil.invokeMethod(aThis, PermissionFailed.class,requestCode);
            }

            @Override
            public void permissionApplyForever() {
                PermissionUtil.invokeMethod(aThis, PermissionForever.class,requestCode);
            }
        });
    }
}
