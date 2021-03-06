package com.calm.permission.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Create by Calm
 * 2020/6/12 16:49
 * 申请权限失败的回调
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionFailed {
    int requestCode();
}
